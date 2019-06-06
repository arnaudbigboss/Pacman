package fr.dauphine.ar.network;

import fr.dauphine.ar.map.Map;
import fr.dauphine.ar.model.Direction;
import fr.dauphine.ar.model.Game;
import fr.dauphine.ar.model.Ghost;
import fr.dauphine.ar.model.Pacman;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.Socket;

public class HandleClient implements Runnable, PacmanProtocol, PacmanModelEvents {
	private static final Logger LOGGER = Logger.getLogger(HandleClient.class);

	private final Socket s;
	private PacmanOutput pmo;
	private PacmanInput pmi;
	private Game game = Game.getInstance();
	private int id;
	private boolean isAdmin;

	private boolean stop = false;

	public 	HandleClient(Socket s){
		this.s = s;
		isAdmin = false;
	}

	@Override
	public void run() {
		try (Socket s1 = s) {
			pmo = new PacmanOutput(s1.getOutputStream());
			pmi = new PacmanInput(s1.getInputStream(), this);
			pmi.doRun();
		} catch (IOException e) {
			if (!stop) {
				finish();
			}
		}
	}
	
	public void init() {
		id = game.connect(this);
		LOGGER.info("New client connected. Id: "+id);
		pmo.size(Map.length(), Map.width());
		pmo.walls(Map.walls());
		pmo.pills(game.getPills());
	}
	
	public void player() {
		game.join(this, id);
	}
	
	public void start() {
		if(isAdmin)
			game.start();
	}
	
	public void move(Direction direction) {
		game.changePacmanDirection(direction, id);
	}
	
	public synchronized void finish() {
		if (!stop) {
			stop = true;
			try {
				s.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			game.removeClient(id, isAdmin);
			LOGGER.info("Client "+id+" disconnected");
		}
	}

	
	// PacmanModelEvents
	@Override
	public void gameStep() {
		for(Pacman p : game.getPacmans())
			if(p.isAlive())
				pmo.pacman(p.getID(), p.isSuper(), p.getX(), p.getY(), p.getDirection().toString(), p.getScore());
		for(Ghost g : game.getGhosts().values())
			if(g.isAlive())
				pmo.ghost(game.getGhostID(g), g.getX(), g.getY());
	}

	@Override
	public void gameOver() {
		pmo.over();
		pmo.pills(Map.pills());
	}
	
	@Override
	public void admin() {
		isAdmin = true;
		pmo.admin();
	}
	
	@Override
	public void nbPlayers(int nbPlayers) {
		pmo.nbPlayers(nbPlayers);
	}
	
	@Override
	public void eat(int x, int y) {
		pmo.eat(x, y);
	}

	@Override
	public void pacmanKilled(int pacmanID) {
		pmo.pacmanDead(pacmanID);
	}

	@Override
	public void ghostKilled(int ghostID) {
		pmo.ghostDead(ghostID);
	}
	
	@Override
	public void sendID(int id) {
		pmo.sendID(id);
	}
	
	@Override
	public void fruit(int x, int y, boolean isActive) {
		pmo.fruit(x, y, isActive);
	}
}
