package es.ucm.fdi.tp.views.swing;


import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;

import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

/**
 * A game view class representing all the rect board games. Extends this if you
 * are creating a bord game like chess or Tic-Tac-Toe.
 *
 */
public abstract class FiniteRectBoardSwingView extends SwingView implements GameObserver {

	public FiniteRectBoardSwingView(Observable<GameObserver> g, Controller c, 
			Piece localPiece, Player randPlayer, Player aiPlayer)
	{
		super(g, c, localPiece, randPlayer, aiPlayer);
	}
	
	/**
	 * Should return wether the given piece is a player piece or not.
	 * For example and obstacle
	 * @param p the piece
	 * @return true if it's a player piece
	 */
	protected abstract boolean isPlayerPiece(Piece p);

}
