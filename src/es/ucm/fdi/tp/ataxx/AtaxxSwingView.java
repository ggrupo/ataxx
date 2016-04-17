package es.ucm.fdi.tp.ataxx;

import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.control.PlayerObserver;
import es.ucm.fdi.tp.control.SwingPlayer;
import es.ucm.fdi.tp.views.swing.FiniteRectBoardSwingView;
import es.ucm.fdi.tp.views.swing.boardpanel.BoardComponent;

public class AtaxxSwingView extends FiniteRectBoardSwingView implements GameObserver, PlayerObserver {

	private static final long serialVersionUID = 1836234838652616662L;
	
	private static final String CLICK_ORIGIN = "Click on one of your pieces";
	private static final String CLICK_DESTINATION = "Click on a destination cell";
	
	private Map<Piece,PlayerMode> playerModes;
	
	private SwingPlayer player;

	public AtaxxSwingView(Observable<GameObserver> g, Controller c, Piece localPiece, 
			Player randPlayer, Player aiPlayer) {
		super(g, c, localPiece, randPlayer, aiPlayer);
		this.playerModes = getPlayerModes();
		this.player = new SwingPlayer(this);
	}

	@Override
	protected BoardComponent createBoard() {
		return new AtaxxBoard(this, getPlayerColors(), getPlayerModes(), player);
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
		if(playerModes.get(turn) == PlayerMode.MANUAL) {
			this.showMessage(getMoveStartMessage());
		}
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
		if(playerModes.get(turn) == PlayerMode.MANUAL) {
			this.showMessage(getMoveStartMessage());
		}
	}

	@Override
	public void onMovementSet() {
		requestPlayerMove(player);
	}
	
	public static String getMoveStartMessage() {
		return CLICK_ORIGIN;
	}
	
	public static String getOriginClickedMessage(int i, int j) {
		return "You have selected (" + i + ", " + j + ") as origin. " + CLICK_DESTINATION;
	}
	
	public static String getDestinationClickedMessage(int i, int j) {
		return "You have selected (" + i + ", " + j + ") as destination";
		
	}
	
}
