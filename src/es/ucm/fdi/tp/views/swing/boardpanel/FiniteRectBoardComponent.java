package es.ucm.fdi.tp.views.swing.boardpanel;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import java.util.Map;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;


public abstract class FiniteRectBoardComponent extends BoardComponent implements GameObserver {

	private static final long serialVersionUID = 6531989076185688155L;
	
	private int rows;
	private int cols;
	
	private int vMargin = 0;
	private int hMargin = 0;

	private int pieceSize;
	
	private final int MARGIN_GAP;
	

	protected FiniteRectBoardComponent(Controller c, Map<Piece,Color> colors) {
		super(c, colors);
		this.MARGIN_GAP = getMargin();
	}
	
	@Override
	public void redraw() {
		this.repaint();
	}
	
	@Override
	final public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		calcMetrics();
		
		for(int i=0, y=vMargin; i<rows; i++, y+=pieceSize + MARGIN_GAP) {
			for(int j=0,x=hMargin; j<cols; j++, x+=pieceSize + MARGIN_GAP) {
				paintPiece(g, i, j, x, y, pieceSize);
			}
		}
		
	}
	
	private void calcMetrics() {
		calcPieceSize();
		centerBoard();
	}
	
	private void centerBoard() {
		this.vMargin = (getHeight() - (pieceSize+MARGIN_GAP)*rows -MARGIN_GAP )/2 + MARGIN_GAP;
		this.hMargin = (getWidth() - (pieceSize+MARGIN_GAP)*cols -MARGIN_GAP )/2 + MARGIN_GAP;
	}
	
	private void calcPieceSize() {
		this.pieceSize = Math.min((getWidth()-MARGIN_GAP)/cols, (getHeight()-MARGIN_GAP)/rows) - MARGIN_GAP;
	}
	
	@Override
	public void onGameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn) {
		super.onGameStart(board, gameDesc, pieces, turn);
		this.rows = board.getRows();
		this.cols = board.getCols();
	}


	@Override
	protected final void handleRightClick(int x, int y) {
		if(x >= hMargin && (x < getWidth()-hMargin) &&
		   y >= vMargin && (y < getHeight()-vMargin)) {
			int i = (y - vMargin)/(pieceSize+MARGIN_GAP);
			int j = (x - hMargin)/(pieceSize+MARGIN_GAP);
			handlePieceRightClick(i, j);
		}
	}


	@Override
	protected final void handleLeftClick(int x, int y) {
		if(x >= hMargin && (x < getWidth()-hMargin) &&
		   y >= vMargin && (y < getHeight()-vMargin)) {
			int i = (y - vMargin)/(pieceSize+MARGIN_GAP);
			int j = (x - hMargin)/(pieceSize+MARGIN_GAP);
			handlePieceLeftClick(i, j);
		}
	}
	
	/**
	 * Fires when the board is right-clicked.
	 * @param i - row which was clicked 
	 * @param j - column which was clicked 
	 */
	protected abstract void handlePieceRightClick(int i, int j);
	
	/**
	 * Fires when the board is left-clicked.
	 * @param i - row which was clicked 
	 * @param j - column which was clicked 
	 */
	protected abstract void handlePieceLeftClick(int i, int j);
	
	/**
	 * Provides the margin gap between positions in the board.
	 * @return the size of the gap in pixels
	 */
	protected abstract int getMargin();
	
	/**
	 * Paints the piece at the given position in the board.
	 * @param g - graphics object used to paint
	 * @param i - piece's row in the board
	 * @param j - piece's column in the board
	 * @param x - piece's horizontal location on the canvas
	 * @param y - piece's vertical location on the canvas
	 * @param size - piece's size
	 */
	protected abstract void paintPiece(Graphics g, int i, int j, int x, int y, int size);
}
