package es.ucm.fdi.tp.ataxx;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import java.util.Map;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.control.SwingPlayer;
import es.ucm.fdi.tp.views.swing.SwingView;
import es.ucm.fdi.tp.views.swing.SwingView.PlayerMode;
import es.ucm.fdi.tp.views.swing.boardpanel.FiniteRectBoardComponent;

public class AtaxxBoard extends FiniteRectBoardComponent implements GameObserver {

	private static final long serialVersionUID = -4187463114811199234L;
	
	protected static final Color BG_COLOR = new Color(0,0,102);
	protected static final Color LINE_COLOR = new Color(120,250,250);
	protected static final Color LINE_COLOR_MASK = new Color(0,153,255,51);
	protected static final Color HIGHLIGHTED_LINE_COLOR = new Color(255,255,255);
	
	private SwingView view;
	private List<Piece> pieceList;
	private Piece turn;
	
	private boolean clicked = false;
	private int origRow;
	private int origCol;
	
	private SwingPlayer player;
	
	public AtaxxBoard(SwingView view, Map<Piece,Color> colors, Map<Piece,PlayerMode> playerModes, SwingPlayer player) {
		super(colors, playerModes);
		this.setBackground(BG_COLOR);
		this.player = player;
		this.view = view;
	}

	@Override
	public void onGameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn) {
		super.onGameStart(board, gameDesc, pieces, turn);
		this.pieceList = pieces;
		this.turn = turn;
	}

	@Override
	public void onChangeTurn(Board board, Piece turn) {
		super.onChangeTurn(board, turn);
		this.turn = turn;
	}



	@Override
	public void redraw() {
		this.repaint();
	}
	
	//Override paintComponent to draw a border arround the board
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		paintBoardBorder(g);
	}
	
	private void paintBoardBorder(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		int margin = getMarginLeft();
		
		g.setColor(LINE_COLOR);
		
		if(width > height) {	//paint vertical borders
			margin = getMarginLeft();
			g.drawLine(margin, 0, margin, height);
			margin = width - margin;
			g.drawLine(margin, 0, margin, height);
		} else {				//paint horizontal borders
			margin = getMarginTop();
			g.drawLine(0, margin, width, margin);
			margin = height - margin;
			g.drawLine(0, margin, width, margin);
		}
	}

	@Override
	protected void paintPiece(Graphics g, int i, int j, int x, int y, int size) {
		Piece piece = getBoard().getPosition(i, j);
		
		try {
			if(piece == null) {
				paintBackgroundPiece(g, x, y, size);
			} else if(pieceList.contains(piece)) { // if not an obstacle
				paintPlayerPiece(g, piece, x, y, size);
			} else {
				paintObstacle(g, x, y, size);
			}
		} catch (NullPointerException e) {
			paintBackgroundPiece(g, x, y, size);
		}
	}

	private void paintPlayerPiece(Graphics g, Piece p, int x, int y, int size) {
		paintBackgroundPiece(g, x, y, size);
		
		int pieceSize = (int) (size* 0.5f);
		int piecePadding = (size - pieceSize)/2;
		x += piecePadding;
		y += piecePadding;
		
		Color pColor = pieceColors.get(p);
		try {
			//Paint piece background
			g.setColor(pColor);
			g.fillRect(x, y, pieceSize, pieceSize);
			
			//Green mask
			g.setColor(LINE_COLOR_MASK);
			g.fillRect(x, y, pieceSize, pieceSize);
			
			//Paint piece border
			if(p.equals(turn)) {
				g.setColor(HIGHLIGHTED_LINE_COLOR);
			} else {
				g.setColor(LINE_COLOR);
			}
			g.drawRect(x, y, pieceSize, pieceSize);
			
		} catch (NullPointerException e) {
			//Do nothing - background already painted
			//Will look as an empty position
		}
	}
	
	private void paintBackgroundPiece(Graphics g, int x, int y, int size) {
		int halfP = size / 2;
		g.setColor(LINE_COLOR);
		g.drawLine(x, y+halfP, x+size, y+halfP);
		g.drawLine(x+halfP, y, x+halfP, y+size);
	}
	
	private void paintObstacle(Graphics g, int x, int y, int size) {
		int half = size / 2;
		int corner = size / 4;
		
		g.setColor(LINE_COLOR);
		g.drawLine(x, y+half, x+corner, y+half);
		g.drawLine(x+half, y, x+half, y+corner);
		g.drawLine(x+size-corner, y+half, x+size, y+half);
		g.drawLine(x+half, y+size-corner, x+half, y+size);
		
		drawDiamond(g,x+corner,y+corner,x+size-corner,y+size-corner);
		
	}
	
	private void drawDiamond(Graphics g, int x1, int y1, int x2, int y2) {
	    int halfX = (x1+x2)/2;
	    int halfY = (y1+y2)/2;
	    
	    //Left to top
	    g.drawLine(x1, halfY, halfX, y1);
	    //Top to right
	    g.drawLine(halfX, y1, x2, halfY);
	    //Right to bottom
	    g.drawLine(x2, halfY, halfX, y2);
	    //Bottom to left
	    g.drawLine(halfX, y2, x1, halfY);
	}

	@Override
	protected void handlePieceRightClick(int i, int j) {
		clicked = false;
	}

	@Override
	protected void handlePieceLeftClick(int i, int j) {
		if(clicked) {
			view.showMessage(AtaxxSwingView.getDestinationClickedMessage(i, j));
			player.setMove(new AtaxxMove(origRow, origCol, i, j, turn));
			clicked = false;
		} else {
			view.showMessage(AtaxxSwingView.getOriginClickedMessage(i, j));
			clicked = true;
		}
		origRow = i;
		origCol = j;
	}

	@Override
	protected int getMargin() {
		return 0;
	}

	@Override
	public void onPlayerModesChange(Piece player, PlayerMode newMode) {
		clicked = false;
		view.showMessage(AtaxxSwingView.getMoveStartMessage());
	}

	@Override
	public void onError(String msg) {
		// TODO Auto-generated method stub
		
	}

}
