package es.ucm.fdi.tp.ataxx;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.control.SwingPlayer;
import es.ucm.fdi.tp.views.swing.FiniteRectBoardSwingView;

import es.ucm.fdi.tp.views.swing.boardpanel.BoardComponent;
import es.ucm.fdi.tp.views.swing.boardpanel.FiniteRectBoardComponent;

/**
 * View class in the MVC for Ataxx game, powered by Java Swing widgets.
 * Will show a window containing the game divided in two main regions: 
 * the board and the control panel.<br/>
 * May be used as a single view for everyone or as a window per player (multiviews).
 */
public class AtaxxSwingView extends FiniteRectBoardSwingView implements GameObserver {
	
	private static final String CLICKED_ORIGIN_MESSAGE = "Click on one of your pieces";
	private static final String CLICKED_DESTINATION_MESSAGE = "Click on a destination cell";
	
	/**
	 * Board background color.
	 */
	protected static final Color BG_COLOR = new Color(0,0,102);
	
	/**
	 * Color of the grid lines and pieces border.
	 */
	protected static final Color LINE_COLOR = new Color(120,250,250);
	
	/**
	 * Color mask applied over piece color. Gives a retro appearance.
	 */
	private static final Color LINE_COLOR_MASK = new Color(0,153,255,51);
	
	/**
	 * Border line of a highlighted piece. 
	 * Pieces are highlighted when it's their turn.
	 */
	protected static final Color HIGHLIGHTED_LINE_COLOR = new Color(255,255,255);
	
	private FiniteRectBoardComponent boardComponent;
	
	private List<Piece> pieceList;
	private Piece turn;
	
	private SwingPlayer player;
	
	/**
	 * Status variable. May me used with {@link #origRow} and {@link #origCol}.
	 * true: already clicked in the past.
	 * false: not clicked in the past.
	 */
	private boolean clicked = false;
	
	/**
	 * Last clicked row and column.
	 */
	private int origRow, origCol;

	public AtaxxSwingView(Observable<GameObserver> g, Controller c, Piece localPiece, 
			Player randPlayer, Player aiPlayer) {
		super(g, c, localPiece, randPlayer, aiPlayer);
		this.player = new SwingPlayer();
	}

	@Override
	protected BoardComponent createBoard() {
		this.boardComponent = new FiniteRectBoardComponent() {
			
			private static final long serialVersionUID = 1901033550567275958L;
			
			//Override paintComponent just to draw a border arround the board
			@Override
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
					} else if(isPlayerPiece(piece)) { // if not an obstacle
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
				
				try {
					//Paint piece background
					g.setColor(getPieceColor(p));
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
			    
			    g.drawLine(x1, halfY, halfX, y1);	//Left to top
			    g.drawLine(halfX, y1, x2, halfY);	//Top to right
			    g.drawLine(x2, halfY, halfX, y2);	//Right to bottom
			    g.drawLine(halfX, y2, x1, halfY);	//Bottom to left
			}

			@Override
			public void onPlayerModesChange(Piece player, PlayerMode newMode) {
				if(clicked) {
					showMoveStartMessage();
					clicked = false;
				}
			}
			
			@Override
			public void onColorChange(Piece player, Color newColor) {
				redrawBoard();
			}
			
			@Override
			protected void onRightClick(int i, int j) {
				AtaxxSwingView.this.handleRightClick(i, j);
			}
			
			@Override
			protected void onLeftClick(int i, int j) {
				AtaxxSwingView.this.handleLeftClick(i, j);
			}
			
			@Override
			protected int getMargin() {
				return 0;
			}
			
		};
		
		boardComponent.setBackground(BG_COLOR);
		return boardComponent;
	}
	
	/**
	 * Handle what happens when a left click occurs on the board.
	 * @param i - clicked row
	 * @param j - clicked column
	 */
	protected void handleLeftClick(int i, int j) {
		if(clicked) {
			showDestinationClickedMessage(i, j);
			player.setMove(new AtaxxMove(origRow, origCol, i, j, turn));
			requestPlayerMove(player);
			clicked = false;
		} else {
			showOriginClickedMessage(i, j);
			clicked = true;
			origRow = i;
			origCol = j;
		}
		
	}
	
	/**
	 * Handle what happens when a right click occurs on the board.
	 * @param i - clicked row
	 * @param j - clicked column
	 */
	protected void handleRightClick(int i, int j) {
		if(clicked) {
			showMoveStartMessage();
			clicked = false;
		}
	}

	@Override
	public void onGameStart(final Board board, final String gameDesc, final List<Piece> pieces, final Piece turn) {
		super.onGameStart(board, gameDesc, pieces, turn);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				handleGameStart(board, gameDesc, pieces, turn);
			}
		});
		
	}
	
	private void handleGameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn) {
		this.pieceList = pieces;
		handleChangeTurn(board, turn);
	}
	
	@Override
	public void onChangeTurn(final Board board, final Piece turn) {
		super.onChangeTurn(board, turn);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				handleChangeTurn(board, turn);
			}
		});
	}
	
	private void handleChangeTurn(Board board, Piece turn) {
		this.turn = turn;
		if(getPlayerMode(turn) == PlayerMode.MANUAL) {
			showMoveStartMessage();
		}
	}

	/**
	 * Shows a message when the turn starts.
	 */
	protected void showMoveStartMessage() {
		showMessage(CLICKED_ORIGIN_MESSAGE);
	}
	
	/**
	 * Shows a message when the origin piece is clicked.
	 * @param i - clicked row
	 * @param j - clicked column
	 */
	protected void showOriginClickedMessage(int i, int j) {
		showMessage("You've selected (" + i + ", " + j + ") as origin");
		showMessage(CLICKED_DESTINATION_MESSAGE);
	}
	
	/**
	 * Shows a message when a destination cell was clicked.
	 * @param i - clicked row
	 * @param j - clicked column
	 */
	protected void showDestinationClickedMessage(int i, int j) {
		showMessage("You've selected (" + i + ", " + j + ") as destination");
		
	}

	@Override
	protected boolean isPlayerPiece(Piece p) {
		return pieceList.contains(p);
	}

}
