package es.ucm.fdi.tp.control.multiplayer;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.GameFactory;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.control.commands.Command;
import es.ucm.fdi.tp.basecode.bgame.control.commands.PlayCommand;
import es.ucm.fdi.tp.basecode.bgame.control.commands.QuitCommand;
import es.ucm.fdi.tp.basecode.bgame.control.commands.RestartCommand;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.control.GameAdapter;
import es.ucm.fdi.tp.control.multiplayer.Responses.Response;

public class GameClient extends Controller implements Observable<GameObserver> {
	
	private final String host;
	private final int port;
	private Connection serverConnection;
	
	private List<GameObserver> observers;
	
	private Piece localPiece;
	private GameFactory gameFactory;
	
	volatile private boolean gameOver;

	public GameClient(String hostname, int port) throws Exception {
		super(null, null); //The client has no model
		this.host = hostname;
		this.port = port;
		this.observers = new ArrayList<GameObserver>(3);
		connect();
	}

	

	public GameFactory getGameFactoty() {
		return gameFactory;
	}

	public Piece getPlayerPiece() {
		return localPiece;
	}

	@Override
	public void makeMove(Player p) {
		forwardCommand(new PlayCommand(p));
	}

	@Override
	public void stop() {
		if(serverConnection != null)
			forwardCommand(new QuitCommand());
	}

	@Override
	public void restart() {
		forwardCommand(new RestartCommand());
	}
	
	 // if the game is over do nothing, otherwise
	 // send the object cmd to the server
	private void forwardCommand(Command cmd) {
		if(!gameOver) {
			try {
				serverConnection.sendObject(cmd);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void connect() throws Exception {
		serverConnection = new Connection(new Socket(host, port));
		serverConnection.sendObject(GameServer.MESSAGE_REQUEST);
		Object response = serverConnection.getObject();
		if ((response instanceof Exception)){
			throw (Exception) response;
		}
		
		if(!(response instanceof String) ||
				!((String) response).equalsIgnoreCase(GameServer.MESSAGE_ACCEPT)) {
			serverConnection.stop();
			throw new GameError("Server refused connection");
		}
		
		try {
			 gameFactory = (GameFactory) serverConnection.getObject();
			 localPiece = (Piece) serverConnection.getObject();
		} catch (Exception e) {
			 throw new GameError("Unknown server response: " + e.getMessage());
		}
	}

	@Override
	public void start() {
		addCloseClientConnectionObserver();
		
		this.gameOver = false;
		
		Thread t = new Thread() {
			
			@Override
			public void run() {
				while(!gameOver) {
					try {
						Response r = (Response) serverConnection.getObject();
						for(GameObserver o : observers) {
							r.run(o);
						}
					} catch(ClassNotFoundException | IOException e) {
						
					}
				}
			}
			
		};
		
		t.start();

	}
	
	private void addCloseClientConnectionObserver() {
		this.observers.add(new GameAdapter() {
			
			@Override
			public void onGameOver(Board board, State state, Piece winner) {
				if(state == State.Stopped) {
					try {
						GameClient.this.gameOver = true;
						GameClient.this.serverConnection.stop();
						GameClient.this.serverConnection = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	@Override
	public void addObserver(GameObserver o) {
		observers.add(o);
	}

	@Override
	public void removeObserver(GameObserver o) {
		observers.remove(o);
	}
}
