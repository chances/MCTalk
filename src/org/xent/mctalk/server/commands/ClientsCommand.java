/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.xent.mctalk.server.commands;

import java.util.ArrayList;
import org.xent.mctalk.net.MCTalkConnection;
import org.xent.mctalk.net.MCTalkServer;
import org.xent.mctalk.net.Room;

/**
 *
 * @author Chance Snow
 */
public class ClientsCommand extends Command {

    public ClientsCommand() {
        this.setCommand("clients");
        this.setDescription("Lists the connected clients" +
                "\n\t [roomID] Lists the connected clients within a room");
    }

    @Override
    public boolean doCommand(MCTalkServer server, String[] args) {
        ArrayList<MCTalkConnection> clients;
        if (args == null) {
            clients = server.getClients();
            System.out.print("Clients: ");
        } else {
            //validate argument
            boolean found = false;
            for (Room room : server.getRooms())
                if (room.getID().equals(args[0])) {
                    found = true;
                    break;
                }
            if (found) {
                clients = server.getClientsByRoomId(args[0]);
                System.out.print("Clients: ");
            } else {
                System.out.println("A room with the ID of '" +
                        args[0] + "' does not exist.");
                return false;
            }
        }
        if (clients.isEmpty()) {
            System.out.println("[There are no clients]");
        } else {
            for (int i = 0; i < clients.size(); i++) {
                if (i < clients.size() - 1)
                    System.out.print(clients.get(i).getName() + ", ");
                else
                    System.out.println(clients.get(i).getName());
            }
        }
        return false;
    }
}