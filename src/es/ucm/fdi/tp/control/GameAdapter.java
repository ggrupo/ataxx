package es.ucm.fdi.tp.control;

import java.util.List;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

/**
 * An adapter for the GameObserver interface.
 * Override just the methods you want to use.
 * @author german
 *
 */
public class GameAdapter implements GameObserver {

	@Override
	public void onGameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn) {}

	@Override
	public void onGameOver(Board board, State state, Piece winner) {}

	@Override
	public void onMoveStart(Board board, Piece turn) {}

	@Override
	public void onMoveEnd(Board board, Piece turn, boolean success) {}

	@Override
	public void onChangeTurn(Board board, Piece turn) {}

	@Override
	public void onError(String msg) {}

}
