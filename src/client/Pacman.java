package client;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

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

	public boolean isSuper() {
		return isSuper;
	}

	public void setSuper(boolean isSuper) {
		this.isSuper = isSuper;
	}

	public String getDirection() {
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
	
	private final BufferedImage[] pacmanred = new BufferedImage[] {
			loadImage("pacmanred1.png"),
			loadImage("pacmanred2.png"),
			loadImage("pacmanred3.png"),
			loadImage("pacmanred4.png"),
	};
	
	private final BufferedImage[] pacmanblue = new BufferedImage[] {
			loadImage("pacmanblue1.png"),
			loadImage("pacmanblue2.png"),
			loadImage("pacmanblue3.png"),
			loadImage("pacmanblue4.png"),
	};
	
	private final BufferedImage[] pacmangreen = new BufferedImage[] {
			loadImage("pacmangreen1.png"),
			loadImage("pacmangreen2.png"),
			loadImage("pacmangreen3.png"),
			loadImage("pacmangreen4.png"),
	};
	
	private final BufferedImage[] pacmanyellow = new BufferedImage[] {
			loadImage("pacmanyellow1.png"),
			loadImage("pacmanyellow2.png"),
			loadImage("pacmanyellow3.png"),
			loadImage("pacmanyellow4.png"),
	};
	
	public BufferedImage draw(int i) {
		int index = 0;
		if(openMouth%2==1)
			index++;
		if(isSuper)
			index+=2;
		BufferedImage pacmanImage;
		if(i%4==0) pacmanImage = pacmanred[index];
		else if (i%4==1) pacmanImage = pacmanblue[index];
		else if (i%4==2) pacmanImage = pacmangreen[index];
		else pacmanImage = pacmanyellow[index];
		openMouth++;
		if(direction.equals("RIGHT"))
			return pacmanImage;
		else {
			AffineTransform transform = new AffineTransform();
			AffineTransformOp op;
			if(direction.equals("DOWN")) {
				transform.rotate(Math.PI/2, pacmanImage.getWidth()/2, pacmanImage.getHeight()/2);
				op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
				pacmanImage = op.filter(pacmanImage, null);
				return pacmanImage;
			} else if(direction.equals("LEFT")) {
				transform.concatenate(AffineTransform.getScaleInstance(1, -1));
		        transform.concatenate(AffineTransform.getTranslateInstance(0, -pacmanImage.getHeight()));
		        transform.rotate(Math.PI, pacmanImage.getWidth()/2, pacmanImage.getHeight()/2);
				op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
				pacmanImage = op.filter(pacmanImage, null);
				return pacmanImage;
			} else {
				transform.rotate(Math.PI/2, pacmanImage.getWidth()/2, pacmanImage.getHeight()/2);
				try {
					transform.invert();
				} catch (NoninvertibleTransformException e) {
					e.printStackTrace();
				}
				op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
				pacmanImage = op.filter(pacmanImage, null);
				return pacmanImage;
			}
		}
	}
	
	public BufferedImage loadImage(String filename) {
		try {
			return ImageIO.read(Pacman.class.getResourceAsStream("/images/"+filename));
	    } catch (IOException e) {
	    	System.out.println("Cant load image");
	    	return null;
	    }
	}

}
