package server.model;

import server.map.Map;

public abstract class Character {
	protected int x;
	protected int y;

	
	protected Direction curentDirection = Direction.Stop;
	protected Direction nextDirection = Direction.Stop;
	
	protected boolean alive;
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	public void kill() {
		alive = false;
	}

	public void move() {
		if(x%Game.scale==0 && y%Game.scale==0) {
			curentDirection = nextDirection;
		}
		
		int nRealX = x;
		int nRealY = y;
		int nx,ny;	


		if(curentDirection == Direction.Up) {
			nRealY -= speed();
			ny = nRealY / Game.scale;
			nx = (nRealX+Game.scale/2)/Game.scale;
		} else if(curentDirection == Direction.Down) {
			nRealY += speed();
			ny = (nRealY / Game.scale);
			if(nRealY % Game.scale != 0) { ny++; }
			nx = (nRealX+Game.scale/2)/Game.scale;
		} else if(curentDirection == Direction.Right) {
			nRealX = (nRealX + speed()) % (Map.length()*Game.scale);
			nx = nRealX / Game.scale;
			if(nRealX % Game.scale != 0) { nx++; }
			ny = (nRealY+Game.scale/2)/Game.scale;
		} else if(curentDirection == Direction.Left) {
			nRealX = (nRealX - speed()) % (Map.length()*Game.scale);				
			nx = nRealX / Game.scale;
			ny = (nRealY+Game.scale/2)/Game.scale;				
		} else {				
			return;
		}
		
		if(Map.isWall(nx,ny)) {
			curentDirection = Direction.Stop;
		} else {
			x = nRealX;
			y = nRealY;
		}
	}
	
	public boolean isCloseTo(Character c) {
		if(!(this.equals(c))) {
			if(this.x/Game.scale == c.x/Game.scale && this.y/Game.scale == c.y/Game.scale)
				return true;
		}
		return false;
	}
	
	public abstract int speed();

}
