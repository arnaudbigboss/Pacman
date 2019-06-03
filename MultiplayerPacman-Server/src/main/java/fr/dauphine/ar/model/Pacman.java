package fr.dauphine.ar.model;

public class Pacman extends Character{
	
	private enum pacmanState{
		Normal, Super;
	}
	
	private int score;
	private pacmanState state;
	private int clientID;
	private int id;
	
	private int timeSuper;
	
	public Pacman(int clientID) {
		score = 0;
		state = pacmanState.Normal;
		this.clientID = clientID;
		alive = true;
		timeSuper = 0;
	}
	
	public void setCoordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void addScore(int k) {
		score += k;
	}
	
	public int getScore() {
		return score;
	}
	
	public int getClientID() {
		return clientID;
	}
	
	public int getID() {
		return id;
	}
	
	public void setID(int id) {
		this.id = id;
	}
	
	public Direction getDirection() {
		return curentDirection;
	}
	
	public boolean isSuper() {
		return state==pacmanState.Super;
	}
	
	public void becomeSuper() {
		state = pacmanState.Super;
		if(x%15==5) 		x -= 5;
		else if (x%15==10)	x += 5;
		else if (y%15==5)	y -= 5;
		else if (y%15==10)	y += 5;
		timeSuper = 60;
	}
	
	@Override
	public void move() {
		super.move();
		if(state==pacmanState.Super)
			timeSuper--;
		if(timeSuper==0) {
			state=pacmanState.Normal;
		}
	}

	@Override
	public int speed() {
		if(state==pacmanState.Normal)
			return Game.scale/6;
		else if (state==pacmanState.Super)
			return Game.scale/2;
		return 0;
	}
	
	public void changeDirection(Direction direction) {
		nextDirection = direction;
	}
	
	@Override
	public void kill() {
		super.kill();
		score = 0;
	}

}
