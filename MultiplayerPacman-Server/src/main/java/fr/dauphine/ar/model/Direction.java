package fr.dauphine.ar.model;

import java.util.Random;

public enum Direction {
	Up(0,-1), Down(0,1), Left(-1,0), Right(1,0), Stop(0,0);
	
	private int x;
	private int y;
	
	Direction(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	private static final Direction[] values = values();
	private static final int size = values.length-1;
	private static final Random r = new Random();
	
	public static Direction random() {
		return values[r.nextInt(size)];
	}
	
	@Override
	public String toString() {
		if(this==Direction.Up) {
			return "UP";
		} else if(this==Direction.Down) {
			return "DOWN";
		} else if(this==Direction.Left) {
			return "LEFT";
		} else if(this==Direction.Right) {
			return "RIGHT";
		} else return "STOP";
	}
}
