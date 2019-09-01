package com.cornelius;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client {
    public static final int PORT = 9876;
    private static Socket endPoint;
    public String serverIP;
    public static ArrayList<Packet> roomsFound = new ArrayList<>();

    public Client() {

    }

    public static ArrayList<String> getHosts() {
        return getHosts(false);
    }

    public static ArrayList<String> getHosts(boolean pingSelf) {
        ArrayList<String> hosts = new ArrayList<>();
        ProcessBuilder pb = new ProcessBuilder("arpe.bat");
        pb.redirectErrorStream();
        Process p = null;
        try {
            p = pb.start();
        } catch (IOException io) {
            io.printStackTrace();
            return null;
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String out = "";
        while (true) {
            String l = null;
            try {
                l = br.readLine();
            } catch (IOException ex) {
            }
            if (l == null)
                break;
            out += "\n" + l;
        }

        Pattern pattern =
                Pattern.compile(".*\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b");
        Matcher match = pattern.matcher(out);
        out = "";
        String prev = "", pLoc;

        if (!(match.find())) {
            out = "No IP found";
        } else {
            if (pingSelf) {
                try {
                    hosts.add(InetAddress.getLocalHost().getHostAddress());
                } catch (UnknownHostException uhe) {
                    uhe.printStackTrace();
                }
            }
            pLoc = match.group();
            while (match.find()) {
                pLoc = match.group();
                hosts.add(pLoc);
            }
            try {
                br.close();
            } catch (IOException ex) {
            }
        }

        return hosts;
    }

    public void run() {
        try {
            InetAddress hostAddress = InetAddress.getLocalHost();
            Socket socket;
            ObjectOutputStream objOut = null;
            ObjectInputStream objIn = null;

            for (int i = 0; i < 5; i++) {
                socket = new Socket(hostAddress.getHostName(), PORT);
                objOut = new ObjectOutputStream(socket.getOutputStream());
//                System.out.println("Sending request to the Socket Server");

                if (i == 4) {
                    objOut.writeObject("exit");
                } else {
                    objOut.writeObject("Client sending: " + i);
                }

                objIn = new ObjectInputStream(socket.getInputStream());
                String message = (String) objIn.readObject();
//                System.out.println("Client recieved message: " + message);

                objIn.close();
                objOut.close();
                Thread.sleep(100);
            }
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static RoomOptionButton[] findRooms() {
        ArrayList<Thread> seekers = new ArrayList<>();
        for (String address : getHosts(true)) {
            new Seeker(address).start();
        }
        ArrayList<RoomOptionButton> r = new ArrayList<>();
        for (Packet p : roomsFound) {
            r.add(new RoomOptionButton(p.getPreferredName(), p.getAddress(), Integer.parseInt(p.getPopulation())));
        }
        return (RoomOptionButton[]) r.toArray();
    }

    public void close() {
        //TODO
    }

    public void connectToRoom(String ipAddress) {
        try {
            this.serverIP = ipAddress;
            endPoint = new Socket(ipAddress, PORT);
        } catch (IOException ioe) {
            ioe.printStackTrace();
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

    public String getGreetMessage() {
        String s = null;
        try {
            s = getGreetMessage(InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException uhe) {
            uhe.printStackTrace();
        }
        return s;
    }

    public static String getGreetMessage(String preferredName) {
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


}

// CONTEXT:10.0.0.1:HostName:Preferred Name:Message:Population

