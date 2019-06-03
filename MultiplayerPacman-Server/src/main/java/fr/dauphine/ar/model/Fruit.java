package fr.dauphine.ar.model;

import fr.dauphine.ar.map.Map;

public class Fruit {
	private int x;
	private int y;
	private boolean active;
	private int timeInactive;
	
	private static Fruit f = null;

	private Fruit(int x, int y) {
		this.x = x;
		this.y = y;
		active = true;
		timeInactive = 0;
	}
	
	public static Fruit getInstance() {
		if(f==null)
			f = new Fruit(Map.ghostSpawn()[0], Map.ghostSpawn()[1]);
		return f;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void eat() {
		active = false;
		timeInactive = 500;
	}
	
	public void step() {
		if(timeInactive<=0)
			active = true;
		else timeInactive--;
	}
	
	public void reset() {
		active = true;
		timeInactive = 0;
	}
}
