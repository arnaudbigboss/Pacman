package fr.dauphine.ar.network;

import java.io.IOException;
import java.net.Socket;

import fr.dauphine.ar.gui.ClientGui;
import fr.dauphine.ar.model.Ghost;
import fr.dauphine.ar.model.Pacman;

public class ClientHandleConnection extends Thread implements PacmanProtocol{
	private Socket s;
	private PacmanInput pmi;
	private PacmanOutput pmo;
	private ClientGui gui;
	
	public ClientHandleConnection(Socket s){
		this.s = s;
		this.start();
	}
	
	@Override
	public void run() {
		try (Socket s1 = s){
			pmo = new PacmanOutput(s1.getOutputStream());
			pmi = new PacmanInput(s1.getInputStream(), this);
			pmi.doRun();
		} catch (IOException e) {
			
		}
	}
	
	// Output
	
	@Override
	public void player() {
		pmo.player();
	}
	
	public void gameStart() {
		pmo.gameStart();
	}
	
	public void down() {
		pmo.down();
	}
	
	public void up() {
		pmo.up();
	}
	
	public void left() {
		pmo.left();
	}
	
	public void right() {
		pmo.right();
	}
	
	// Input
	
	@Override
	public void size(int length, int width) {
		gui = new ClientGui(this, length, width);
	}
	
	@Override
	public void walls(boolean[][] walls) {
		gui.setWalls(walls);
		gui.repaint();
	}
	
	@Override
	public void pills(boolean[][] pills) {
		gui.setPills(pills);
		gui.repaint();
	}
	
	@Override
	public void eat(int x, int y) {
		gui.notPills(x,y);
		gui.repaint();
	}
	
	@Override
	public void fruit(boolean isActive, int x, int y) {
		gui.getFruit().set(isActive, x, y);
	}
	
	@Override
	public void pacman(int id, String pacmanState, int x, int y, String direction, int score) {
		Pacman p = gui.getPacmans().get(id);
		if(!(p==null)) {
			p.setX(x);
			p.setY(y);
			p.setSuper(pacmanState.equals("SUPER"));
			p.setDirection(direction);
			p.setScore(score);
		}
		else
			gui.getPacmans().put(id, new Pacman(x,y, pacmanState.equals("SUPER"), score));
		gui.repaint();
	}
	
	@Override
	public void ghost(int id, int x, int y) {
		Ghost g = gui.getGhosts().get(id);
		if(!(g==null)) {
			g.setX(x);
			g.setY(y);
		} else gui.getGhosts().put(id, new Ghost(x, y));
		gui.repaint();
	}
	
	@Override
	public void pacmanDead(int id) {
		gui.changeGameMessage(gui.getPacmans().get(id).name(id)+" IS DEAD!");
		gui.getPacmans().remove(id);
		gui.repaint();
	}
	
	@Override
	public void ghostDead(int id) {
		gui.getGhosts().remove(id);
		gui.repaint();
	}
	
	@Override
	public void admin() {
		gui.isAdmin();
		gui.changeGameMessage("YOU ARE NOW ADMIN!");
		gui.repaint();
	}
	
	@Override
	public void nbPlayers(int nbPlayers) {
		gui.setNbPlayers(nbPlayers);
		gui.repaint();
	}
	
	@Override
	public void over() {
		if(gui.getPacmans().size()==0)
			gui.changeGameMessage("NO WINNER");
		else if (gui.getPacmans().size()==1) {
			int id = gui.getPacmans().firstKey();
			gui.changeGameMessage(gui.getPacmans().get(id).name(id)+" WINS!");
		} else {
			int idMaxScore = gui.getPacmans().firstKey();
			for(Integer i : gui.getPacmans().keySet()) {
				if(gui.getPacmans().get(idMaxScore).getScore()<gui.getPacmans().get(i).getScore())
					idMaxScore = i;
			}
			gui.changeGameMessage(gui.getPacmans().get(idMaxScore).name(idMaxScore)+" WINS!");
		}
		gui.reset();
		gui.repaint();
	}
	
	@Override
	public void sendID(int id) {
		gui.changeGameMessage("You are Pacman "+gui.getPacmans().get(id).name(id));
		gui.repaint();
	}
}
