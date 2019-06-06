package fr.dauphine.ar.model;

public class Pacman {
	private int x;
	private int y;
	private boolean isSuper;
	private String direction;
	private int score;
	private int openMouth = 0;
	
	public Pacman(int x, int y, boolean isSuper, int score) {
		this.x = x;
		this.y = y;
		this.isSuper = isSuper;
		direction = "RIGHT";
		this.score = score;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isSuper(){
		return isSuper;
	}

	public void setSuper(boolean isSuper) {
		this.isSuper = isSuper;
	}

	public String getDirection(){
		return direction;
	}
	
	public void setDirection(String direction) {
		if(!(direction.equals("STOP"))) this.direction = direction;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public int getScore() {
		return score;
	}

	public int getOpenMouth(){
		return openMouth;
	}

	public void increaseOpenMouth(){
		openMouth++;
	}
	
	public String name(int key) {
		switch(key) {
		case 0:
			return "RED";
		case 1:
			return "BLUE";
		case 2:
			return "GREEN";
		case 3: 
			return "YELLOW";
		default:
			return "";
		}
	}

}
