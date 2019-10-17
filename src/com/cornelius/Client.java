package com.cornelius;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client {
    static final int PORT = 9876;
    private static Client instance;
    ArrayList<Packet> roomsFound = new ArrayList<>();
    private volatile ArrayList<String> queuedMessages = new ArrayList<>();
    private String serverIP;

    private Client() {

    }

    public static Client getInstance() {
        return instance = instance == null ? new Client() : instance;
    }

    RoomOptionButton[] findRooms() {
        ArrayList<Seeker> seekers = new ArrayList<>();
        for (String address : Objects.requireNonNull(getHosts(true))) {
            Seeker s = new Seeker(address);
            s.start();
            seekers.add(s);
        }

        ArrayList<RoomOptionButton> roomButtons = new ArrayList<>();
        for (Seeker hideN : seekers) {
            try {
                hideN.join();
                if (hideN.foundRoom()) {
                    addRoomToList(hideN.getFoundServer());
                    roomButtons.add(new RoomOptionButton(hideN.getFoundServer().getPreferredName(),
                            hideN.getFoundServer().getAddress(),
                            Integer.parseInt(hideN.getFoundServer().getPopulation())));
                }
            } catch (InterruptedException | NullPointerException ie) {
                ie.printStackTrace();
            }
        }
        RoomOptionButton[] buttonArray = new RoomOptionButton[roomButtons.size()];

        for (int i = 0; i < buttonArray.length; i++) {
            buttonArray[i] = roomButtons.get(i);
        }

        return buttonArray;
    }

    void joinServer() {
        ListenForMessages listener = new ListenForMessages();
        listener.start();
    }

    void writeToServer(final Socket sock, final String message) {
        try {
            System.out.println("Writing " + message + " to " + serverIP);
            DataOutputStream dataOut = new DataOutputStream(s.getOutputStream());
            dataOut.writeUTF(message);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    void setSelectedServer(final String address) {
        serverIP = address;
    }


    static String getGreetMessage(String preferredName) {
        String ip = null;
        String hostname = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException uhe) {
            uhe.printStackTrace();
        }
        return "GREET:" + ip + ":" + hostname + ":" + preferredName + ":joining";
    }

    public ArrayList<String> getHosts() {
        return getHosts(false);
    }

    private ArrayList<String> getHosts(boolean pingSelf) {
        System.out.println("Called getHosts");
        ArrayList<String> hosts = new ArrayList<>();
        ProcessBuilder pb = new ProcessBuilder("arpe.bat");
        Process p;
        try {
            p = pb.start();
        } catch (IOException io) {
            io.printStackTrace();
            return null;
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        StringBuilder out = new StringBuilder();
        while (true) {
            String l = null;
            try {
                l = br.readLine();
            } catch (IOException ignored) {
            }
            if (l == null)
                break;
            out.append("\n").append(l);
        }

        Pattern pattern =
                Pattern.compile(".*\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b");
        Matcher match = pattern.matcher(out.toString());
        out = new StringBuilder();
        String pLoc;

        if (!(match.find())) {
            out = new StringBuilder("No IP found");
        } else {
            if (pingSelf) {
                System.out.println("Adding localhost");
                try {
                    hosts.add(InetAddress.getLocalHost().getHostAddress());
                } catch (UnknownHostException uhe) {
                    uhe.printStackTrace();
                }
            }
            while (match.find()) {
                pLoc = match.group();
                hosts.add(pLoc);
            }
            try {
                br.close();
            } catch (IOException ignored) {
            }
        }
        return hosts;
    }

    private void addRoomToList(Packet p) {
        for (Packet pack : roomsFound) {
            if (pack.equals(p)) {
                return;
            }
        }
        roomsFound.add(p);
    }

    public static String getGreetMessage() {
        String s = null;
        try {
            s = getGreetMessage(InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException uhe) {
            uhe.printStackTrace();
        }
        return s;
    }

    // Example purposes
    private void run() {
        try {
            InetAddress hostAddress = InetAddress.getLocalHost();
            Socket socket;
            ObjectOutputStream objOut;
            ObjectInputStream objIn;

            for (int i = 0; i < 5; i++) {
                socket = new Socket(hostAddress.getHostName(), PORT);
                objOut = new ObjectOutputStream(socket.getOutputStream());

                if (i == 4) {
                    objOut.writeObject("exit");
                } else {
                    objOut.writeObject("Client sending: " + i);
                }

                objIn = new ObjectInputStream(socket.getInputStream());
                String message = (String) objIn.readObject();

                objIn.close();
                objOut.close();
                Thread.sleep(100);
            }
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getChatMessage(String message) {
        String ip = null;
        String hostname = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException uhe) {
            uhe.printStackTrace();
        }

        return "MESSAGE:" + ip + ":" + hostname + ":" + "I was never given a name" + ":" + message;
    }

    public String getCloseMessage() {
        String ip = null;
        String hostname = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException uhe) {
            uhe.printStackTrace();
        }
        return "CLOSE:" + ip + ":" + hostname + ":left:leaving";
    }


    // TODO Copy this class into the main project

    private class ListenForMessages extends Thread implements Runnable {
        private final ArrayList<String> queuedMessages = new ArrayList<>();

        @Override
        public void run() {
            while (!isInterrupted()) {
                try {
                    final Socket sock = new Socket(serverIP, PORT);
                    final DataInputStream dataIn = new DataInputStream(sock.getInputStream());
                    final DataOutputStream dataOut = new DataOutputStream(sock.getOutputStream());

                    final String received = dataIn.readUTF();
                    System.out.println(received);
                    if (!received.equals("")) {
                        GUI.getInstance().addForeignMessage(received);
                    }

                    queuedMessages.forEach(Client.getInstance()::writeToServer);
                    queuedMessages.forEach(queuedMessages::remove);

                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName() + " | No messages");
                }
            }
        }

        private synchronized void queueMessage(final String message) {
            queuedMessages.add(message);
        }

    }
}

// CONTEXT:10.0.0.1:HostName:Preferred Name:Message:Population

