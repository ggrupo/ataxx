package es.ucm.fdi.tp.views.swing;


import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;

import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;


public abstract class FiniteRectBoardSwingView extends SwingView implements GameObserver {

	public FiniteRectBoardSwingView(Observable<GameObserver> g, Controller c, 
			Piece localPiece, Player randPlayer, Player aiPlayer)
	{
		super(g, c, localPiece, randPlayer, aiPlayer);
	}
	
	protected abstract boolean isPlayerPiece(Piece p);

}
