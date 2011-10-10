package org.xent.mctalk.net;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

/**
 *
 * @author Chance Snow
 */
public class MCTalkProtocol {

    public static void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(GetRooms.class);
        kryo.register(String[].class);
        kryo.register(Rooms.class);
        kryo.register(GetRoomInfo.class);
        kryo.register(RoomInfo.class);
        kryo.register(JoinAsListener.class);
        kryo.register(JoinAsBroadcaster.class);
        kryo.register(UpdateNames.class);
        kryo.register(byte[].class);
        kryo.register(StreamAudio.class);
        kryo.register(AudioStream.class);
        kryo.register(Disconnected.class);
    }

    public static class GetRooms {
        //No data sent
    }

    public static class Rooms {
        public String[] roomIDs;
    }

    public static class GetRoomInfo {
        public String roomID;
    }

    public static class RoomInfo {
        public String roomID;
        public int numClients;
        public int maxClients;
    }

    public static class JoinAsListener {
        public String roomID;
        public String name;
    }

    public static class JoinAsBroadcaster {
        public String roomID;
        public String name;
    }

    public static class UpdateNames {
        public String[] names;
    }

    public static class StreamAudio {
        public String roomID;
        public byte[] data;
    }

    public static class AudioStream {
        public byte[] data;
    }

    public static class Disconnected {
        public String name;
    }
}