package org.xent.mctalk.net;

import java.util.ArrayList;

/**
 *
 * @author Chance Snow
 */
public class Room {
    private String id;
    private ArrayList<Integer> listeners;
    private ArrayList<Integer> broadcasters;
    private int maxClients;
    private ArrayList<byte[]> audioStreams;

    public Room(String id) {
        this.id = id;
        this.listeners = new ArrayList<Integer>();
        this.broadcasters = new ArrayList<Integer>();
        this.maxClients = Integer.MAX_VALUE;
        this.audioStreams = new ArrayList<byte[]>();
    }

    public String getID() {
        return id;
    }

    public ArrayList<Integer> getClients() {
        ArrayList<Integer> clients = listeners;
        clients.addAll(broadcasters);
        return clients;
    }

    public ArrayList<Integer> getBroadcasters() {
        return broadcasters;
    }

    public ArrayList<Integer> getListeners() {
        return listeners;
    }

    public int getMaxClients() {
        return maxClients;
    }

    public void setMaxClients(int maxClients) {
        this.maxClients = maxClients;
    }

    public ArrayList<byte[]> getAudioStreams() {
        return audioStreams;
    }
}