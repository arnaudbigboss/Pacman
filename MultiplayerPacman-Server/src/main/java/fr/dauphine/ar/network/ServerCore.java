package fr.dauphine.ar.network;

import fr.dauphine.ar.model.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ServerCore extends Thread {
	private int port;
	ServerSocket ss;
	private boolean stop = false;
	private IPacmanLogger logger;

	public ServerCore(int port) throws IOException {
		this.port = port;
		logger = new TextPacmanLogger();
		logger.systemMessage("Server started...");
		this.start();
		new Thread(Game.getInstance()).start();
	}

	public void run() {
		try (ServerSocket ss = new ServerSocket(port)) {
			ss.setSoTimeout(1000);
			while (!stop) {
				try {
					Socket s = ss.accept();
					new Thread(new HandleClient(s, logger)).start();
				} catch (SocketTimeoutException ex) {
				}
			}
		} catch (IOException e) {
			System.out.println("Could not bind port " + port);
			Logger.getLogger(ServerCore.class.getName()).log(Level.SEVERE, null, e);
		}
	}
}
