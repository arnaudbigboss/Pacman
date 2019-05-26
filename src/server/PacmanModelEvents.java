package server;

public interface PacmanModelEvents {
	public void gameOver();
	public void nbPlayers(int nbPlayers);
	public void admin();
	public void gameStep();
	public void eat(int x, int y);
	public void pacmanKilled(int pacmanID);
	public void ghostKilled(int ghostID);
	default void sendID(int id) {};
	public void fruit(int x, int y, boolean isActive);
}

