package es.ucm.fdi.tp.views.swing.boardpanel;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Map;

import javax.swing.border.EmptyBorder;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.views.swing.boardpanel.pieces.BoardPiece;


public abstract class JFiniteRectBoard extends JBoard {

	private static final long serialVersionUID = 6531989076185688155L;
	
	final protected BoardPiece[][] casillas;
	
	private static final int mGap = 4;
	
	protected int rows;
	protected int cols;
	

	public JFiniteRectBoard(Board board, Controller c, Map<Piece,Color> colors) {
		super(new GridLayout(board.getRows(),board.getCols(),mGap,mGap),c, colors);
		this.setBorder(new EmptyBorder(mGap,mGap,mGap,mGap));
		
		this.rows = board.getRows();
		this.cols = board.getCols();
		this.casillas = new BoardPiece[rows][cols];

	}
	
	@Override
	public void onColorChange(Piece player, Color newColor) {
		repaint();
	}

	@Override
	public void disable() {
		for(int i=0; i<rows; i++) {
			for(int j=0; j<cols; j++) {
				casillas[i][j].setEnabled(false);
			}
		}
	}

	@Override
	public void enable() {
		for(int i=0; i<rows; i++) {
			for(int j=0; j<cols; j++) {
				casillas[i][j].setEnabled(true);
			}
		}
	}
	
}
