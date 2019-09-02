package com.cornelius;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.util.ArrayList;

enum UserType {FOREIGN, YOU, INFO}

public class MessageBox extends JPanel {
    public static final ArrayList<MessageBox> messages = new ArrayList<>();
    public static final int INITIAL_YOU_MESSAGE_X = MainFrame.FRAME_WIDTH - 70;
    public static final int INITIAL_YOU_MESSAGE_Y = 500;
    public static final int INITIAL_YOU_MESSAGE_WIDTH = 50;
    public static final int INITIAL_YOU_MESSAGE_HEIGHT = 25;
    public static final int INITIAL_FOREIGN_MESSAGE_X = 7;
    public static final int INITIAL_FOREIGN_MESSAGE_Y = 500;
    public static final int INITIAL_FOREIGN_MESSAGE_WIDTH = 50;
    public static final int INITIAL_FOREIGN_MESSAGE_HEIGHT = 25;
    public static final int MESSAGE_Y_SPACING = 26;
    public static final int CHARACTER_WIDTH = 11;
    public static final int CHARACTER_HEIGHT = 18;
    public static final int MAX_CHARACTERS_ON_LINE = 0;
    public static final int WEIRD_WIDTH_THING = 15;
    private JLabel textLabel;
    private String messageText;

    public MessageBox(String message, UserType type) {
        super();
        if (type != UserType.INFO) {

            super.setBorder(type == UserType.YOU ? BorderFactory.createSoftBevelBorder(BevelBorder.RAISED, new Color(59, 67, 255), new Color(0, 0, 176)) : BorderFactory.createSoftBevelBorder(0));
        }
        this.messageText = message;
        textLabel = new JLabel(message);
        super.setLayout(null);

        switch (type) {
            case YOU:
                super.setBackground(Color.BLUE);
                if (messages.size() == 0) {
                    super.setBounds(INITIAL_YOU_MESSAGE_X - (CHARACTER_WIDTH / 2 * messageText.length()) + WEIRD_WIDTH_THING, INITIAL_YOU_MESSAGE_Y, INITIAL_YOU_MESSAGE_WIDTH + (CHARACTER_WIDTH / 2 * messageText.length()) - WEIRD_WIDTH_THING, INITIAL_YOU_MESSAGE_HEIGHT);
                } else {
                    super.setBounds(INITIAL_YOU_MESSAGE_X - (CHARACTER_WIDTH / 2 * messageText.length()) + WEIRD_WIDTH_THING, INITIAL_FOREIGN_MESSAGE_Y /*messages.get(messages.size() - 2).getY() - MESSAGE_Y_SPACING*/, INITIAL_YOU_MESSAGE_WIDTH + (CHARACTER_WIDTH / 2 * messageText.length()) - WEIRD_WIDTH_THING, INITIAL_YOU_MESSAGE_HEIGHT);
                }
                textLabel.setBounds(5, 3, messageText.length() * CHARACTER_WIDTH, CHARACTER_HEIGHT);
                textLabel.setForeground(Color.WHITE);
                super.add(textLabel);
                break;

            case FOREIGN:
                super.setBackground(Color.LIGHT_GRAY);
                if (messages.size() == 0) {
                    super.setBounds(INITIAL_FOREIGN_MESSAGE_X, INITIAL_FOREIGN_MESSAGE_Y, INITIAL_FOREIGN_MESSAGE_WIDTH + (CHARACTER_WIDTH / 2 * messageText.length()) - WEIRD_WIDTH_THING, INITIAL_FOREIGN_MESSAGE_HEIGHT);
                } else {
                    super.setBounds(INITIAL_FOREIGN_MESSAGE_X, INITIAL_FOREIGN_MESSAGE_Y /*messages.get(messages.size() - 2).getY() + MESSAGE_Y_SPACING*/, INITIAL_FOREIGN_MESSAGE_WIDTH + (CHARACTER_WIDTH / 2 * messageText.length()) - WEIRD_WIDTH_THING, INITIAL_FOREIGN_MESSAGE_HEIGHT);
                }
                textLabel.setBounds(5, 3, messageText.length() * CHARACTER_WIDTH, CHARACTER_HEIGHT);
                textLabel.setForeground(Color.BLACK);
                super.add(textLabel);
                break;

            case INFO:
        }
//            System.out.printf("%s | (%d, %d)", getMessageText(), getX(), getY());
//            System.out.println();
        messages.add(this);
        setVisible(true);

    }

    public String getMessageText() {
        return messageText;
    }

}
