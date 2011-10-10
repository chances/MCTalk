package org.xent.mctalk.server.commands;

import org.xent.mctalk.net.MCTalkServer;

/**
 *
 * @author Chance Snow
 */
public class StopCommand extends Command {

    public StopCommand() {
        this.setCommand("stop");
        this.setDescription("Stops the server");
    }

    @Override
    public boolean doCommand(MCTalkServer server, String[] args) {
        System.out.println("Stopping server...");
        server.stopServer();
        System.out.println("Server stopped.");
        return true;
    }
}