package com.cornelius;

import javax.swing.*;
import java.awt.*;

import static com.cornelius.MessageBox.MESSAGE_Y_SPACING;
import static com.cornelius.MessageBox.messages;

public class MainFrame extends JFrame {
    static final int FRAME_WIDTH = 400;
    static final int FRAME_HEIGHT = 600;
    //TODO Make this variable function
    private boolean isHost = false;
    private double scroll = 0;

    public MainFrame(boolean host) {
        super("Secret Student Chatroom | " + Main.version);
        this.isHost = host;
    }

    @Override
    public void setSize(int width, int height) {
        //Disabled setSize
    }

    @Override
    public void setVisible(final boolean visible) {
        if (visible) {
            super.setLayout(null);
            super.setSize(FRAME_WIDTH, FRAME_HEIGHT);
            super.setResizable(false);
            super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            super.setBackground(Color.WHITE);
            super.setForeground(Color.WHITE);
        }
        super.setVisible(visible);
    }

    public void addYourMessage(String message) {
        if (!this.isHost) {
            Client.writeToServer(message);
        }
        add(new MessageBox(message, UserType.YOU));
        if (MessageBox.messages.size() > 1) {
            moveMessagesUp();
        }

    }

    public void addForeignMessage(String message) {
        MessageBox m = new MessageBox(message, UserType.FOREIGN);
        add(m);
        if (MessageBox.messages.size() > 1) {
            moveMessagesUp();
        }
    }

    public void moveMessagesUp() {
        for (int i = 0; i < messages.size(); i++) {
            if (i == messages.size() - 1) {
                break;
            }
            MessageBox m = messages.get(i);
            int og = m.getY();
            m.setBounds(m.getX(), og - MESSAGE_Y_SPACING, m.getWidth(), m.getHeight());
            System.out.println(m.getMessageText() + " " + og + " -> " + m.getY());

        }
    }
}
