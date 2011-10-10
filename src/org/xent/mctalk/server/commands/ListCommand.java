package org.xent.mctalk.server.commands;

import java.util.ArrayList;
import org.xent.mctalk.net.MCTalkServer;
import org.xent.mctalk.net.Room;

/**
 *
 * @author Chance Snow
 */
public class ListCommand extends Command {

    public ListCommand() {
        this.setCommand("list");
        this.setDescription("Lists the available chat rooms");
    }

    @Override
    public boolean doCommand(MCTalkServer server, String[] args) {
        ArrayList<Room> rooms = server.getRooms();
        System.out.print("Rooms: ");
        for (int i = 0; i < rooms.size(); i++) {
            if (i < rooms.size() - 1)
                System.out.print(rooms.get(i).getID() + ", ");
            else
                System.out.println(rooms.get(i).getID());
        }
        return false;
    }
}