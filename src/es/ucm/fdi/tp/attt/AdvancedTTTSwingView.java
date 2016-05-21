package es.ucm.fdi.tp.attt;

import java.util.List;

import es.ucm.fdi.tp.basecode.attt.AdvancedTTTMove;
import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.connectn.ConnectNSwingView;
import es.ucm.fdi.tp.control.SwingPlayer;
import es.ucm.fdi.tp.views.swing.SwingView;

/**
 * View class in the MVC for Advanced TicTacToe game, powered by Java Swing widgets.
 * Will show a window containing the game divided in two main regions: 
 * the board and the control panel.<br/>
 * May be used as a single view for everyone or as a window per player (multiviews).
 */
public class AdvancedTTTSwingView extends ConnectNSwingView {
	
	private static final String CLICKED_DESTINATION_MESSAGE = "Click on a destination cell";
	
	/**
	 * Player used as mcontainer of movements. It is passed to the controller.
	 */
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
	private int origRow = -1;
	private int origCol = -1;
	
	private Piece turn;

	/**
	 * Creates a window which shows the given game (which should be Advanced TocTacToe). 
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
	public AdvancedTTTSwingView(Observable<GameObserver> g, Controller c,
			Piece localPiece, Player randPlayer, Player aiPlayer) {
		super(g, c, localPiece, randPlayer, aiPlayer);
		player = new SwingPlayer();
	}
	
	@Override
	protected void handleRightClick(int i, int j) {
		this.clicked = false;
	}
	
	@Override
	protected void handleLeftClick(int i, int j) {
		if(getBoard().getPieceCount(turn) > 0) {	//Añadir pieza
			player.setMove(new AdvancedTTTMove(-1, -1, i, j, turn));
			requestPlayerMove(player);
		} else {									//Mover pieza
			decideMakePieceMove(i, j);
		}
	}
	
	private void decideMakePieceMove(int destRow, int destCol) {
		if(clicked) {
			player.setMove(new AdvancedTTTMove(origRow, origCol, destRow, destCol, turn));
			requestPlayerMove(player);
			clicked = false;
			origRow = -1;
			origCol = -1;
			showDestinationClickedMessage(destRow, destCol);
		} else {
			clicked = true;
			origRow = destRow;
			origCol = destCol;
			showOriginClickedMessage(destRow, destCol);
		}
	}
	
	@Override
	public void onGameStart(final Board board, final String gameDesc, final List<Piece> pieces, final Piece turn) {
		super.onGameStart(board, gameDesc, pieces, turn);
		this.turn = turn;
	}
	
	@Override
	public void onChangeTurn(final Board board, final Piece turn) {
		super.onChangeTurn(board, turn);
		this.turn = turn;
		this.clicked = false;
	}
	
	@Override
	public void onPlayerModesChange(Piece player, PlayerMode newMode) {
		super.onPlayerModesChange(player, newMode);
		this.clicked = false;
	}
	
	/**
	 * Shows a message when the origin piece has been clicked
	 */
	protected void showOriginClickedMessage(int i, int j) {
		showMessage("You've selected (" + i + ", " + j + ") as origin");
		showMessage(CLICKED_DESTINATION_MESSAGE);
	}
	
	/**
	 * Shows a message when the destination cell has been clicked.
	 */
	public void showDestinationClickedMessage(int i, int j) {
		showMessage("You've selected (" + i + ", " + j + ") as destination");
	}

}
