package fr.dauphine.ar.gui;

import fr.dauphine.ar.model.Pacman;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class ImageLoader {
    public static final BufferedImage PILL_IMAGE = loadImage("pill.png");
    public static final BufferedImage FRUIT_IMAGE = loadImage("fruit.png");

    private static final BufferedImage[] GHOSTS_IMAGES = new BufferedImage[] {
            loadImage("ghost1.png"),
            loadImage("ghost2.png"),
            loadImage("ghost3.png"),
            loadImage("ghost4.png"),
    };

    private static final BufferedImage[] PACMAN_RED = new BufferedImage[] {
            loadImage("pacmanred1.png"),
            loadImage("pacmanred2.png"),
            loadImage("pacmanred3.png"),
            loadImage("pacmanred4.png"),
    };

    private static final BufferedImage[] PACMAN_BLUE = new BufferedImage[] {
            loadImage("pacmanblue1.png"),
            loadImage("pacmanblue2.png"),
            loadImage("pacmanblue3.png"),
            loadImage("pacmanblue4.png"),
    };

    private static final BufferedImage[] PACMAN_GREEN = new BufferedImage[] {
            loadImage("pacmangreen1.png"),
            loadImage("pacmangreen2.png"),
            loadImage("pacmangreen3.png"),
            loadImage("pacmangreen4.png"),
    };

    private static final BufferedImage[] PACMAN_YELLOW = new BufferedImage[] {
            loadImage("pacmanyellow1.png"),
            loadImage("pacmanyellow2.png"),
            loadImage("pacmanyellow3.png"),
            loadImage("pacmanyellow4.png"),
    };

    private static final Random random = new Random();

    private static BufferedImage loadImage(String filename) {
        try {
            return ImageIO.read(ClientGui.class.getClassLoader().getResourceAsStream("images/"+filename));
        } catch (IOException e) {
            System.out.println("Cant load image");
            return null;
        }
    }

    public static BufferedImage drawGhost(int color){
        return GHOSTS_IMAGES[color];
    }

    public static BufferedImage drawPacman(Pacman p, int i) {
        int index = 0;
        if(p.getOpenMouth()%2==1)
            index++;
        if(p.isSuper())
            index+=2;
        BufferedImage pacmanImage;
        if(i%4==0) pacmanImage = PACMAN_RED[index];
        else if (i%4==1) pacmanImage = PACMAN_BLUE[index];
        else if (i%4==2) pacmanImage = PACMAN_GREEN[index];
        else pacmanImage = PACMAN_YELLOW[index];
        p.increaseOpenMouth();
        if(p.getDirection().equals("RIGHT"))
            return pacmanImage;
        else {
            AffineTransform transform = new AffineTransform();
            AffineTransformOp op;
            if(p.getDirection().equals("DOWN")) {
                transform.rotate(Math.PI/2, pacmanImage.getWidth()/2, pacmanImage.getHeight()/2);
                op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
                pacmanImage = op.filter(pacmanImage, null);
                return pacmanImage;
            } else if(p.getDirection().equals("LEFT")) {
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

    public static int nbGhostImages(){
        return GHOSTS_IMAGES.length;
    }
}
