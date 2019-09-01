package com.cornelius;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Seeker extends Thread {
    private final String ip;
    private Socket sock = null;
    private int timesToTry;
    private ArrayList<Packet> roomsFound;

    public Seeker(String ip) {
        this(ip, 5);
    }

    public Seeker(final String ip, int timesToTry) {
        this.ip = ip;
        this.timesToTry = timesToTry;
    }

    @Override
    public void run() {
        boolean foundRoom = false;
        try {
            sock = new Socket(ip, Client.PORT);
            ObjectInputStream objIn = new ObjectInputStream(sock.getInputStream());
            ObjectOutputStream objOut = new ObjectOutputStream(sock.getOutputStream());
            String message;
            boolean found;
            int i = 0;

            do {
                objIn = new ObjectInputStream(sock.getInputStream());
                objOut.writeObject(Client.getGreetMessage("Jeff"));
                message = (String) objIn.readObject();
                found = message.contains("ACCEPT:");

                if (found) {
                    roomsFound.add(new Packet(message));
                    this.close();
                    return;

                }
            } while (i <= timesToTry);
            this.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean alreadyFound(Packet p) {
        for (Packet pack : Client.roomsFound) {
            if (p.equals(pack));
        }
        return false;
    }

    public void close() {
        interrupt();
        try {
            sock.close();
        } catch (IOException ignored) {

        }
    }
}

