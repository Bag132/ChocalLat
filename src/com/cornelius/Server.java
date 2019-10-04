package com.cornelius;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Server implements Closeable {
    private static final int PORT = 9876;
    public static ArrayList<Socket> connectedClients = new ArrayList<>();
    public static String preferredName = "PreferredName";
    private static ServerSocket serverSock;
    private static Server instance;
    private Listener listener;

    private Server() {
        try {
            serverSock = new ServerSocket(PORT);
        } catch (IOException ignored) {
            ignored.printStackTrace();
        }
    }

    public static Server getInstance() {
        return instance = instance == null ? new Server() : instance;
    }

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

    public void stopListening() {
        listener.interrupt();
    }

    public void hostChatroom() {
        listener = new Listener();
        listener.start();
    }

    public static String getAcceptMessage() {
        String ip = null;
        String hostname = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException uhe) {
            uhe.printStackTrace();
        }

        return "ACCEPT:" + ip + ":" + hostname + ":" + preferredName + ":" + "accepting you" + ":" + "0";
    }

    public void close() {

    }

    static void writeToAllConnected(String message) {
        for (Socket s : connectedClients) {
            try {
                Socket client = new Socket(s.getInetAddress().getHostAddress(), PORT);
                DataOutputStream dataOut = new DataOutputStream(client.getOutputStream());
                dataOut.writeUTF(message);
                dataOut.close();
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class Listener extends Thread {

        public void run() {
            try {
                while (!isInterrupted()) {
                    System.out.println("Waiting for clients to connect");
                    Socket received = serverSock.accept();
                    System.out.println("Recieved");
                    DataInputStream socketReader = new DataInputStream(received.getInputStream());
                    DataOutputStream socketPrinter = new DataOutputStream(received.getOutputStream());

                    Server.connectedClients.add(received);
                    String messageRecieved = socketReader.readUTF();
                    System.out.println("Recieved from client: " + messageRecieved);
                    socketPrinter.writeUTF(Server.getAcceptMessage());
                    GUI.getInstance().addForeignMessage(messageRecieved);

                    received.close();
                    socketPrinter.close();
                    socketReader.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    class ChatroomHostThread extends Thread {

        @Override
        public void run() {

            while (!isInterrupted()) {

            }
        }
    }

}
