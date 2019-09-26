package com.cornelius;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

enum SetupFrameState {TYPE_SELECT, ROOM_SELECTION}

public class SetupFrame extends JFrame {
    public static final int SETUP_FRAME_WIDTH = 300, SETUP_FRAME_HEIGHT = 400;
    RoomOptionButton[] rooms;
    RoomOptionButton noRoomRoom = new RoomOptionButton("No rooms found", "", 0);
    SetupFrameState currentState = SetupFrameState.TYPE_SELECT;
    JButton ipSelectButton;
    JButton hostSelectButton;
    JButton refreshButton;
    BackButton backButton;

    public SetupFrame() {
        super("WeLcOmE");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void setVisible(boolean visible) {
        backButton = new BackButton();
        refreshButton = new JButton("Refresh");
        ipSelectButton = new JButton("Find Room");
        hostSelectButton = new JButton("Host Room");
        setLayout(null);
        setSize(SETUP_FRAME_WIDTH, SETUP_FRAME_HEIGHT);
        showHostOrClientSelection();
        super.setVisible(visible);
    }

    public void showHostOrClientSelection() {
        backButton.setLocation(5, 5);
        refreshButton.setBounds(203, 5, 80, 20);
        ipSelectButton.setBounds(85, 100, 100, 40);
        hostSelectButton.setBounds(85, 150, 100, 40);
        ipSelectButton.addMouseListener(makeIPSelectButton());
        backButton.addMouseListener(makeBackButtonListener());
        hostSelectButton.addMouseListener(makeHostSelectButton());
        ipSelectButton.setBackground(Color.LIGHT_GRAY);
        hostSelectButton.setBackground(Color.BLUE);
        hostSelectButton.setForeground(Color.WHITE);
        ipSelectButton.setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.RAISED));
        hostSelectButton.setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.RAISED, new Color(215, 213, 255), Color.BLACK));
        add(ipSelectButton);
        add(hostSelectButton);
        add(backButton);
        add(refreshButton);
        if (rooms != null) {
            for (RoomOptionButton r : rooms) {
                r.setVisible(false);
            }
        }
        backButton.setVisible(false);
        refreshButton.setVisible(false);
        ipSelectButton.setVisible(true);
        hostSelectButton.setVisible(true);
    }

    public void showIPSelection() {
        final int x = 80;
        int y = 20;
        final int width = 120;
        final int height = 40;
        hostSelectButton.setVisible(false);
        ipSelectButton.setVisible(false);
        refreshButton.setVisible(true);
        backButton.setVisible(true);

        rooms = Client.findRooms();
        for (RoomOptionButton r : rooms) {
            r.setBounds(x, y, width, height);
            add(r);
            System.out.println("Beep");
            y += 50;
        }

//        noRoomRoom.setVisible(true);
//        add(noRoomRoom.createButton(80, 20));
//
        repaint();
    }

    public MouseListener makeBackButtonListener() {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (currentState == SetupFrameState.ROOM_SELECTION) {
//                    removeAll();
                    showHostOrClientSelection();
                    currentState = SetupFrameState.TYPE_SELECT;
                    System.out.println(currentState.toString());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                backButton.mouseEntered();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backButton.mouseExited();
            }
        };
    }

    public MouseListener makeIPSelectButton() {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showIPSelection();
                currentState = SetupFrameState.ROOM_SELECTION;
                System.out.println(currentState.toString());
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

    public MouseListener makeHostSelectButton() {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                GUI.getInstance().createMainFrame(true);
                Server.getInstance().hostChatroom();
                setVisible(false);
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

    public MouseListener makeRefreshListener() {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                noRoomRoom.setVisible(false);
                showIPSelection();
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

    class RefreshButton extends JButton {

        public RefreshButton() {
            super();
            setSize(45, 20);
            setText("Refresh");
            setBorder(BorderFactory.createLineBorder(Color.BLACK));
            setBackground(new Color(149, 255, 49));
        }
    }
}
