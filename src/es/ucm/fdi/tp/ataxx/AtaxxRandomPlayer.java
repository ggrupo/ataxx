package es.ucm.fdi.tp.ataxx;

import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.Utils;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Pair;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

/**
 * A random player for ConnectN.
 * 
 * <p>
 * Un jugador aleatorio para ConnectN.
 *
 */
public class AtaxxRandomPlayer extends Player {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public GameMove requestMove(Piece p, Board board, List<Piece> pieces, GameRules rules) {
		if (board.isFull()) {
			throw new GameError("The board is full, cannot make a random move!!");
		}

		int rows = board.getRows();
		int cols = board.getCols();

		// pick an initial random position
		int currRow = Utils.randomInt(rows);
		int currCol = Utils.randomInt(cols);
		
		Pair<Integer,Integer> pos = null;
		
		// start at (currRow,currColl) and look for the first empty position.
		while (true) {
			if (board.getPosition(currRow, currCol) == null) {
				pos = surroundings(board, currRow, currCol, p);
				if(pos != null) {
					return new AtaxxMove(pos.getFirst(), pos.getSecond(), currRow, currCol, p);
				}
			}
			currCol = (currCol + 1) % cols;
			if (currCol == 0) {
				currRow = (currRow + 1) % rows;
			}
		}

	}

	private static Pair<Integer,Integer> surroundings(Board board, int row, int col, Piece p) {
		int minRow = Math.max(0, row-2),
		    maxRow = Math.min(board.getRows()-1, row+2);
		int minCol = Math.max(0, col-2),
		    maxCol = Math.min(board.getCols()-1, col+2);
		
		
		for(int i = minRow; i<=maxRow; i++){
			for(int j = minCol; j<=maxCol; j++){
				if(p.equals(board.getPosition(i,j))) {
					return new Pair<Integer,Integer>(i,j);
				}
			}
		}
		return null;
	}
}