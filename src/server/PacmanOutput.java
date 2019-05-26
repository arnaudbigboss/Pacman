package server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class PacmanOutput implements PacmanProtocol {
	
	PrintWriter os;
	
	public PacmanOutput(OutputStream out) throws IOException {
		this.os = new PrintWriter(out, true);
	}
	
	public synchronized void size(int length, int width) {
		os.println("SIZE");
		os.println(length);
		os.println(width);
	}
	
	public synchronized void walls(boolean[][] walls) {
		os.println("WALLS");
		for(int i=0; i<walls.length; i++) {
			for(int j=0; j<walls[0].length; j++) {
				if(walls[i][j]) {
					os.println(i);
					os.println(j);
				}
			}
		}
		os.println(".");
	}
	
	public synchronized void pills(boolean[][] pills) {
		os.println("PILLS");
		for(int i=0; i<pills.length; i++) {
			for(int j=0; j<pills[0].length; j++) {
				if(pills[i][j]) {
					os.println(i);
					os.println(j);
				}
			}
		}
		os.println(".");
	}
	
	public synchronized void eat(int x, int y) {
		os.println("EAT");
		os.println(x);
		os.println(y);
	}
	
	public synchronized void fruit(int x, int y, boolean isActive) {
		os.println("FRUIT");
		os.println(isActive);
		os.println(x);
		os.println(y);
	}
	
	public synchronized void nbPlayers(int nbPlayers) {
		os.println("NBPLAYERS");
		os.println(nbPlayers);
	}
	
	public synchronized void sendID(int id) {
		os.println("SENDID");
		os.println(id);
	}
	
	public synchronized void admin() {
		os.println("ADMIN");
	}

	public synchronized void pacman(int id, boolean isSuper, int x, int y, String direction, int score) {
		os.println("PACMAN");
		os.println(id);
		if(!isSuper) os.println("NORMAL");
		else os.println("SUPER");
		os.println(x);
		os.println(y);
		os.println(direction);
		os.println(score);
	}
	
	public synchronized void pacmanDead(int id){
		os.println("PACMANDEAD");
		os.println(id);
	}
	
	public synchronized void ghost(int id, int x, int y){
		os.println("GHOST");
		os.println(id);
		os.println(x);
		os.println(y);
	}
	
	public synchronized void ghostDead(int id) {
		os.println("GHOSTDEAD");
		os.println(id);
	}
	
	public synchronized void over() {
		os.println("OVER");
	}
}
