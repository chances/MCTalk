package org.xent.mctalk.net;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xent.mctalk.net.MCTalkProtocol.*;

/**
 *
 * @author Chance Snow
 */
public class MCTalkServer extends Thread {

    private Server server;
    private ServerProperties config;
    private ArrayList<Room> rooms;

    public MCTalkServer() {
        loadConfig();
        //Create rooms from config file
        rooms = new ArrayList<Room>();
        for (String roomID: config.getRooms()) {
            Room room = new Room(roomID);
            room.setMaxClients(config.getMaxClients());
            rooms.add(room);
        }
        //Construct server
        constructServer();
    }

    private void loadConfig() {
        config = new ServerProperties();
        config.load();
    }

    private void constructServer() {
        server = new com.esotericsoftware.kryonet.Server() {
            @Override
            protected Connection newConnection() {
                return new MCTalkConnection();
            }
        };

        MCTalkProtocol.register(server);

        server.addListener(new Listener() {
            @Override
            public void received (Connection c, Object data) {
                MCTalkConnection connection = (MCTalkConnection)c;

                if (data instanceof GetRooms) {
                    Rooms resp = new Rooms();
                    resp.roomIDs = config.getRooms();
                    server.sendToTCP(c.getID(), resp);
                }

                if (data instanceof GetRoomInfo) {
                    GetRoomInfo in = (GetRoomInfo)data;
                    RoomInfo resp = new RoomInfo();
                    resp.roomID = null;
                    resp.numClients = 0;
                    resp.maxClients = 0;
                    for (Room room: rooms) {
                        if (room.getID().equalsIgnoreCase(in.roomID)) {
                            resp.roomID = room.getID();
                            int clients = room.getListeners().size();
                            clients += room.getBroadcasters().size();
                            resp.numClients = clients;
                            resp.maxClients = room.getMaxClients();
                            break;
                        }
                    }
                    server.sendToTCP(c.getID(), resp);
                }

                if (data instanceof JoinAsListener) {
                    JoinAsListener in = (JoinAsListener)data;
                    //validate params
                    if (!isValidRoomID(in.roomID))
                        return;
                    if (!isValidName(in.name))
                        return;
                    //add listener
                    for (Room room: rooms) {
                        if (room.getID().equals(in.roomID)) {
                            connection.setName(in.name);
                            connection.setRoomID(in.roomID);
                            room.getListeners().add(connection.getID());
                            updateNames(room.getID());
                            break;
                        }
                    }
                }

                if (data instanceof JoinAsBroadcaster) {
                    JoinAsBroadcaster in = (JoinAsBroadcaster)data;
                    //validate params
                    if (!isValidRoomID(in.roomID))
                        return;
                    if (!isValidName(in.name))
                        return;
                    //add broadcaster
                    for (Room room: rooms) {
                        if (room.getID().equals(in.roomID)) {
                            connection.setName(in.name);
                            connection.setRoomID(in.roomID);
                            room.getBroadcasters().add(connection.getID());
                            updateNames(room.getID());
                            break;
                        }
                    }
                }

                if (data instanceof StreamAudio) {
                    StreamAudio in = (StreamAudio)data;
                    //validate roomID
                    if (!isValidRoomID(in.roomID))
                        return;
                    if (getRoomById(in.roomID) != null)
                        return;
                    //create AudioStream
                    AudioStream stream = new AudioStream();
                    stream.data = in.data;
                    //stream audio to room's clients
                    for (Connection conn : server.getConnections()) {
                        MCTalkConnection connOut = (MCTalkConnection)conn;
                        if (connOut.getRoomID().equals(in.roomID))
                            connOut.sendTCP(stream);
                    }
                }
            }

            @Override
            public void disconnected(Connection c) {
                MCTalkConnection connection = (MCTalkConnection)c;
                if (isValidName(connection.getName())) {
                    Disconnected discon = new Disconnected();
                    discon.name = connection.getName();
                    //Tell the other clients in the disconnected client's room
                    //  that the disconnected client had disconnected.
                    for (Connection conn : server.getConnections()) {
                        MCTalkConnection disconOut = (MCTalkConnection)conn;
                        if (disconOut.getRoomID().equals(connection.getRoomID()))
                            disconOut.sendTCP(discon);
                    }
                }
            }
        });
    }

    private boolean isValidRoomID(String id) {
        for (Room room: rooms) {
            if (room.getID().equals(id))
                return true;
        }
        return false;
    }

    private boolean isValidName(String name) {
        if (name == null)
            return false;
        name = name.trim();
        if (name.length() == 0)
            return false;
        if (name.contains(" "))
            return false;

        return true;
    }

    private Room getRoomById(String roomId) {
        for (Room room : rooms) {
            if (room.getID().equals(roomId))
                return room;
        }
        return null;
    }

    private void updateNames(String roomId) {
        UpdateNames data = new UpdateNames();
        ArrayList<String> names = new ArrayList<String>();
        for (Connection conn : server.getConnections()) {
            MCTalkConnection connection = (MCTalkConnection)conn;
            if (connection.getRoomID().equals(roomId))
                names.add(connection.getName());
        }
        data.names = (String[]) names.toArray();
        names.clear();
        names = null;
        for (Connection conn : server.getConnections()) {
            MCTalkConnection connection = (MCTalkConnection)conn;
            if (connection.getRoomID().equals(roomId))
                connection.sendTCP(data);
        }
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public ArrayList<MCTalkConnection> getClients() {
        ArrayList<MCTalkConnection> clients = new ArrayList<MCTalkConnection>();
        for (Connection conn : server.getConnections())
            clients.add((MCTalkConnection)conn);
        return clients;
    }

    public ArrayList<MCTalkConnection> getClientsByRoomId(String roomId) {
        ArrayList<MCTalkConnection> clients = new ArrayList<MCTalkConnection>();
        for (Connection conn : server.getConnections()) {
            MCTalkConnection connection = (MCTalkConnection)conn;
            if (connection.getRoomID().equals(roomId))
                clients.add((MCTalkConnection)conn);
        }
        return clients;
    }

    @Override
    public void start() {
        try {
            server.bind(config.getPort());
        } catch (IOException ex) {
            Logger.getLogger(MCTalkServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        server.start();
    }

    public void stopServer() {
        server.stop();
    }
}