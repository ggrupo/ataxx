package es.ucm.fdi.tp.ataxx;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

/**
 * A factory for creating ataxx games. See {@link AtaxxRules} for the
 * description of the game.
 */
public class AtaxxFactoryExt extends AtaxxFactory {

	private static final long serialVersionUID = 7903719868927465694L;
	
	/**
	 * Default constructor. Will create a 5x5 ataxx game with no obstacles.
	 */
	public AtaxxFactoryExt() {
		super();
	}
	
	/**
	 * Will create a dim x dim ataxx game with no obstacles.
	 * @param dim - board size. Number of rows/columns.
	 */
	public AtaxxFactoryExt(int dim) {
		super(dim);
	}
	
	/**
	 * Will create a dim x dim ataxx game with {@code obstacles} obstacles.
	 * @param dim - board size. Number of rows/columns.
	 * @param obstacles - number of obstacles
	 */
	public AtaxxFactoryExt(int dim, int obstacles) {
		super(dim,obstacles);
	}
	
	@Override
	public void createSwingView(final Observable<GameObserver> g, final Controller c, final Piece viewPiece,
			final Player random, final Player ai) {
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
    		@Override
			public void run() {
    			new AtaxxSwingView(g, c, viewPiece, random, ai);
			}
		});
		
	}
	
}
