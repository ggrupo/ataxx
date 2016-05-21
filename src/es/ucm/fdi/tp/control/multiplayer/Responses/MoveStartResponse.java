package es.ucm.fdi.tp.control.multiplayer.Responses;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

public class MoveStartResponse implements Response {

	private static final long serialVersionUID = 3154876708001474258L;
	
	private Board board;
	private Piece turn;
	
	public MoveStartResponse(Board board, Piece turn) {
		this.board = board;
		this.turn = turn;
	}
	
	@Override
	public void run(GameObserver o) {
		o.onMoveStart(board, turn);
	}

}
