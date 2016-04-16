package es.ucm.fdi.tp.ataxx;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.connectn.ConnectNSwingView;

public class AtaxxFactoryExt extends  AtaxxFactory{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7903719868927465694L;
	@Override
	public void createSwingView(final Observable<GameObserver> g, final Controller c, final Piece viewPiece,
			final Player random, final Player ai) {
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
    		@Override
			public void run() {
    			new AtaxxSwingView(g, c, viewPiece, random, ai).setVisible(true);
			}
		});
		
	}
	
	public AtaxxFactoryExt() {
		super();
	}
	
	public AtaxxFactoryExt(int dim) {
		super(dim);
	}
}
