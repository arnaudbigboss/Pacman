package fr.dauphine.ar.map;

import java.io.IOException;
import java.io.InputStream;


public class Map {
	private static Map m = new Map("map.txt");
	private int length;
	private int width;
	private int[][] map;
	
	private Map(String mapFile){
		try {	
			//Ouverture fichier
			InputStream f = this.getClass().getClassLoader().getResourceAsStream(mapFile);
			byte[] buffer = new byte[3];
	    	
	    	// On récupère Longueur et Largeur de la map    	
	    	f.read(buffer);
	    	length = buffer[0]*10-48*10+buffer[1]-48;
	    
	    	f.read(buffer);
	    	width = buffer[0]*10-48*10+buffer[1]-48;

	    	// On récupère la map
	    	
	    	this.map = new int[length][width];
	    	
	    	byte[] buf = new byte[length+1];
	    	for(int i=0; i<width; i++) {
	    		f.read(buf);
	    		for(int j=0; j<length; j++) {
	    			map[j][i] = buf[j]-48;
	    		}
	    	}
	    	
	    	//Fermeture Fichier
	    	f.close();
		}
		catch (IOException e) {
        	e.printStackTrace();
		}
	}
	
	public static int length() {
		return m.length;
	}
	
	public static int width() {
		return m.width;
	}
	
	public static int[][] pacmanSpawns(){
		int[][] pacmanSpawns = new int[4][2];
		for(int i=0; i<m.length; i++) {
			for(int j=0; j<m.width; j++) {
				if(m.map[i][j]==1 || m.map[i][j]==2 || m.map[i][j]==3 || m.map[i][j]==4) {
					pacmanSpawns[m.map[i][j]-1][0] = i;
					pacmanSpawns[m.map[i][j]-1][1] = j;
				}
			}
		}
		return pacmanSpawns;
	}
	
	public static boolean[][] walls(){
		boolean[][] walls = new boolean[m.length][m.width];
		for(int i=0; i<m.length; i++) {
			for(int j=0; j<m.width; j++) {
				if(m.map[i][j] == 39)
					walls[i][j] = true;
			}
		}
		return walls;
	}
	
	public static boolean[][] pills(){
		boolean[][] pills = new boolean[m.length][m.width];
		for(int i=0; i<m.length; i++) {
			for(int j=0; j<m.width; j++) {
				if(m.map[i][j] == 32) {
					pills[i][j] = true;
				}
			}
		}
		return pills;
	}
	
	public static int[] ghostSpawn() {
		int[] ghostSpawn = new int[2];
		for(int i=0; i<m.length; i++) {
			for(int j=0; j<m.width; j++) {
				if(m.map[i][j]==23) {
					ghostSpawn[0] = i;
					ghostSpawn[1] = j;
				}
			}
		}
		return ghostSpawn;
	}
	
	public static boolean isWall(int x, int y) {
		return walls()[x][y];
	}
}
