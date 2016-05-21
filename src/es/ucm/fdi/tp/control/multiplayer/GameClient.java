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
import es.ucm.fdi.tp.control.multiplayer.Responses.Response;

public class GameClient extends Controller implements Observable<GameObserver>{

	private String host;
	private int port;
	private Connection serverConnection;
	
	private List<GameObserver> observers;
	
	private Piece localPiece;
	private GameFactory gameFactory;
	
	private boolean gameOver;

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
		serverConnection.sendObject("Connect");
		Object response = serverConnection.getObject();
		if ((response instanceof Exception) && ((String)response).equalsIgnoreCase("OK")){
			throw (Exception) response;
		}
		try {
			 gameFactory = (GameFactory) response;
			 localPiece = (Piece) serverConnection.getObject();
		} catch (Exception e) {
			 throw new GameError("Unknown server response: " + e.getMessage());
		}
	}

	public void start() {
		addCloseClientConnectionObserver();
		
		this.gameOver = false;
		
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
	
	private void addCloseClientConnectionObserver() {
		this.observers.add(new GameObserver() {
			
			@Override
			public void onGameOver(Board board, State state, Piece winner) {
				GameClient.this.gameOver = true;
				try {
					GameClient.this.serverConnection.stop();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onGameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn) {}

			@Override
			public void onMoveStart(Board board, Piece turn) {}

			@Override
			public void onMoveEnd(Board board, Piece turn, boolean success) {}

			@Override
			public void onChangeTurn(Board board, Piece turn) {}

			@Override
			public void onError(String msg) {}
			
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
