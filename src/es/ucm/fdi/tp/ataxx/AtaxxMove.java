package es.ucm.fdi.tp.ataxx;

import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.connectN.ConnectNMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;


public class AtaxxMove extends GameMove {

	protected int orig_row;
	protected int orig_col;
	
	protected int dest_row;
	protected int dest_col;
	
	@Override
	public void execute(Board board, List<Piece> pieces) {
		if (board.getPosition(dest_row, dest_col) == null) {
			board.setPosition(dest_row, dest_col, getPiece());
		} else {
			throw new GameError("position (" + dest_row + "," + dest_col + ") is already occupied!");
		}
	}
	
	private short radius() {
		if(Math.abs(dest_row - orig_row) > 2 || Math.abs(dest_col - orig_col) > 2) {
			return 3;
		} else if(Math.abs(dest_row - orig_row) == 2 || Math.abs(dest_col - orig_col) == 2) {
			return 2;
		} else if(Math.abs(dest_row - orig_row) == 1 || Math.abs(dest_col - orig_col) == 1) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public GameMove fromString(Piece p, String str) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String help() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
