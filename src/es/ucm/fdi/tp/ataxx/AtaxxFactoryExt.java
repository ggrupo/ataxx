package es.ucm.fdi.tp.ataxx;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

public class AtaxxFactoryExt extends AtaxxFactory {

	private static final long serialVersionUID = 7903719868927465694L;
	
	public AtaxxFactoryExt() {
		super();
	}
	
	public AtaxxFactoryExt(int dim) {
		super(dim);
	}
	
	public AtaxxFactoryExt(int dim, int obstacles) {
		super(dim,obstacles);
	}
	
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
	
}
