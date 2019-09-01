package com.cornelius;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MessageInputField extends JTextField {
    public static final int INPUT_FIELD_X = 2, INPUT_FIELD_Y = 535, INPUT_FIELD_WIDTH = 330, INPUT_FIELD_HEIGHT = 25;
    public static final int ENTER_KEY_CODE = 10;

    public MessageInputField() {
        super();
        super.setBounds(INPUT_FIELD_X, INPUT_FIELD_Y, INPUT_FIELD_WIDTH, INPUT_FIELD_HEIGHT);
        super.setBorder(BorderFactory.createEtchedBorder());
        super.setText("Enter Message Here");
        super.addKeyListener(makeKeyListener());
        super.setVisible(true);
    }

    public KeyListener makeKeyListener() {
        return new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getExtendedKeyCode() == ENTER_KEY_CODE) {
                    enterMessage();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        };
    }

    public void enterMessage() {
        GUI.getInstance().addYourMessage(super.getText());
        super.setText("");
    }
}
