package es.ucm.fdi.tp.ataxx;

import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.bgame.model.GameMove;


public class AtaxxMove extends GameMove {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected int orig_row;
	protected int orig_col;
	
	protected int dest_row;
	protected int dest_col;
	
	
	/**
	 * This constructor should be used ONLY to get an instance of
	 * {@link AtaxxMove} to generate game moves from strings by calling
	 * {@link #fromString(String)}
	 * 
	 * <p>
	 * Solo se debe usar este constructor para obtener objetos de
	 * {@link AtaxxMove} para generar movimientos a partir de strings usando
	 * el metodo {@link #fromString(String)}
	 * 
	 */

	public AtaxxMove() {
	}
	
	/**
	 * Constructs a move for placing a piece of the type referenced by {@code p}
	 * at position ({@code dest_row},{@code dest_col}) from ({@code orig_row},{@code orig_col}).
	 * 
	 * <p>
	 * Construye un movimiento para colocar una ficha del tipo referenciado por
	 * {@code p} en la posicion ({@code dest_row},{@code dest_col}) desde ({@code orig_row},{@code orig_col}).
	 * 
	 * @param orig_row
	 *            Number of origin row.
	 *            <p>
	 *            Numero de fila de origen.
	 * @param orig_col
	 *            Number of origin column.
	 *            <p>
	 *            Numero de columna de origen.
	 *            
	 * @param dest_col
	 *            Number of destination column.
	 *            <p>
	 *            Numero de columna de destino.
	 * @param dest_col
	 *            Number of destination column.
	 *            <p>
	 *            Numero de columna de destino.
	 * @param p
	 *            A piece to be place at ({@code dest_row},{@code dest_col}).
	 *            <p>
	 *            Ficha a colocar en ({@code dest_row},{@code dest_col}).
	 */
	public AtaxxMove(int orig_col, int orig_row, int dest_row, int dest_col, Piece p) {
		super(p);
		this.dest_row = dest_row;
		this.dest_col = dest_col;
		this.orig_row = orig_row;
		this.orig_col = orig_col;
	}
	
	@Override
	public void execute(Board board, List<Piece> pieces) {
		int radius = 0;
		Piece p = null;
		
		if (board.getPosition(orig_row, orig_col) == null) {
			throw new GameError("position (" + orig_row + "," + orig_col + ") is empty!");
		}
		if (board.getPosition(dest_row, dest_col) != null) {
			throw new GameError("position (" + dest_row + "," + dest_col + ") is already occupied!");
		}
		radius = radius();
		p = getPiece();
		if(2 == radius) {
			board.setPosition(dest_row, dest_col, p);
			board.setPosition(orig_row, orig_col, null);
		} else if(1 == radius) {
			board.setPosition(dest_row, dest_col, p);
			board.setPieceCount(p,board.getPieceCount(p) + 1);
		} else {
			throw new GameError("invalid move from (" + orig_row + "," + orig_col + ") to (" + dest_row + "," + dest_col + ")");
		}
		convertirArea(board);
	}

	private void convertirArea(Board board) {
		int minRow = Math.max(0, dest_row-1),
		    maxRow = Math.min(board.getRows()-1, dest_row+1);
		int minCol = Math.max(0, dest_col-1),
		    maxCol = Math.min(board.getCols()-1, dest_col+1);
		
		Piece p = getPiece();
		Piece q = null;
		
		for(int i = minRow; i<maxRow; i++){
			for(int j = minCol; j<maxCol; j++){
				q = board.getPosition(i,j);
				if(null != q) {
					board.setPieceCount(q, board.getPieceCount(q) - 1);
					board.setPieceCount(p, board.getPieceCount(p) + 1);
					board.setPosition(i, j, p);
				}
			}
		}
	}
	
	protected int radius() {
		return Math.max(Math.abs(dest_row - orig_row), Math.abs(dest_col - orig_col));
	}

	@Override
	public GameMove fromString(Piece p, String str) {
		String[] words = str.split(" ");
		if (words.length != 4) {
			return null;
		}

		try {
			int orig_row, orig_col, dest_row, dest_col;
			orig_row = Integer.parseInt(words[0]);
			orig_col = Integer.parseInt(words[1]);
			dest_row = Integer.parseInt(words[2]);
			dest_col = Integer.parseInt(words[3]);
			return new AtaxxMove(orig_row, orig_col, dest_row, dest_col, p);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	@Override
	public String help() {
		return "enter 'origin-row origin-column destination-row destination-column', to move a piece.";
	}
}