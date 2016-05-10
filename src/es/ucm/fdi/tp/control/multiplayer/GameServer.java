package es.ucm.fdi.tp.control.multiplayer;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.control.multiplayer.Responses.*;

public class GameServer extends Controller implements GameObserver {
	
	private List<Connection> connections;
	private ServerSocket server;
	private int port;
	
	private boolean stopped;
	
	public GameServer(Game game, List<Piece> pieces, int port) {
		super(game, pieces);
		this.connections = new ArrayList<Connection>();
		game.addObserver(this);
		this.port = port;
	}
	
	private void startClientListener(Connection c) {
		
	}
	
	private void log(String msg) {
		
	}
	
	private void startServer() throws IOException {
		this.server = new ServerSocket(port);
		
		this.stopped = false;
		
		while(!stopped) {
			try {
			} catch (Exception e) {
				if(!stopped){
					log("Error while waiting for a connection: " + e.getMessage());
				}
			}
		}
	}

	private void forwardNotification(Response r) {
		for(Connection c : connections){
			try {
				c.sendObject(r);
			} catch (IOException e) {
				connections.remove(c);
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
