package fr.dauphine.ar.network;

public interface PacmanProtocol {
	// Output
	default void player() {}
	default void gameStart() {}
	default void right() {}
	default void left() {}
	default void up() {}
	default void down() {}
	
	// Input
	default void size(int length, int width) {}
	default void walls(boolean [][] walls) {}
	default void pills(boolean[][] pills) {}
	default void eat(int x, int y) {}
	default void fruit(boolean isActive, int x, int y) {}
	default void nbPlayers(int nbPlayers) {}
	default void sendID(int id) {}
	default void admin() {}
	default void pacman(int id, String pacmanState, int x, int y, String direction, int score) {}
	default void pacmanDead(int id) {}
	default void ghost(int id, int x, int y) {}
	default void ghostDead(int id) {}
	default void over() {}
}
