package com.cornelius;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.InputMismatchException;

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
        listener = new Listener();
        listener.start();
    }

    public void stopListening() {
        listener.interrupt();
    }

    public void hostChatroom() {

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

    class Listener extends Thread {

        public void run() {
            try {
                Socket received;

                while (!isInterrupted()) {
                    received = serverSock.accept();
                    BufferedReader socketReader = new BufferedReader(new InputStreamReader(received.getInputStream()));
                    PrintWriter socketPrinter = new PrintWriter(received.getOutputStream());
                    Server.connectedClients.add(received);
                    System.out.println(socketReader.readLine());
                    socketPrinter.println(Server.getAcceptMessage());
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

}
