package fr.dauphine.ar.network;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class PacmanInput {
	private static final Logger LOGGER = Logger.getLogger(PacmanInput.class);

	private PacmanProtocol handler;
	private InputStream in;
	private boolean stop = false;

	public PacmanInput(InputStream in, PacmanProtocol handler) {
		this.in = in;
		this.handler = handler;
	}

	public void doRun() throws IOException {
		int length = 0;
		int width = 0;
		String x;
		String y;
		int id = 0;
		String pacmanState;
		String direction;
		int score = 0;
		try (BufferedReader is = new BufferedReader(new InputStreamReader(in))) {
			while (!stop) {
				String line = is.readLine();
				if(line==null) {
					LOGGER.error("Cannot read input: line is null");
					System.exit(1);
				}
				switch (line) {
				case "SIZE":
					length = Integer.parseInt(is.readLine());
					width = Integer.parseInt(is.readLine());
					handler.size(length, width);
					break;
				case "WALLS":
					boolean[][] walls = new boolean[length][width];
					while(!(x=is.readLine()).equals(".")) {
						y=is.readLine();
						walls[Integer.parseInt(x)][Integer.parseInt(y)] = true;
					}
					handler.walls(walls);
					break;
				case "PILLS":
					boolean[][] pills = new boolean[length][width];
					while(!(x=is.readLine()).equals(".")) {
						y=is.readLine();
						pills[Integer.parseInt(x)][Integer.parseInt(y)] = true;
					}
					handler.pills(pills);
					break;
				case "EAT":
					x = is.readLine();
					y = is.readLine();
					handler.eat(Integer.parseInt(x), Integer.parseInt(y));
					break;
				case "FRUIT":
					handler.fruit(Boolean.parseBoolean(is.readLine()),
							Integer.parseInt(is.readLine()), Integer.parseInt(is.readLine()));
					break;
				case "PACMAN":
					id = Integer.parseInt(is.readLine());
					pacmanState = is.readLine();
					x = is.readLine();
					y = is.readLine();
					direction = is.readLine();
					score = Integer.parseInt(is.readLine());
					handler.pacman(id, pacmanState, Integer.parseInt(x), Integer.parseInt(y), direction, score);
					break;
				case "GHOST":
					id = Integer.parseInt(is.readLine());
					x = is.readLine();
					y = is.readLine();
					handler.ghost(id, Integer.parseInt(x), Integer.parseInt(y));
					break;
				case "NBPLAYERS":
					handler.nbPlayers(Integer.parseInt(is.readLine()));
					break;
				case "SENDID":
					handler.sendID(Integer.parseInt(is.readLine()));
					break;
				case "ADMIN":
					handler.admin();
					break;
				case "PACMANDEAD":
					id = Integer.parseInt(is.readLine());
					handler.pacmanDead(id);
					break;
				case "GHOSTDEAD":
					id = Integer.parseInt(is.readLine());
					handler.ghostDead(id);
					break;
				case "OVER":
					handler.over();
					break;
				default:
					LOGGER.error("Invalid protocole input");
					System.exit(1);
				}
			}
		}
	}
}
