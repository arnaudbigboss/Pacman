// Merci à Viviane pour ses dessins (Instagram: @vivipantsu)

package fr.dauphine.ar.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

import fr.dauphine.ar.model.Fruit;
import fr.dauphine.ar.model.Pacman;
import fr.dauphine.ar.model.Ghost;
import fr.dauphine.ar.network.ClientHandleConnection;

public class ClientGui{
	private int scale = 30;
	private final int borderX = 300;
	private final int borderY = 30;
	
	private boolean[][] walls;
	private boolean[][] pills;
	private Fruit fruit = new Fruit(0,0,false);
	
	private TreeMap<Integer, Pacman> pacmans = new TreeMap<Integer, Pacman>();
	private TreeMap<Integer, Ghost> ghosts = new TreeMap<Integer, Ghost>();

	private JFrame frame;
	private JComponent component;
	
	private int nbPlayers = 0;
	private boolean admin = false;
	private boolean player = false;
	
	private final BufferedImage pillImage = loadImage("pill.png");
	private final BufferedImage fruitImage = loadImage("fruit.png");
	
	private String gameMessage = "";
	
	public ClientGui(ClientHandleConnection handler, int length, int width) {
		this.walls = new boolean[length][width];
		this.pills = new boolean[length][width];
		
		component = new JComponent() {
			@Override
			protected void paintComponent(Graphics g) {
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, length*scale, width*scale);
				g.drawString(gameMessage, length*scale+borderX/10, borderY);
				
				if(pacmans.size()==0)
					g.drawString("Nombre de joueurs: "+nbPlayers, length*scale+borderX/10, borderY*2);
				
				if(player && admin && nbPlayers>1 && nbPlayers<5 && pacmans.size()==0) {
					g.setColor(Color.GREEN);
					g.fillRect(length*scale+borderX/5, width*scale-borderY*2, scale, scale);
					g.setColor(Color.BLACK);
					g.drawString("START", length*scale+borderX/5, width*scale-borderY*2);
				} else if(!player && nbPlayers<5) {
					g.setColor(Color.RED);
					g.fillRect(length*scale+borderX/5, width*scale-borderY*2, scale, scale);
					g.setColor(Color.BLACK);
					g.drawString("PLAY", length*scale+borderX/5, width*scale-borderY*2);
				}
				
				int xScore = length*scale+borderX/10;
				int yScore = borderY*4;
				for(Integer i : pacmans.keySet()) {
					g.drawString(pacmans.get(i).name(i)+": "+pacmans.get(i).getScore(), xScore, yScore);
					yScore += borderY;
				}
				
				for(int i=0; i<length; i++) {
					for(int j=0; j<width; j++) {
						g.setColor(Color.BLUE);
						if(walls[i][j])
							g.fillRect(i*scale, j*scale, scale, scale);
						g.setColor(Color.WHITE);
						if(pills[i][j])
							g.drawImage(pillImage, i*scale, j*scale, this);
					}
				}
				
				if(fruit.isActive())
					g.drawImage(fruitImage, fruit.getX()*scale, fruit.getY()*scale, this);
				
				for(Ghost ghost : ghosts.values()) {
					g.drawImage(ghost.draw(), ghost.getX(), ghost.getY(), this);
				}
				
				for(Integer i : pacmans.keySet()) {
					Pacman p = pacmans.get(i);
					g.drawImage(p.draw(i), p.getX(), p.getY(), this);
				}
			}
		};
		
		frame = new JFrame("Multiplayer Pacman");
		frame.add(component);
		frame.setSize(length*scale+borderX, width*scale+borderY);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    frame.setLocation(((int)(screenSize.getWidth() - frame.getWidth()) / 2),
	    		(int)((screenSize.getHeight() - frame.getHeight()) / 2));
		frame.setResizable(false);
		frame.setVisible(true);
		frame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(player && !(pacmans.size()==0)) {
					super.keyPressed(e);
					switch (e.getKeyCode()) {
					case KeyEvent.VK_LEFT:
						handler.left();
						break;
					case KeyEvent.VK_RIGHT:
						handler.right();
						break;					
					case KeyEvent.VK_UP:
						handler.up();
						break;
					case KeyEvent.VK_DOWN:
						handler.down();
						break;
					default:
						break;
					}
				}
			}
		});
		frame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				int x = e.getX();
				int y = e.getY();
				if(player && admin && nbPlayers>1 && nbPlayers<5 && pacmans.size()==0) {
					if(x>=length*scale+borderX/5 && x<=length*scale+borderX/5+scale
						&& y>=width*scale-borderY*2+3+scale && y<=width*scale-borderY*2+3+scale+scale)
						handler.gameStart();
				} else if(!player && nbPlayers<5) {
					if(x>=length*scale+borderX/5 && x<=length*scale+borderX/5+scale
							&& y>=width*scale-borderY*2+3+scale && y<=width*scale-borderY*2+3+scale+scale) {
						handler.player();
						player=true;
						gameMessage = "You are now a player!";
						repaint();
					}
				}
			}
		});
	}
	
	public void setWalls(boolean[][] walls) {
		this.walls = walls;
	}
	
	public void setPills(boolean[][] pills) {
		this.pills = pills;
	}
	
	public Fruit getFruit() {
		return fruit;
	}
	
	public void changeGameMessage(String s) {
		gameMessage = s;
	}
	
	public void notPills(int x, int y) {
		pills[x][y] = false;
	}
	
	public void isAdmin() {
		admin = true;
	}
	
	public void reset() {
		admin = false;
		player= false;
		nbPlayers = 0;
		pacmans.clear();
		ghosts.clear();
	}
	
	public void setNbPlayers(int nbPlayers) {
		this.nbPlayers = nbPlayers;
	}

	public void repaint() {
		component.repaint();
	}
	
	public TreeMap<Integer, Pacman> getPacmans(){
		return pacmans;
	}
	
	public TreeMap<Integer, Ghost> getGhosts(){
		return ghosts;
	}
	
	public void notPlayer() {
		player = false;
	}
	
	public BufferedImage loadImage(String filename) {
		try {
			return ImageIO.read(ClientGui.class.getResourceAsStream("images/"+filename));
	    } catch (IOException e) {
	    	System.out.println("Cant load image");
	    	return null;
	    }
	}
}
