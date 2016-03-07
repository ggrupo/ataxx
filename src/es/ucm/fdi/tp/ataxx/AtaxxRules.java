package es.ucm.fdi.tp.ataxx;

import java.util.Iterator;
import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.Utils;
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
	private int obstacles;
	
	private static final Piece obs = new Piece("*");
	
	public AtaxxRules(int dim, int obstacles) {
		if(obstacles >= dim*dim) {
			throw new GameError("So many obstacles: " + obstacles);
		}
		if (dim < 5) {
			throw new GameError("Dimension must be at least 5: " + dim);
		} else {
			this.dim = dim;
			this.obstacles = Math.max(0, obstacles);
		}
	}
	
	@Override
	public String gameDesc() {
		return "Ataxx " + dim + "x" + dim;
	}

	@Override
	public Board createBoard(List<Piece> pieces) {
		FiniteRectBoard board = new FiniteRectBoard(dim, dim);
		
		Piece p = null;
		Iterator<Piece> iterator = pieces.iterator();
		
		//Esquinas superior izquierda e inferior derecha	
		p = iterator.next();
		board.setPieceCount(p, 2);
		board.setPosition(0, 0, p);
		board.setPosition(dim-1, dim-1, p);
		
		//Esquina superior derecha e inferior izquierda	
		p = iterator.next();
		board.setPieceCount(p, 2);
		board.setPosition(dim-1, 0, p);
		board.setPosition(0, dim-1, p);
			
		if(iterator.hasNext()) {
			p = iterator.next();
			//Centro superior y centro inferior
			board.setPieceCount(p, 2);
			board.setPosition(0, dim/2, p);
			board.setPosition(dim-1, dim/2, p);
			if(iterator.hasNext()) {
				p= iterator.next();
				// Centro izquierda y centro derecha
				board.setPieceCount(p, 2);
				board.setPosition(dim/2, 0, p);
				board.setPosition(dim/2, dim-1, p);
			}
		}
		constructObstacles(board, false, pieces);
		
		return board;
	}
	
	protected void constructObstacles(Board board, boolean simetrico, List<Piece> pieces) {
		if(obstacles >= (dim*dim - pieces.size() * 2)) {
			throw new GameError("So many obstacles: " + obstacles);
		}
		
		if(simetrico) {
			//TODO - Simetrico
		} else {
			int i = 0;
			int randRow, randCol;
			while(i<obstacles) {
				randRow = Utils.randomInt(dim);
				randCol = Utils.randomInt(dim);
				if(board.getPosition(randRow, randCol) == null) {
					board.setPosition(randRow, randCol, obs);
					i++;
				}
			}
		}
	}
	
	@Override
	public Piece initialPlayer(Board board, List<Piece> pieces) {
		return pieces.get(0);
	}

	@Override
	public int minPlayers() {
		return 2;
	}

	@Override
	public int maxPlayers() {
		return 4;
	}

	@Override
	public Pair<State, Piece> updateState(Board board, List<Piece> pieces,
			Piece turn) {
			int zeroes = 0;
			Piece winner = null;
			int nPiezas, maxPiezas = 0;
			boolean empate = false;
			
			for(Piece p : pieces) {
				nPiezas=board.getPieceCount(p);
				if(0 == nPiezas) {
					zeroes++;
				} else if(nPiezas > maxPiezas) {
					winner = p;
					maxPiezas = nPiezas;
					empate = false;
				} else if(nPiezas == maxPiezas) {
					empate = true;
				}
				
			}
			if(zeroes == pieces.size() - 1) {
				return new Pair<State,Piece>(State.Won,winner);
			}
			if(board.isFull()) {
				if(empate) {
					return new Pair<State,Piece>(State.Draw, null);
				}
				return new Pair<State,Piece>(State.Won, winner);
			}
			
			return new Pair<State, Piece>(State.InPlay, null);
	}

	@Override
	public Piece nextPlayer(Board board, List<Piece> pieces, Piece turn) {
		int i = pieces.indexOf(turn);
		return pieces.get((i + 1) % pieces.size());
	}

	@Override
	public double evaluate(Board board, List<Piece> pieces, Piece turn) {
		return 0;
	}

	@Override
	public List<GameMove> validMoves(Board board, List<Piece> playersPieces,
			Piece turn) {
		return null;
	}

}
