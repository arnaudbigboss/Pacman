package server.model;

public class Ghost extends Character{

	public Ghost(int x, int y) {
		this.x = x;
		this.y = y;
		alive = true;
	}
	
	@Override
	public int speed() {
		return Game.scale/5;
	}
	
	@Override
	public void move() {
		super.move();
		if(curentDirection == Direction.Stop)
			nextDirection = Direction.random();
	}

}
