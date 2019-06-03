package fr.dauphine.ar.network;

public class TextPacmanLogger implements IPacmanLogger {

	@Override
	public void clientConnected(int id) {
		System.out.println("New client connected. Id:"+id);
	}

	@Override
	public void clientDisconnected(int id) {
		System.out.println("Client "+id+" disconnected.");
	}

	@Override
	public void systemMessage(String msg) {
		System.out.println(msg);
	}

	@Override
	public void gameStart() {
		System.out.println("The game has started");
	}

	@Override
	public void gameOver() {
		System.out.println("The game is over");
	}
}
