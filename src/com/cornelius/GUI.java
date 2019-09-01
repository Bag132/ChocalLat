package com.cornelius;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class GUI {
    private static GUI instance;
    private MainFrame mainFrame;
    private SetupFrame setupFrame;
    private MessageInputField inputField;
    private BackButton backButton;
    private SubmitButton sendButton;
    private boolean mainCreated = false;

    private GUI() {
    }

    public static GUI getInstance() {
        return instance = instance == null ? new GUI() : instance;
    }

    public void createSetupFrame() {
        backButton = new BackButton();
        setupFrame = new SetupFrame();
        setupFrame.setVisible(true);
    }

    public void createMainFrame(boolean host) {
        mainFrame = new MainFrame(host);
        inputField = new MessageInputField();
        sendButton = new SubmitButton();
        mainFrame.addWindowListener(makeMainFrameWindowListener());
        mainFrame.add(inputField);
        mainFrame.add(sendButton);
        mainFrame.setVisible(true);
        mainCreated = true;
    }

    public boolean mainIsCreated() {
        return mainCreated;
    }

    public String getInputFieldText() {
        return inputField == null ? "" : inputField.getText();
    }

    public void setInputFieldText(String text) {
        if (inputField == null) {
            return;
        } else {
            inputField.setText(text);
        }
    }

    public void addYourMessage(String message) {
        if (mainFrame == null) {
            return;
        } else {
            mainFrame.addYourMessage(message);
            mainFrame.repaint();
        }
    }

    public void addForeignMessage(String message) {
        if (mainFrame == null) {
            return;
        } else {
            mainFrame.addForeignMessage(message);
            mainFrame.repaint();
        }
    }

    public void setVisible(boolean visible) {
        mainFrame.setVisible(visible);
    }

    public void repaintMainFrame() {
        mainFrame.repaint();
    }

    public void setupFrameCloseOperation() {

    }

    public void mainFrameCloseOperation() {
        //TODO
    }

    public WindowListener makeMainFrameWindowListener() {
        return new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {

            }

            @Override
            public void windowClosed(WindowEvent e) {
                mainFrameCloseOperation();
            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        };
    }

    public WindowListener makeSetupFrameWindowListener() {
        return new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {

            }

            @Override
            public void windowClosed(WindowEvent e) {
                setupFrameCloseOperation();
            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        };
    }
}
