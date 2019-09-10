package com.cornelius;

import javax.swing.*;
import java.awt.*;

public class BackButton extends JButton {

    public BackButton() {
        setSize(37, 20);
        setText("Back");
        setBorder(BorderFactory.createEmptyBorder());
        setBackground(new Color(255, 255, 255, 0));
        setForeground(new Color(0, 22, 183, 37));
    }

    public void mouseEntered() {
        setForeground(Color.BLUE);
    }

    public void mouseExited() {
        setForeground(new Color(0, 22, 183, 171));
    }

}
