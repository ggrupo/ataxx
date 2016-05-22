package es.ucm.fdi.tp.connectn;

import java.lang.reflect.InvocationTargetException;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.connectn.ConnectNFactory;

@SuppressWarnings("serial")
public class ConnectNFactoryExt extends ConnectNFactory {
	@Override
	public void createSwingView(final Observable<GameObserver> g, final Controller c, final Piece viewPiece,
			final Player random, final Player ai) {
		
		try {
			javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					new ConnectNSwingView(g, c, viewPiece, random, ai);
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public ConnectNFactoryExt() {
		super();
	}
	
	public ConnectNFactoryExt(int dim) {
		super(dim);
	}
}
