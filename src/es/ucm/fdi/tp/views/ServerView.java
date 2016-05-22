package es.ucm.fdi.tp.views;

import es.ucm.fdi.tp.control.multiplayer.NetObserver;

public interface ServerView extends NetObserver {
	
	public void log(String msg);

}
