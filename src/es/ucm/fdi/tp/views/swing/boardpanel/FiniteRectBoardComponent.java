package es.ucm.fdi.tp.views.swing.boardpanel;

import java.awt.Graphics;
import java.awt.Point;

import es.ucm.fdi.tp.basecode.bgame.model.Board;

public abstract class FiniteRectBoardComponent extends BoardComponent {

	private static final long serialVersionUID = 6531989076185688155L;
	
	/**
	 * Number of rows and columns
	 */
	private int rows;
	private int cols;
	
	/**
	 * Margins on top and left sides.
	 */
	private int vMargin;
	private int hMargin;
	
	/**
	 * Margin gap between cells.
	 */
	private int marginGap;
	
	private int pieceSize;	

	/**
	 * Creates an empty finite rect board component
	 */
	protected FiniteRectBoardComponent() {}
	
	@Override
	public void redraw(Board board) {
		this.rows = board.getRows();
		this.cols = board.getCols();
		this.marginGap = getMargin();
		this.repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		try {
			
			calcMetrics();
			
			for(int i=0, y=vMargin; i<rows; i++, y+=pieceSize + marginGap) {
				for(int j=0,x=hMargin; j<cols; j++, x+=pieceSize + marginGap) {
					paintPiece(g, i, j, x, y, pieceSize);
				}
			}
			
		} catch (RuntimeException e) {
			//Do not draw if any exception is found
			//Mostly arithmetic and null pointer exceptions due to
			//values don't initialize until on game start
		}
		
	}
	
	/**
	 * Returns the margin on top of the board. 
	 * It's the same as the bottom margin.
	 * @return vertical margin
	 */
	protected final int getMarginTop() {
		return this.vMargin;
	}
	
	/**
	 * Returns the right or left margin, they both are the same.
	 * @return horizontal margin
	 */
	protected final int getMarginLeft() {
		return this.hMargin;
	}
	
	/**
	 * Calculates marins and sizes for the board to be drawn correctly.
	 */
	private void calcMetrics() {
		this.pieceSize = Math.min((getWidth()-marginGap)/cols, (getHeight()-marginGap)/rows) - marginGap;
		
		this.vMargin = (getHeight() - (pieceSize+marginGap)*rows - marginGap )/2 + marginGap;
		this.hMargin = (getWidth() - (pieceSize+marginGap)*cols - marginGap )/2 + marginGap;
	}


	@Override
	protected final void handleRightClick(int x, int y) {
		Point piece = calculatePieceAt(x, y);
		if(piece != null)
			onRightClick(piece.y, piece.x);
	}


	@Override
	protected final void handleLeftClick(int x, int y) {
		Point piece = calculatePieceAt(x, y);
		if(piece != null)
			onLeftClick(piece.y, piece.x);
	}
	
	/**
	 * Calculates the board piece placed at x and y coordinates.
	 * @param x - x coordinate
	 * @param y - y coordinate
	 * @return Point in a hipothetical matrix with x coordinate representing i 
	 * position and y coordinate representing j position in the matrix.
	 */
	protected final Point calculatePieceAt(int x, int y) {
		if(x >= hMargin && (x < getWidth()-hMargin) &&
		   y >= vMargin && (y < getHeight()-vMargin)) {
			int i = (y - vMargin)/(pieceSize+marginGap);
			int j = (x - hMargin)/(pieceSize+marginGap);
			return new Point(j,i);
		}
		return null;
	}
	
	/**
	 * Fires when the board is right-clicked.
	 * @param i - row which was clicked 
	 * @param j - column which was clicked 
	 */
	protected abstract void onRightClick(int i, int j);
	
	/**
	 * Fires when the board is left-clicked.
	 * @param i - row which was clicked 
	 * @param j - column which was clicked 
	 */
	protected abstract void onLeftClick(int i, int j);
	
	/**
	 * Provides the margin gap between positions in the board.
	 * @return the size of the gap in pixels
	 */
	protected abstract int getMargin();
	
	/**
	 * Paints the piece at the given position in the board.
	 * Must not paint outside of the given bounds.
	 * @param g - graphics object used to paint
	 * @param i - piece's row in the board
	 * @param j - piece's column in the board
	 * @param x - piece's horizontal location on the canvas
	 * @param y - piece's vertical location on the canvas
	 * @param size - piece's size
	 */
	protected abstract void paintPiece(Graphics g, int i, int j, int x, int y, int size);
}
