package com.cornelius;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Server implements Closeable {
    private static final int PORT = 9876;
    public static ArrayList<Socket> connectedClients = new ArrayList<>();
    private static ServerSocket serverSock;
    public static String preferredName = "PreferredName";

    public void run() {
        try {
            serverSock = new ServerSocket(PORT);
            Socket packetSocket = serverSock.accept();

            ObjectInputStream objIn = new ObjectInputStream(packetSocket.getInputStream());
            String message = (String) objIn.readObject();

            ObjectOutputStream objOut = new ObjectOutputStream(packetSocket.getOutputStream());
            objOut.writeObject("Hello Client " + packetSocket.getInetAddress().getHostAddress());

            objIn.close();
            objOut.close();
            packetSocket.close();

            if (message.equalsIgnoreCase("exit")) {

            }

        } catch (NullPointerException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void listenForConnections() {

    }

    public void hostChatroom() {

    }

    public String getAcceptMessage() {
        String ip = null;
        String hostname = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException uhe){
            uhe.printStackTrace();
        }

        return "ACCEPT:" + ip + ":" + hostname + ":" + preferredName + ":" + "accepting you" + ":" + "0";
    }

    public void close() {

    }

    class Listener extends Thread {
        ServerSocket serverSock = null;

        public void run() {
            try {
                Socket received;
                serverSock = new ServerSocket(PORT);

                while (!isInterrupted()) {
                    received = serverSock.accept();

                    ObjectInputStream objIn = new ObjectInputStream(received.getInputStream());

                    ObjectOutputStream objOut = new ObjectOutputStream(received.getOutputStream());


                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

}
