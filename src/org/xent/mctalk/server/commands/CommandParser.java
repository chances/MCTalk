package org.xent.mctalk.server.commands;

import java.util.ArrayList;
import org.xent.mctalk.net.MCTalkServer;

/**
 *
 * @author Chance Snow
 */
public class CommandParser {

    private MCTalkServer server;
    private ArrayList<Command> commands;
    private boolean running = true;

    public CommandParser(MCTalkServer server) {
        this.server = server;
        this.commands = new ArrayList<Command>();
        this.commands.add(new ClientsCommand());
        this.commands.add(new ListCommand());
        this.commands.add(new StopCommand());
    }

    public void parseCommand(String commandLine) {
        String[] pieces = commandLine.split(" ");
        String commandName = pieces[0];
        //get arguments
        String[] args = null;
        if (pieces.length > 1) {
            args = new String[pieces.length - 1];
            for (int i = 1; i < pieces.length; i++)
                args[i-1] = pieces[i];
        }
        //is it the help command?
        if (commandName.equalsIgnoreCase("help")) {
            System.out.println("-------------------------");
            System.out.println("Command List:");
            for (Command command : commands) {
                System.out.print(command.getCommand());
                System.out.println(" - " + command.getDescription());
            }
            System.out.println("-------------------------");
        } else {
            //is it a real command?
            if (commandExists(commandName)) {
                //do the command
                for (Command command : commands) {
                    if (command.getCommand().equals(commandName)) {
                        if (command.doCommand(server, args))
                            running = false;
                        break;
                    }
                }
            } else { //unknown command
                System.out.println("Unknown command: '" + commandName);
            }
        }
    }

    private boolean commandExists(String commandName) {
        for (Command command : commands) {
            if (command.getCommand().equals(commandName))
                return true;
        }
        return false;
    }

    public boolean isRunning() {
        return running;
    }
}