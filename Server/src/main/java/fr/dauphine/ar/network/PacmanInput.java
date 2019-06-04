package fr.dauphine.ar.network;

import fr.dauphine.ar.model.Direction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PacmanInput {
	PacmanProtocol handler;
	InputStream in;
	boolean stop = false;

	public PacmanInput(InputStream in, PacmanProtocol handler) throws IOException {
		this.in = in;
		this.handler = handler;
	}

	public void doRun() throws IOException {
		try (BufferedReader is = new BufferedReader(new InputStreamReader(in))) {
			while (!stop) {
				String line = is.readLine();
				if(line==null) throw new IOException();
				switch (line) {
				case "INIT":
					handler.init();
					break;
				case "PLAYER":
					handler.player();
					break;
				case "START":
					handler.start();
					break;
				case "UP":
					handler.move(Direction.Up);
					break;
				case "DOWN":
					handler.move(Direction.Down);
					break;
				case "RIGHT":
					handler.move(Direction.Right);
					break;
				case "LEFT":
					handler.move(Direction.Left);
					break;
				default:
					throw new PacmanProtocolException("Invalid input");
				}
			}
		}
	}
}
