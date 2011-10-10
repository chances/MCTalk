package org.xent.mctalk.server.commands;

import org.xent.mctalk.net.MCTalkServer;

/**
 *
 * @author Chance Snow
 */
public abstract class Command {

    private String command;
    private String description;

    /**
     * Process the command.
     * @param server The MCTalkServer to command
     * @param args Arguments passed to the command
     * @return Whether or not to stop running the command parser
     */
    public boolean doCommand(MCTalkServer server, String[] args) {
        return false;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}