package es.ucm.fdi.tp.ataxx;

import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.basecode.bgame.model.FiniteRectBoard;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;
import es.ucm.fdi.tp.basecode.bgame.model.GameRules;
import es.ucm.fdi.tp.basecode.bgame.model.Pair;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

public class AtaxxRules implements GameRules {
	
	private int dim;
	
	public AtaxxRules(int dim) {
		if (dim < 5) {
			throw new GameError("Dimension must be at least 5: " + dim);
		} else {
			this.dim = dim;
		}
	}
	
	@Override
	public String gameDesc() {
		return "Ataxx " + dim + "x" + dim;
	}

	@Override
	public Board createBoard(List<Piece> pieces) {
		return new FiniteRectBoard(dim, dim);
	}

	@Override
	public Piece initialPlayer(Board board, List<Piece> pieces) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int minPlayers() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int maxPlayers() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Pair<State, Piece> updateState(Board board, List<Piece> pieces,
			Piece turn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Piece nextPlayer(Board board, List<Piece> pieces, Piece turn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double evaluate(Board board, List<Piece> pieces, Piece turn) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<GameMove> validMoves(Board board, List<Piece> playersPieces,
			Piece turn) {
		// TODO Auto-generated method stub
		return null;
	}

}
