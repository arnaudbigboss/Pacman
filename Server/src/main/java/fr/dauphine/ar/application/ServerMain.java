package fr.dauphine.ar.application;

import fr.dauphine.ar.network.ServerCore;
import fr.dauphine.ar.properties.PropertiesLoader;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

public class ServerMain {
	private static final Logger LOGGER = Logger.getLogger(ServerMain.class);

	public static void main(String[] args) {
		BasicConfigurator.configure();
		Properties properties = PropertiesLoader.get("config.properties");
		int port = args.length == 1 ? Integer.parseInt(args[0]) : Integer.parseInt(properties.get("port").toString());
		System.out.println(port);
		try {
			ServerCore core = new ServerCore(port);
		} catch (IOException e) {
			LOGGER.error("Error during initialisation: " + e.toString());
		}
	}
}
