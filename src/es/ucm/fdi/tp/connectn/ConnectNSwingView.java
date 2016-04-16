package es.ucm.fdi.tp.connectn;

import java.util.Map;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;

import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.views.swing.FiniteRectBoardSwingView;
import es.ucm.fdi.tp.views.swing.SwingView.PlayerMode;
import es.ucm.fdi.tp.views.swing.boardpanel.ConnectNBoard;
import es.ucm.fdi.tp.views.swing.boardpanel.BoardComponent;

public class ConnectNSwingView extends FiniteRectBoardSwingView implements GameObserver {

	private static final long serialVersionUID = -4401832872659565879L;

	public ConnectNSwingView(Observable<GameObserver> g, Controller c, Piece localPiece, Player randPlayer,
			Player aiPlayer) {
		super(g, c, localPiece, randPlayer, aiPlayer);
		
		
	}
	
	@Override
	protected BoardComponent createBoard() {
		return new ConnectNBoard(cntrl, pieceColors, playerModes);
	}

	@Override
	public void setEnabled(boolean b) {
		// TODO Auto-generated method stub
		
	}

}
