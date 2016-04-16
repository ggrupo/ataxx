package es.ucm.fdi.tp.ttt;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.ttt.TicTacToeFactory;

public class TicTacToeFactoryExt extends TicTacToeFactory {

	private static final long serialVersionUID = -3391480144182205704L;

	public void createSwingView(final Observable<GameObserver> g, final Controller c, final Piece viewPiece,
			final Player random, final Player ai) {
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
    		@Override
			public void run() {
    			new TicTacToeSwingView(g, c, viewPiece, random, ai).setVisible(true);
			}
		});
	}
}
