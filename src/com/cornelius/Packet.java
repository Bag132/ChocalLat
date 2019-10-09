package com.cornelius;

public class Packet {
    private String[] packetContents;
    private String fullMessage;

    Packet(String packet) {
        this.fullMessage = packet;
        packetContents = packet.split(":");
    }

    public String getContext() {
        return packetContents[0];
    }

    public String getAddress() {
        return packetContents[1];
    }

    public String getHostName() {
        return packetContents[2];
    }

    public String getPreferredName() {
        return packetContents[3];
    }

    public String getChatMessage() {
        return packetContents[4];
    }

    public String getPopulation() {
        return packetContents[5];
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Packet && equals((Packet) obj);
    }

    public boolean equals(Packet packet) {
        return this.getContext().equals(packet.getContext()) &&
                this.getAddress().equals(packet.getHostName()) &&
                this.getHostName().equals(packet.getHostName());
    }
}
