package es.ucm.fdi.tp.connectn;

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
import es.ucm.fdi.tp.basecode.connectn.ConnectNMove;
import es.ucm.fdi.tp.control.SwingPlayer;
import es.ucm.fdi.tp.views.swing.FiniteRectBoardSwingView;
import es.ucm.fdi.tp.views.swing.SwingView;
import es.ucm.fdi.tp.views.swing.boardpanel.BoardComponent;
import es.ucm.fdi.tp.views.swing.boardpanel.FiniteRectBoardComponent;

/**
 * View class in the MVC for ConnectN game, powered by Java Swing widgets.
 * Will show a window containing the game divided in two main regions: 
 * the board and the control panel.<br/>
 * May be used as a single view for everyone or as a window per player (multiviews).
 */
public class ConnectNSwingView extends FiniteRectBoardSwingView implements GameObserver {
	
	/**
	 * Cells' background color
	 */
	protected static final Color PIECE_BG_COLOR = new Color(153,153,153);
	
	/**
	 * Pieces' shadow color
	 */
	protected static final Color PIECE_SHADOW_COLOR = new Color(51,51,51,100);
	
	private Piece turn;
	
	/**
	 * Player used as mcontainer of movements. It is passed to the controller.
	 */
	private SwingPlayer player;

	/**
	 * Creates a window which shows the given game (which should be ConnectN). 
	 * It is also able to send responses to the game through the controller.
	 * 
	 * Give a value to localPiece if you want one player's perspective or null.
	 * You also may want to create multiple {@linkplain SwingView} if you have
	 * multiple players.
	 * @param g - Game model observable. Needed to recieve game events.
	 * @param c - Game controller
	 * @param localPiece - Player which will own this window, null if 
	 * everyone plays in the same window.
	 * @param randPlayer - Random player if supported or null.
	 * @param aiPlayer - AI player if supported or null.
	 */
	public ConnectNSwingView(Observable<GameObserver> g, Controller c, Piece localPiece, 
			Player randPlayer, Player aiPlayer) 
	{
		super(g, c, localPiece, randPlayer, aiPlayer);
		this.player = new SwingPlayer();
	}
	
	@Override
	protected BoardComponent createBoard() {
		return new FiniteRectBoardComponent() {
			
			private static final long serialVersionUID = -8184304331622622656L;
			
			@Override
			protected void paintPiece(Graphics g, int i, int j, int x, int y, int size) {
				Piece piece = getBoard().getPosition(i, j);
				
				g.setColor(PIECE_BG_COLOR);
				g.fillRect(x, y, size, size);
				
				if(piece != null) {
					paintPlayerPiece(g, piece, x, y, size);
				}
			}
			
			/**
			 * Paints a piece corresponding with a player
			 */
			private void paintPlayerPiece(Graphics g, Piece p, int x, int y, int size) {
				int pieceSize = (int) (size* 0.8f);
				int shadowOffset = pieceSize/20 + 1;
				int piecePadding = (size - pieceSize)/2;
				
				x += piecePadding;
				y += piecePadding;
				
				Color pColor = getPieceColor(p);
				
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
			public void onPlayerModesChange(Piece player, PlayerMode newMode) {}
			
			@Override
			public void onColorChange(Piece player, Color newColor) {
				redrawBoard();
			}
			
			
			@Override
			protected void onRightClick(int i, int j) {
				ConnectNSwingView.this.handleRightClick(i,j);
			}
			
			@Override
			protected void onLeftClick(int i, int j) {
				ConnectNSwingView.this.handleLeftClick(i, j);
			}
			
			@Override
			protected int getMargin() {
				return 4;
			}
		};
	}
	
	/**
	 * Handles what happens when the board is right clicked.
	 * @param i - clicked row
	 * @param j - clicked column
	 */
	protected void handleRightClick(int i, int j) {}

	/**
	 * Handles what happens when the board is left clicked.
	 * @param i - clicked row
	 * @param j - clicked column
	 */
	protected void handleLeftClick(int i, int j) {
		showMessage("You have selected(" + i + "," + j + ")");
		if(isPieceTurn(turn)){
			player.setMove(new ConnectNMove(i, j, turn));
			requestPlayerMove(player);
		};
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
		handleChangeTurn(board, turn);
	}

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
		if(getPlayerMode(turn) == PlayerMode.MANUAL)
			showMessage("Click on a cell");
	}

	@Override
	protected boolean isPlayerPiece(Piece p) {
		return true;
	}
}
