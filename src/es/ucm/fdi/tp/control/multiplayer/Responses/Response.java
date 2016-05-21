package es.ucm.fdi.tp.control.multiplayer.Responses;

import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;

public interface Response extends java.io.Serializable {
	public void run(GameObserver o);
}
