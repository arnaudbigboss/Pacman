package fr.dauphine.ar.network;

import fr.dauphine.ar.model.Game;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;


public class ServerCore extends Thread {
	private static final Logger LOGGER = Logger.getLogger(ServerCore.class);

	private int port;
	private ServerSocket ss;
	private boolean stop = false;

	public ServerCore(int port) throws IOException {
		this.port = port;
		LOGGER.info("Server started...");
		this.start();
		new Thread(Game.getInstance()).start();
	}

	public void run() {
		try (ServerSocket ss = new ServerSocket(port)) {
			ss.setSoTimeout(1000);
			while (!stop) {
				try {
					Socket s = ss.accept();
					new Thread(new HandleClient(s)).start();
				} catch (SocketTimeoutException ex) {
				}
			}
		} catch (IOException e) {
			LOGGER.error("Could not bind port "+port);
			LOGGER.fatal(e);
		}
	}
}
