package com.cornelius;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Seeker extends Thread {
    private final String ip;
    private String name;
    private Socket sock = null;
    private int timesToTry;
    private boolean foundRoom = false;
    private Packet foundServer;

    public Seeker(String ip) {
        this(ip, "Jeff");
    }

    public Seeker(String ip, String name) {
        this(ip, 5, name);
    }

    public Seeker(final String ip, final int timesToTry, final String name) {
        this.ip = ip;
        this.timesToTry = timesToTry;

    }

    public static String byteArrayToString(byte[] input) {
        String output = "";
        for (byte b : input) {
            output.concat(Byte.toString(b));
        }
        return output;
    }

    @Override
    public void run() {
        try {
            try {
                sock = new Socket(ip, Client.PORT);
            } catch (UnknownHostException uhe) {
                System.out.println(ip + " is not a host");
                return;
            }
            System.out.println(Main.ANSI_BLUE + "FOUND HOST AT " + ip + Main.ANSI_RESET);
            String message;
            int i = 0;

            do {
                DataOutputStream out = new DataOutputStream(sock.getOutputStream());
                out.writeUTF(Client.getGreetMessage("Jeffrey"));
                System.out.println(Client.getGreetMessage("Jeffrey"));

                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                    message = in.readLine();
                    System.out.println(message);
                    in.close();
                } catch (Exception e) {
                    System.out.println("Couldn't readObject()");
                    e.printStackTrace();
                    message = "nope";
                }

                out.close();
                foundRoom = message.contains("ACCEPT:");
                if (foundRoom) {
                    if (alreadyFound(new Packet(message))) {
                        foundRoom = false;

                    } else {
                        foundServer = new Packet(message);
                        this.close();
                        return;
                    }
                }
                i++;
            } while (i <= timesToTry);
            this.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean alreadyFound(Packet p) {
        for (Packet pack : Client.roomsFound) {
            if (p.equals(pack)) {
                return true;
            }
        }
        return false;
    }

    public boolean foundRoom() {
        return foundRoom;
    }

    public Packet getFoundServer() {
        return foundServer;
    }

    public void close() {
        try {
            sock.close();
        } catch (IOException ignored) {

        }
    }
}

