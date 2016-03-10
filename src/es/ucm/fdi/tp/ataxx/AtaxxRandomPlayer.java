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

		List<GameMove> moves = rules.validMoves(board, pieces, p);
		if(null == moves || moves.isEmpty()) {
			throw new GameError("Piece " + p.getId() + " cannot make any move!");
		}
		Integer random = Utils.randomInt(moves.size());
		return moves.get(random);
	}
}