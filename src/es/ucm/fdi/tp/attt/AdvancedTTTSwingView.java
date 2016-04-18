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

public class AdvancedTTTSwingView extends ConnectNSwingView {

	private static final long serialVersionUID = -2594177504068542341L;
	
	//private static final String CLICK_ORIGIN = "Click on one of your pieces";
	private static final String CLICK_DESTINATION = "Click on a destination cell";
	
	private SwingPlayer player;
	private Piece turn;
	
	private boolean clicked = false;
	private int origRow = -1;
	private int origCol = -1;

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
	
	protected void showOriginClickedMessage(int i, int j) {
		showMessage("You've selected (" + i + ", " + j + ") as origin");
		showMessage(CLICK_DESTINATION);
	}
	
	public void showDestinationClickedMessage(int i, int j) {
		showMessage("You've selected (" + i + ", " + j + ") as destination");
	}

}
