package es.ucm.fdi.tp.control;

import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

public class SwingPlayer extends Player {

	private static final long serialVersionUID = -257722318128195740L;
	
	private GameMove move;
	
	public SwingPlayer() {}
	
	@Override
	public GameMove requestMove(Piece p, Board board, List<Piece> pieces, GameRules rules) {
		if(p.equals(move.getPiece()) {
			return this.move;
		}
		return null;
	}
	
	public void setMove(GameMove newMove) {
		this.move = move;
	}
	
	public GameMove getMove() {
		return this.move;
	}

}
