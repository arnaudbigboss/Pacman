package fr.dauphine.ar.application;

import java.io.IOException;
import java.net.Socket;

import fr.dauphine.ar.network.ClientHandleConnection;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class ClientMain {
	private static final Logger LOGGER = Logger.getLogger(ClientMain.class);
	public static void main(String[] args) {
		BasicConfigurator.configure();

		int port = args.length == 1 ? Integer.parseInt(args[0]) : 1234;
		String server = "localhost";
		try {
			Socket s = new Socket(server, port);
			ClientHandleConnection handler = new ClientHandleConnection(s);
		} catch (IOException e) {
			LOGGER.fatal("Could not connect");
		}
	}
}
