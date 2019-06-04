package fr.dauphine.ar.network;

public interface IPacmanLogger {
	public void clientConnected(int id);
	
	public void clientDisconnected(int id);
	
	public void gameStart();
	
	public void gameOver();
	
	public void systemMessage(String msg);
}
