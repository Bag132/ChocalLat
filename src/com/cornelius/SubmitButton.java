package com.cornelius;

import javax.swing.*;
import javax.swing.border.SoftBevelBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SubmitButton extends JButton {
    public static final int SEND_BUTTON_X = 333, SEND_BUTTON_Y = 535, SEND_BUTTON_WIDTH = 50, SEND_BUTTON_HEIGHT = 25;

    public SubmitButton() {
        super();
        super.setBounds(SEND_BUTTON_X, SEND_BUTTON_Y, SEND_BUTTON_WIDTH, SEND_BUTTON_HEIGHT);
        super.setBackground(new Color(7, 0, 148));
        super.setLayout(null);
        super.setBorder(BorderFactory.createSoftBevelBorder(SoftBevelBorder.RAISED));
        super.setForeground(Color.WHITE);
        super.setText("Send");
        super.addMouseListener(makeMouseListener());
        super.setVisible(true);
    }

    public void enterMessage() {
        GUI.getInstance().addYourMessage(GUI.getInstance().getInputFieldText());
        GUI.getInstance().setInputFieldText("");
    }

    public MouseListener makeMouseListener() {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                enterMessage();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };
    }
}
