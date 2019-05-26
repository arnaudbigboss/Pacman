package client;

import java.io.IOException;
import java.net.Socket;

public class ClientMain {
	public static void main(String[] args) {
		int port = args.length == 1 ? Integer.parseInt(args[0]) : 1234;
		String server = "localhost";
		try {
			Socket s = new Socket(server, port);
			ClientHandleConnection handler = new ClientHandleConnection(s);
		} catch (IOException e) {
			System.out.println("Could not connect");
		}
	}
}
