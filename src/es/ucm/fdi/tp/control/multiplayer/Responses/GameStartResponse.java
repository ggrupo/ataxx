package es.ucm.fdi.tp.control.multiplayer.Responses;

import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

public class GameStartResponse implements Response {
	
	private static final long serialVersionUID = -5606851848243655248L;
	
	
	private Board board;
	private String gameDesc;
	private List<Piece> pieces;
	private Piece turn;
	
	public GameStartResponse(Board b, String gameDesc, List<Piece> pieces, Piece turn) {
		this.board = b;
		this.gameDesc = gameDesc;
		this.pieces = pieces;
		this.turn = turn;
	}
	
	@Override
	public void run(GameObserver o) {
		o.onGameStart(board, gameDesc, pieces, turn);
	}

}
