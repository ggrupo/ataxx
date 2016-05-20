package es.ucm.fdi.tp.control.multiplayer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.sun.istack.internal.NotNull;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.GameFactory;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.control.commands.Command;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.control.multiplayer.Responses.*;

public class GameServer extends Controller implements GameObserver {
	
	private List<Connection> clients;
	private ServerSocket server;
	private int port;
	
	private NetObserver view;
	
	private boolean stopped;
	private boolean gameOver;
	
	public static int REQUIRED_PLAYERS = 4;
	
	private int nPlayers = 0;
	private GameFactory gameFactorry;
	private List<Piece> pieces;
	
	public GameServer(GameFactory factory, List<Piece> pieces, int port) {
		super(new Game(factory.gameRules()), pieces);
		this.clients = new ArrayList<Connection>();
		
		this.gameFactorry = factory;
		this.pieces = pieces;
		
		this.port = port;
		
		game.addObserver(this);
	}
	
	private void startClientListener(Connection c) throws IOException {
		this.gameOver = false;
		c.sendObject(gameFactorry);
		c.sendObject(pieces);
		Thread t = new Thread() {
			@Override
			public void run() {
				while (!stopped && !gameOver) {
					try {
						Command cmd = (Command) c.getObject();
						cmd.execute(GameServer.this);
					} catch (ClassNotFoundException | ClassCastException | IOException e) {
						if (!stopped && !gameOver) {
							GameServer.this.stop();
						}
					}
				}
			}
		};
		
		t.start();
		
		view.onPlayerConnected(pieces.get(nPlayers-1));
	}
	
	public int playersConnected() {
		return nPlayers;
	}
	
	private void startServer()  {
		Socket soc;
		try {
			this.server = new ServerSocket(port);
			view.onServerOpened(game.gameDesc());
			this.stopped = false;
			
		} catch (IOException e) {
			view.log("Error opening the server");
			System.err.println("Error opening the server: " + e.getMessage());
			view.onServerClosed();
			this.stopped = true;
		}
		
		this.stopped = false;
		
		while(!stopped) {
			try {
				soc = server.accept();
				handleRequest(soc);
				
			} catch (IOException e) {
				if(!stopped){
					view.log("Error while waiting for a connection");
					System.err.println("Error while waiting for a connection: " + e.getMessage());
				}
			}
		}
	}
	
	private void handleRequest(Socket s) {
		try {
			Connection c = new Connection(s);
			Object clientRequest = c.getObject();
			
			if(!(clientRequest instanceof String) ||
					!((String) clientRequest).equalsIgnoreCase("Connect") ) {
				c.sendObject(new GameError("Invalid Request"));
				c.stop();
				return;
			}
			if(nPlayers >= REQUIRED_PLAYERS) {
				c.sendObject(new GameError("Room is already full"));
				c.stop();
				return;
			}
			
			nPlayers++;
			clients.add(c);
			if(nPlayers >= REQUIRED_PLAYERS)
				startGame();
			
			startClientListener(c);
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void startGame() {
		if(game.getState() == State.Starting)
			game.start(pieces);
		else
			game.restart();
	}
	
	public void setView(@NotNull NetObserver o) {
		this.view = o;
	}
	
	@Override
	public synchronized void makeMove(Player p) {
		try {
			super.makeMove(p);
		} catch (GameError e) {
			view.log(e.getMessage());
		}
	}
	
	@Override
	public synchronized void restart() {
		try {
			super.restart();
		} catch (GameError e) {
			view.log(e.getMessage());
		}
	}
	
	@Override
	public synchronized void stop() {
		try {
			this.gameOver = true;
			super.stop();
			
		} catch (GameError e) {
			view.log(e.getMessage());
		} catch(Exception e) {
			e.printStackTrace();
			
		} finally {
			for(Connection c : clients) {
				try {
					c.stop();
				} catch(IOException e) { e.printStackTrace(); }
			}
		}
		view.onServerClosed();
	}
	
	@Override
	public void start() {
		if(view != null)
			startServer();
		else
			throw new NullPointerException(
					"Uninitialized view. You must call setView() first.");
	}
	
	//Done
	private void forwardNotification(Response r) {
		for(Connection c : clients){
			try {
				c.sendObject(r);
			} catch (IOException e) {
				clients.remove(c);
			}
		}
	}
	
	@Override
	public void onGameStart(Board board, String gameDesc, List<Piece> pieces,
			Piece turn) {
		forwardNotification(new GameStartResponse(board, gameDesc, pieces, turn));
	}

	@Override
	public void onGameOver(Board board, State state, Piece winner) {
		forwardNotification(new GameOverResponse(board, state, winner));
	}

	@Override
	public void onMoveStart(Board board, Piece turn) {
		forwardNotification(new MoveStartResponse(board, turn));
		
	}

	@Override
	public void onMoveEnd(Board board, Piece turn, boolean success) {
		forwardNotification(new MoveEndResponse(board, turn, success));
	}

	@Override
	public void onChangeTurn(Board board, Piece turn) {
		forwardNotification(new ChangeTurnResponse(board, turn));
	}

	@Override
	public void onError(String msg) {
		forwardNotification(new ErrorResponse(msg));
	}
	
}
