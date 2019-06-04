package fr.dauphine.ar.network;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class PacmanOutput implements PacmanProtocol{
	PrintWriter os;
	
	public PacmanOutput(OutputStream out) throws IOException {
		this.os = new PrintWriter(out, true);
		os.println("INIT");
	}
	
	public void player() {
		os.println("PLAYER");
	}
	
	public void gameStart() {
		os.println("START");
	}
	
	public void right() {
		os.println("RIGHT");
	}
	
	public void left() {
		os.println("LEFT");
	}
	
	public void up() {
		os.println("UP");
	}
	
	public void down() {
		os.println("DOWN");
	}
}
