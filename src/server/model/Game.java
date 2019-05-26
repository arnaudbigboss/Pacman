package server.model;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeMap;

import server.PacmanModelEvents;
import server.map.Map;

public class Game implements Runnable{
	
	private static Game model = new Game();
    
    public synchronized static Game getInstance() {
    	return model;
    }
	
	public static final int scale = 30;
	
	public final int nbPlayersMax = 4;
	
	public final int eatPillScore = 1;
	public final int eatGhostScore = 10;
	public final int eatPacmanScore = 20;
	
	private int idNextClient;
	
	private TreeMap<Integer, PacmanModelEvents> clients = new TreeMap<>();
	private TreeMap<Pacman, PacmanModelEvents> players = new TreeMap<>(new Comparator<Pacman>() {
		@Override
		public int compare(Pacman p1, Pacman p2) {
			return p1.getClientID()-p2.getClientID();
		}
	});
	
	private TreeMap<Integer, Ghost> ghosts = new TreeMap<Integer, Ghost>();
	
	public synchronized int getGhostID(Ghost g) {
		for(Integer i : ghosts.keySet())
			if(ghosts.get(i).equals(g))
				return i;
		return -1;
	}

	private boolean[][] pills = Map.pills();
	
	private int nbPlayerRemaining = 0;
	
    private Game(){
    	state = gameState.Waiting;
    	idNextClient = 0;
    }
    
    private enum gameState{
    	Waiting, Playing;
    }
    
    private gameState state;

	public void start() { // Seul le thread de l'administrateur peut lancer cette méthode
		if(players.size()>1 && players.size()<nbPlayersMax+1) {
			initGame();
			state = gameState.Playing;
		}
	}
    
	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(100);
				if(state==gameState.Playing) {
					// Les personnages se déplacent
					int x;
					int y;
					Fruit f = Fruit.getInstance();
					f.step();
					notifyFruit(f.isActive());
					for(Pacman p : players.keySet()) {
						if(p.isAlive()) {
							p.move();
							x = p.getX()/Game.scale;
							y = p.getY()/Game.scale;
							if(f.isActive() && f.getX() == x && f.getY() == y) {
								f.eat();
								p.becomeSuper();
								p.addScore(eatPillScore);
							}
							if(pills[x][y]) {
								p.addScore(eatPillScore);
								pills[x][y] = false;
								notifyEatPill(x,y);
							}
							if(p.isSuper()) {
								for(Pacman p2 : players.keySet())
									if(p.isCloseTo(p2)) {
										p2.kill();
										nbPlayerRemaining--;
										notifyPacmanKilled(p2);
										p.addScore(eatPacmanScore);
									}
								for(Ghost g : ghosts.values())
									if(p.isCloseTo(g)) {
										g.kill();
										notifyGhostKilled(g);
										p.addScore(eatGhostScore);
									}
							}
						}
					}
					
					for(Ghost g : ghosts.values()) {
						if(g.isAlive()) {
							g.move();
							for(Pacman p : players.keySet())
								if(g.isCloseTo(p) && p.isAlive()) {
									p.kill();
									nbPlayerRemaining--;
									notifyPacmanKilled(p);
								}
						}
					}
					
					notifyGameStep();

					if(!(nbPlayerRemaining>1) || pillsRemaining()==0)
						finish();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	// Initialiseurs & Finish
	
	private void initGame() {
		int nbGhosts = players.size()*3;
		nbPlayerRemaining = players.size();
		for(int i=0; i<nbGhosts; i++) {
    		ghosts.put(i, new Ghost(Map.ghostSpawn()[0]*scale, Map.ghostSpawn()[1]*scale));
    	}
		for(int i=0; i<players.size(); i++) {
			int x = Map.pacmanSpawns()[i][0];
			int y = Map.pacmanSpawns()[i][1];
			((Pacman) players.keySet().toArray()[i]).setCoordinate(x*scale, y*scale);
			((Pacman) players.keySet().toArray()[i]).setID(i);
		}
		pills = Map.pills();
		notifyGameStep();
		for(int i=0; i<players.size(); i++) {
			players.get(((Pacman) players.keySet().toArray()[i])).sendID(i);
		}
	}
	
	private void finish() {
		notifyGameOver();
		nbPlayerRemaining = 0;
		ghosts.clear();
		players.clear();
		pills = Map.pills();
		state = gameState.Waiting;
		Fruit.getInstance().reset();
	}

	// Pills
	
	public synchronized boolean[][] getPills(){
		return pills;
	}
	
	private int pillsRemaining() {
		int k = 0;
		for(int i=0; i<pills.length; i++) {
			for(int j=0; j<pills[0].length; j++) {
				if(pills[i][j])
					k++;
			}
		}
		return k;
	}
	
	// Pacman 
	
	public synchronized Set<Pacman> getPacmans(){
		return players.keySet();
	}
	
	public synchronized Pacman getPacman(int clientID) {
		for(Pacman p : players.keySet()) {
			if(p.getClientID() == clientID)
				return p;
		}
		return null;
	}
	
	public synchronized void changePacmanDirection(Direction direction, int clientID) {
		for(Pacman p : players.keySet())
			if(p.getClientID()==clientID) p.changeDirection(direction);
	}
	
	// Ghosts
	
	public synchronized TreeMap<Integer, Ghost> getGhosts(){
		return ghosts;
	}
	
	// Client
	
	public synchronized int connect(PacmanModelEvents client) {
		int id = idNextClient;
		clients.put(id, client);
		idNextClient++;
		return id;
	}
	
	public synchronized boolean join(PacmanModelEvents client, int clientID) {
		if(players.size()<nbPlayersMax) {
			players.put(new Pacman(clientID), client);
			notifyNbPlayers();
			if(players.size()==1) client.admin();
			return true;
		} return false;
	}
	
	public synchronized void removeClient(int clientID, boolean isAdmin) {
		clients.remove(clientID);
		Pacman p = getPacman(clientID);
		if(!(p==null)) {
			if(!(state==gameState.Waiting)) {
				p.kill();
				nbPlayerRemaining--;
				notifyPacmanKilled(p);
			} else {
				players.remove(p);
				notifyNbPlayers();
				if(isAdmin && players.size()>0)
					players.get(players.firstKey()).admin();
				
			}
		}
	}
	
	// Events
	
	private void notifyGameStep() {
		clients.values().forEach(PacmanModelEvents :: gameStep);
	}
	
	private void notifyNbPlayers() {
		for(PacmanModelEvents e : clients.values()) e.nbPlayers(players.size());
	}
	
	private void notifyGameOver() {
		clients.values().forEach(PacmanModelEvents :: gameOver);
	}
	
	private void notifyEatPill(int x, int y) {
		for(PacmanModelEvents e : clients.values()) e.eat(x, y);
	}
	
	private void notifyPacmanKilled(Pacman p) {
		for(PacmanModelEvents e : clients.values()) e.pacmanKilled(p.getID());
	}
	
	private void notifyGhostKilled(Ghost g) {
		for(PacmanModelEvents e : clients.values()) e.ghostKilled(getGhostID(g));
	}
	
	private void notifyFruit(boolean isActive) {
		for(PacmanModelEvents e : clients.values()) 
			e.fruit(Fruit.getInstance().getX(), Fruit.getInstance().getY(), isActive);
	}
}
