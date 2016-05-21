package es.ucm.fdi.tp.control.multiplayer.Responses;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

public class ChangeTurnResponse implements Response {

	private static final long serialVersionUID = -2783115179699093650L;

	private Board board;
	private Piece turn;
	
	public ChangeTurnResponse(Board b, Piece turn){
		this.board = b;
		this.turn = turn;
	}
	
	@Override
	public void run(GameObserver o) {
		o.onChangeTurn(board, turn);
	}

}
