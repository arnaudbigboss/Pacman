package fr.dauphine.ar.application;

import java.io.IOException;
import java.net.Socket;
import java.util.Properties;

import fr.dauphine.ar.network.ClientHandleConnection;
import fr.dauphine.ar.properties.PropertiesLoader;
import org.apache.commons.cli.*;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class ClientMain {
	private static final Logger LOGGER = Logger.getLogger(ClientMain.class);

	public static void main(String[] args) {
		BasicConfigurator.configure();

		Properties config = PropertiesLoader.get("config.properties");
		String server = config.get("server").toString();
		int port = Integer.parseInt(config.get("port").toString());

		Options options = new Options();
		CommandLineParser parser = new DefaultParser();

		options.addOption("s", "server", true, "Server address");
		options.addOption("p", "port", true, "Server port");

		try {
			CommandLine cmd = parser.parse(options, args);

			if(cmd.hasOption("s")){
				server = cmd.getOptionValue("s");
			}
			if(cmd.hasOption("p")) {
				port = Integer.parseInt(cmd.getOptionValue("p"));
			}

		} catch (ParseException e) {
			LOGGER.error("Invalid command line format.");
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "Pacman", options );
			System.exit(1);
		}

		try {
			Socket s = new Socket(server, port);
			ClientHandleConnection handler = new ClientHandleConnection(s);
		} catch (IOException e) {
			LOGGER.error("Could not connect");
			System.exit(1);
		}
	}
}
