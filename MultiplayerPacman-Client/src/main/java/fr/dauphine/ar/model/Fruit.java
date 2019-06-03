package fr.dauphine.ar.model;

public class Fruit {
	private int x;
	private int y;
	private boolean isActive;
	
	
	public Fruit(int x, int y, boolean isActive) {
		super();
		this.x = x;
		this.y = y;
		this.isActive = isActive;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean isActive() {
		return isActive;
	}
	
	public void set(boolean isActive, int x, int y) {
		this.isActive = isActive;
		this.x = x;
		this.y = y;
	}
}
