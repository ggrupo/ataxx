package es.ucm.fdi.tp.views.swing.boardpanel;

import java.awt.Color;
import java.awt.Graphics;

import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.views.swing.SwingView.PlayerMode;
import es.ucm.fdi.tp.views.swing.SwingView;

public class ConnectNBoard extends FiniteRectBoardComponent implements GameObserver {

	private static final long serialVersionUID = -4187463114811199234L;
	
	protected static final Color PIECE_BG_COLOR = new Color(153,153,153);
	protected static final Color PIECE_SHADOW_COLOR = new Color(51,51,51,100);
	
	private SwingView view;
	
	public ConnectNBoard(Controller c, Map<Piece,Color> colors, Map<Piece,PlayerMode> playerModes, SwingView v) {
		super(c,colors, playerModes);
		this.view = v;
	}


	@Override
	public void redraw() {
		this.repaint();
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
		int pieceSize = (int) (size* 0.8f);
		int shadowOffset = pieceSize/20 + 1;
		int piecePadding = (size - pieceSize)/2;
		
		x += piecePadding;
		y += piecePadding;
		
		Color pColor = pieceColors.get(p);
		
		try {
			
			//Paint piece shadow
			g.setColor(PIECE_SHADOW_COLOR);
			g.fillArc(x + shadowOffset, y + shadowOffset, pieceSize, pieceSize ,0,360);
			
			//Paint piece background
			g.setColor(pColor);
			g.fillArc(x, y, pieceSize, pieceSize ,0,360);
			
			//Paint piece border
			g.setColor(pColor.darker());
			g.drawArc(x, y, pieceSize, pieceSize, 0,360);
			
		} catch (NullPointerException e) {}
	}

	@Override
	protected void handlePieceRightClick(int i, int j) {}

	@Override
	protected void handlePieceLeftClick(int i, int j) {
		// TODO Make a move
		//new ErrorDialog("Left clicked piece " + i +", " + j, null);
		view.showMessage("You have selected(" + i + "," + j + ")");
	}

	@Override
	protected int getMargin() {
		return 4;
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
	public void onError(String msg) {
		// TODO Auto-generated method stub
		
	}
	
	public void onChangeTurn(final Board board, final Piece turn) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				handleChangeTurn(board, turn);
			}
		});
	}
	
	private void handleChangeTurn(Board board, Piece turn) {
		super.onChangeTurn(board, turn);
		view.showMessage("Click on a cell");
	}
	
	@Override
	public void onGameStart(final Board board, final String gameDesc, final List<Piece> pieces, final Piece turn) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				handleGameStart(board, gameDesc, pieces, turn);
			}
		});
	}
	
	private void handleGameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn) {
		super.onGameStart(board, gameDesc, pieces, turn);
		view.showMessage("Click on a cell");
	}


	@Override
	public void onPlayerModesChange(Piece player, PlayerMode newMode) {
		
	}
}
