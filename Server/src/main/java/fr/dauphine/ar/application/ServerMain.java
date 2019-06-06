package fr.dauphine.ar.application;

import fr.dauphine.ar.network.ServerCore;
import fr.dauphine.ar.properties.PropertiesLoader;
import org.apache.commons.cli.*;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.util.Properties;

public class ServerMain {
	private static final Logger LOGGER = Logger.getLogger(ServerMain.class);

	public static void main(String[] args) {
		BasicConfigurator.configure();

		Properties config = PropertiesLoader.get("config.properties");
		int port = Integer.parseInt(config.get("port").toString());

		Options options = new Options();
		CommandLineParser parser = new DefaultParser();

		options.addOption("p", "port", true, "Server port");

		try {
			CommandLine cmd = parser.parse( options, args);
			if(cmd.hasOption("p")) {
				port = Integer.parseInt(cmd.getOptionValue("p"));
			}

		} catch (ParseException e) {
			System.err.println("Error: invalid command line format.");
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "Pacman", options );
			System.exit(1);
		}

		ServerCore core = new ServerCore(port);
	}
}
