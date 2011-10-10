package org.xent.mctalk.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Chance Snow
 */
public class ServerProperties {
    private final int PORT = 6285; //Digits for: MCTK
    private Properties props;

    public ServerProperties() {
        // N/A
    }

    public Properties getProperties() {
        return props;
    }

    public int getPort() {
        try {
            int port = Integer.parseInt(props
                    .getProperty("server-port", "6285"));
            return port;
        } catch (Exception ex) {
            return PORT;
        }
    }

    public int getMaxClients() {
        try {
            String maxClients = props.getProperty("max-clients", "max");
            if (maxClients.equalsIgnoreCase("max")) {
                return Integer.MAX_VALUE;
            }
            return Integer.parseInt(maxClients);
        } catch (Exception ex) {
            return Integer.MAX_VALUE;
        }
    }

    public String[] getRooms() {
        String[] rooms = {"home"};
        try {
            String roomsProp = props.getProperty("rooms", "home");
            return roomsProp.split(",");
        } catch (Exception ex) {
            return rooms;
        }
    }

    public void load() {
        props = new Properties();
        try {
            props.load(new FileInputStream(new File("server.properties")));
        } catch (IOException ex) {
            //File doesn't exist, create it
            props = new DefaultProperties();
            try {
                props.store(new FileOutputStream(new File("server.properties")),
                            "MCTalk server properties");
            } catch (IOException ex1) {
                Logger.getLogger(ServerProperties.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    public void save() {
        if (props != null) {
            try {
                props.store(new FileOutputStream(new File("server.properties")),
                            "MCTalk server properties");
            } catch (IOException ex) {
                Logger.getLogger(ServerProperties.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private class DefaultProperties extends Properties {

        public DefaultProperties() {
            super();
            this.setProperty("server-port", "6285");
            this.setProperty("max-clients", "max");
            this.setProperty("rooms", "main");
        }
    }
}