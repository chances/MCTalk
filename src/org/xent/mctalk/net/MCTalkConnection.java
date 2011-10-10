/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.xent.mctalk.net;

import com.esotericsoftware.kryonet.Connection;

/**
 *
 * @author Chance Snow
 */
public class MCTalkConnection extends Connection {
    private String name;
    private String roomID;

    public MCTalkConnection() {
        name = null;
        roomID = null;
    }

    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

}