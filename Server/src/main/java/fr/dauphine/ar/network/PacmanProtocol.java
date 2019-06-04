package fr.dauphine.ar.network;

import fr.dauphine.ar.model.Direction;

public interface PacmanProtocol {
	// Input
	default void init() {}
	default void player() {}
	default void start() {}
	default void move(Direction direction) {}
	
	// Output
	default void size(int length, int width) {}
	default void walls(boolean [][] walls) {}
	default void pills(boolean[][] pills) {}
	default void eat(int x, int y) {}
	default void fruit(int x, int y) {}
	default void nbPlayers(int nbPlayers) {}
	default void sendID(int id) {}
	default void admin() {}
	default void pacman(int id, boolean isSuper, int x, int y, String direction, int score) {}
	default void pacmanDead(int id) {}
	default void ghost(int id, int x, int y) {}
	default void ghostDead(int id) {}
	default void over() {}
}
