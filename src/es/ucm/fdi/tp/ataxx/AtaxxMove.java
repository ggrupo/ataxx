package es.ucm.fdi.tp.ataxx;

import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.connectN.ConnectNMove;

public class AtaxxMove extends ConnectNMove {

	private int origen_row;
	
	private int origen_col;
	
	@Override
	public void execute(Board board, List<Piece> pieces) {
		if (board.getPosition(row, col) == null) {
			board.setPosition(row, col, getPiece());
		} else {
			throw new GameError("position (" + row + "," + col + ") is already occupied!");
		}
	}
	
	private short radius() {
		if(Math.abs(row - origen_row) > 2 || Math.abs(col - origen_col) > 2) {
			return 3;
		} else if(Math.abs(row - origen_row) == 2 || Math.abs(col - origen_col) == 2) {
			return 2;
		} else if(Math.abs(row - origen_row) == 1 || Math.abs(col - origen_col) == 1) {
			return 1;
		} else {
			return 0;
		}
	}
	
}
