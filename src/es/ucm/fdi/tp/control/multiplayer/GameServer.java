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
	
	static final String MESSAGE_REQUEST = "Connect";
	
	static final String MESSAGE_ACCEPT = "OK";
	
	private volatile List<Connection> clients;
	private volatile ServerSocket server;
	private final int port;
	
	private NetObserver view;
	
	private volatile boolean stopped;
	private volatile boolean gameOver;

	
	private final GameFactory gameFactorry;
	private final List<Piece> pieces;
	
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
		c.sendObject(MESSAGE_ACCEPT);
		c.sendObject(gameFactorry);
		Piece nPlayer = pieces.get(clients.size()-1);
		c.sendObject(nPlayer);
		
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
		
		view.onPlayerConnected(nPlayer);
		
		t.start();
		
	}
	
	public synchronized int playersConnected() {
		return clients.size();
	}
	
	public int requiredPlayers() {
		return pieces.size();
	}
	
	private void startServer()  {
		
		try {
			server = new ServerSocket(port);
			view.onServerOpened(game.gameDesc());
			stopped = false;
			
		} catch (IOException e) {
			System.err.println("Error opening the server: " + e.getMessage());
			stopServer();
		}
		
		Thread t = new Thread() {
			
			@Override
			public void run() {
				Socket soc;
				
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
				
				stopServer();
			}
		};
		
		t.start();
		
		
	}
	
	private synchronized void handleRequest(Socket s) {
		try {
			Connection c = new Connection(s);
			Object clientRequest = c.getObject();
			
			if(!(clientRequest instanceof String) ||
					!((String) clientRequest).equalsIgnoreCase(MESSAGE_REQUEST)) {
				c.sendObject(new GameError("Invalid server request"));
				c.stop();
				return;
			}
			if(clients.size() >= pieces.size()) {
				c.sendObject(new GameError("Room is already full"));
				c.stop();
				view.log("A client connection was refused: Maximum players connections reached. ");
				return;
			}
			
			clients.add(c);
			
			startClientListener(c);
			
			if(clients.size() == pieces.size())
				startGame();
			
			
		} catch (IOException | ClassNotFoundException e) {
			try {
				s.close();
			} catch (IOException e1) {}
			e.printStackTrace();
		}
	}
	
	private void startGame() {
		if(game.getState() == State.Starting)
			super.start();
		else
			restart();
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
			
		} catch(Exception e) {
			e.printStackTrace();
			
		}
		
		for(Connection c : clients) {
			try {
				c.stop();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		clients.clear();
		view.onGameStopped();
	}
	
	public synchronized void stopServer() {
		this.stopped = true;
		
		try {
			stop();
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			view.onServerClosed();
		}
	}
	
	@Override
	public synchronized  void start() {
		if(view == null)
			throw new NullPointerException(
					"Uninitialized view. You must call setView() first.");
		if(server != null)
			throw new RuntimeException("Server already started");
		
		startServer();
			
	}
	
	//Done
	private void forwardNotification(Response r) {
		for(Connection c : clients){
			try {
				c.sendObject(r);
			} catch (IOException e) {
				//TODO -> cool recursion idea here
				e.printStackTrace();
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
