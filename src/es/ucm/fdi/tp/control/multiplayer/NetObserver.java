package es.ucm.fdi.tp.control.multiplayer;

import es.ucm.fdi.tp.basecode.bgame.model.Piece;

public interface NetObserver {
	
	public void onPlayerConnected(Piece p);
	
	public void onGameStopped();
	
	public void onServerOpened(String gameDesc);
	
	public void onServerClosed();
	
	public void log(String msg);
}
