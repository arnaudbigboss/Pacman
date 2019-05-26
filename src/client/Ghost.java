package client;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Ghost {
	private int x;
	private int y;
	
	private final BufferedImage[] ghosts = new BufferedImage[] {
	loadImage("ghost1.png"),
	loadImage("ghost2.png"),
	loadImage("ghost3.png"),
	loadImage("ghost4.png"),
	};
	
	private final Random r = new Random();
	private int color = r.nextInt(ghosts.length);
	
	public Ghost(int x, int y) {
		this.x = x;
		this.y = y;
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
	
	public BufferedImage draw() {
		return ghosts[color];
	}
	
	public BufferedImage loadImage(String filename) {
		try {
			return ImageIO.read(Ghost.class.getResourceAsStream("/images/"+filename));
	    } catch (IOException e) {
	    	System.out.println("Cant load image");
	    	return null;
	    }
	}
}
