package es.ucm.fdi.tp.control.multiplayer.Responses;

import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;

public class ErrorResponse implements Response {

	private static final long serialVersionUID = -8734500998980567066L;

	private String msg;
	
	public ErrorResponse(String msg) {
		this.msg = msg;
	}
	
	@Override
	public void run(GameObserver o) {
		o.onError(msg);
	}

}
