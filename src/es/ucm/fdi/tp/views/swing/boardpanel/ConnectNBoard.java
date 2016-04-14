package es.ucm.fdi.tp.views.swing.boardpanel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.util.List;
import java.util.Map;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

public class ConnectNBoard extends FiniteRectBoardComponent implements GameObserver {

	private static final long serialVersionUID = -4187463114811199234L;
	
	public static final Color PIECE_BG_COLOR = new Color(153,153,153);
	//public static final Color PIECE_HOVER_BG_COLOR = new Color(204,204,204);
	
	public ConnectNBoard(Controller c, Map<Piece,Color> colors) {
		super(c,colors);
	}

	@Override
	public void onGameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn) {
		super.onGameStart(board, gameDesc, pieces, turn);
		// TODO Auto-generated method stub
	}

	@Override
	public void onGameOver(Board board, State state, Piece winner) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMoveStart(Board board, Piece turn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMoveEnd(Board board, Piece turn, boolean success) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onChangeTurn(Board board, Piece turn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(String msg) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void redraw() {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void paintPiece(Graphics g, int i, int j, int x, int y, int size) {
		Piece piece = getBoard().getPosition(i, j);
		
		g.setColor(PIECE_BG_COLOR);
		g.fillRect(x, y, size, size);
		
		if(piece != null) {
			paintPlayerPiece(g, piece, x, y, size);
		}
	}
	
	private void paintPlayerPiece(Graphics g, Piece p, int x, int y, int size) {
		Graphics2D g2d = (Graphics2D) g;
		int pieceSize = (int) (size* 0.8f);
		int pieceMargin = (size - pieceSize)/2;
		
		Color pColor = pieceColors.get(p);
		
		try {
			g2d.setColor(pColor);
			g2d.fillArc(x + pieceMargin, y + pieceMargin, pieceSize, pieceSize ,0,360);
			
			g2d.setColor(pColor.darker());
			g2d.setStroke(new BasicStroke(4));
			g2d.drawArc(x + pieceMargin, y + pieceMargin, pieceSize, pieceSize, 0,360);
		} catch (NullPointerException e) {}
	}

	@Override
	protected void handlePieceRightClick(int i, int j) {}

	@Override
	protected void handlePieceLeftClick(int i, int j) {
		// TODO Make a move
		System.out.println("Left clicked piece " + i +", " + j);
	}

	@Override
	protected int getMargin() {
		return 4;
	}

}
