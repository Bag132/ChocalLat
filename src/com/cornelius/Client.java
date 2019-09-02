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
    public static final int PORT = 9876;
    public static ArrayList<Packet> roomsFound = new ArrayList<>();
    public static String serverIP;
    private static Socket selectedServer;
    private static BufferedReader selectedServerIn;
    private static DataOutputStream selectedServerOut;

    public Client() {

    }

    public static RoomOptionButton[] findRooms() {
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
            } catch (InterruptedException ie) {
                ie.printStackTrace();
                continue;
            } catch (NullPointerException npe) {
                npe.printStackTrace();
                continue;
            }
        }
        RoomOptionButton[] buttonArray = new RoomOptionButton[roomButtons.size()];

        for (int i = 0; i < buttonArray.length; i++) {
            buttonArray[i] = roomButtons.get(i);
        }

        return buttonArray;
    }

    public static void writeToServer(String message) {
        try {
            System.out.println("Writing " + message + " to server");
            selectedServerOut.writeUTF(message);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static String getServerMessage() {
        try {
            return selectedServerIn.readLine();
        } catch (IOException io) {
            io.printStackTrace();
            return "null";
        }
    }

    public static void setSelectedServer(Socket sock) {
        selectedServer = sock;
    }

    public static void setSelectedServer(String address) {
        try {
            selectedServer = new Socket(address, PORT);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
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

    public static ArrayList<String> getHosts() {
        return getHosts(false);
    }

    public static ArrayList<String> getHosts(boolean pingSelf) {
        System.out.println("Called getHosts");
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
                System.out.println("Adding localhost");
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
        for (String h : hosts){
            System.out.println("\"" + h + "\"");
        }
        return hosts;
    }

    public static void addRoomToList(Packet p) {
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

    public void close() {
        //TODO
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


}

// CONTEXT:10.0.0.1:HostName:Preferred Name:Message:Population

