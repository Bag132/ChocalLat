package com.cornelius;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class RoomOptionButton extends JPanel {
    private JLabel hostNameLabel;
    private JLabel addressLabel;
    private JLabel populationLabel;
    private String name;
    private String address;
    private int population;
    //    private Color normalColor = new Color(66, 105, 223);
    private Color normalColor = Color.BLUE;
    private Color mouseEnteredColor = new Color(190, 202, 255, 255);
    private Color mousePressedColor = new Color(0, 12, 214);

    RoomOptionButton(String name, String address, int population) {
        super();
        this.name = name;
        this.address = address;
        this.population = population;
        hostNameLabel = new JLabel(name);
        addressLabel = new JLabel(address);
        populationLabel = new JLabel("Pop: " + population);
        hostNameLabel.setBounds(5, 0, 100, 18);
        addressLabel.setBounds(5, 20, 70, 25);
        populationLabel.setBounds(75, 20, 60, 25);
        hostNameLabel.setForeground(Color.WHITE);
        addressLabel.setForeground(Color.WHITE);
        populationLabel.setForeground(Color.WHITE);
        add(hostNameLabel);
        add(addressLabel);
        add(populationLabel);
        setSize(120, 40);
        setLayout(null);
        setBorder(BorderFactory.createSoftBevelBorder(0));
        setBackground(normalColor);

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setBackground(mouseEnteredColor);
                Client.setSelectedServer(address);
                GUI.getInstance().createMainFrame(false);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(mousePressedColor);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(normalColor);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(mouseEnteredColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(normalColor);
            }
        });
    }

    public String getRoomName() {
        return name;
    }

    public String getRoomAddress() {
        return address;
    }

    public int getPopulation() {
        return population;
    }

    public RoomOptionButton createButton(int x, int y) {
        setBounds(x, y, getWidth(), getHeight());
        return this;
    }
}
