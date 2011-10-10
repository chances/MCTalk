package org.xent.mctalk.server;

import java.util.Scanner;
import com.esotericsoftware.minlog.Log;
import org.xent.mctalk.audio.Compressor;
import org.xent.mctalk.server.commands.CommandParser;
import org.xent.mctalk.net.MCTalkServer;

/**
 *
 * @author Chance Snow
 */
public class Server {

    public static void main(String[] args) {
        Log.set(Log.LEVEL_ERROR);
        MCTalkServer server = new MCTalkServer();
        System.out.println("MCTalk Server - v0.1b");
        System.out.println("-------------------------");
        System.out.println("Type 'help' for a list of commands");
        System.out.println("-------------------------");
        System.out.println("Starting server...");
        server.start();
        System.out.println("Server started.");
        Scanner sc = new Scanner(System.in);
        //Command parsing loop
        CommandParser commandParser = new CommandParser(server);
        while (commandParser.isRunning()) {
            //System.out.print("> ");
            String command = sc.nextLine();
            commandParser.parseCommand(command);
        }
        System.exit(0);
    }
}