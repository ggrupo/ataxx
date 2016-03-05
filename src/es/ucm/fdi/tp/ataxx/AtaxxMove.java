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
