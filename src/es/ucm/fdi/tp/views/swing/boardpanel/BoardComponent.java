package es.ucm.fdi.tp.views.swing.boardpanel;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.views.swing.SwingView.PlayerMode;
import es.ucm.fdi.tp.views.swing.controlpanel.ControlPanelObserver;;

public abstract class BoardComponent extends JPanel implements GameObserver, ControlPanelObserver, MouseListener {

	private static final long serialVersionUID = -7727802925463521690L;
	
	public static final Color BG_COLOR = new Color(51,51,51);
	
	protected boolean enabled = true;
	
	final protected Map<Piece,Color> pieceColors;
	final protected Map<Piece,PlayerMode> playerModes;
	
	private Board board;
	
	
	protected BoardComponent(Map<Piece,Color> colors, Map<Piece, PlayerMode> playerModes) {
		this.pieceColors = colors;
		this.playerModes = playerModes;
		
		this.setOpaque(true);
		this.setBackground(BG_COLOR);
		this.addMouseListener(this);
		
	}
	
	@Override
	public void setEnabled(boolean b) {
		this.enabled = b;
	}
	
	/**
	 * Provides the board to children clases.
	 */
	final protected Board getBoard() {
		return this.board;
	}
	
	/**
	 * Fires when the board is right-clicked.
	 * @param x - horizontal position of the mouse when clicked
	 * @param y - vertical position of the mouse when clicked 
	 */
	protected abstract void handleRightClick(int x, int y);
	
	/**
	 * Fires when the board is left-clicked.
	 * @param x - horizontal position of the mouse when clicked
	 * @param y - vertical position of the mouse when clicked 
	 */
	protected abstract void handleLeftClick(int x, int y);
	
	public abstract void redraw();

	@Override
	public void mouseClicked(MouseEvent e) {
		if(this.enabled) {
			if(e.getButton() == MouseEvent.BUTTON1) {
				handleLeftClick(e.getX(), e.getY());
			} else {
				handleRightClick(e.getX(), e.getY());
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void onGameStart(final Board board, final String gameDesc, final List<Piece> pieces, final Piece turn) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				handleGameStart(board, gameDesc, pieces, turn);
			}
		});
	}

	private void handleGameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn){
		this.board = board;
		this.setEnabled(true);
		redraw();
	}
	
	@Override
	public void onGameOver(final Board board, final State state, final Piece winner) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				handleGameOver(board, state, winner);
			}
		});
	}
	
	private void handleGameOver(Board board, State state, Piece winner) {
		this.setEnabled(false);
	}
	
	@Override
	public void onColorChange(Piece player, Color newColor) {
		this.redraw();
	}

	@Override
	public void onChangeTurn(final Board board, final Piece turn) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				handleChangeTurn(board, turn);
			}
		});
	}
	
	private void handleChangeTurn(Board board, Piece turn) {
		//TODO - this isn't ok. Needs to check wether the owner is in their turn
		boolean enable = playerModes.get(turn)==PlayerMode.MANUAL;
		this.setEnabled(enable);
	}
	
	@Override
	public void onMoveStart(Board board, Piece turn) {
		this.setEnabled(false);
	}

	@Override
	public void onMoveEnd(Board board, Piece turn, boolean success) {
		//TODO - this isn't ok. Needs to check wether the owner is in their turn
		boolean enable = playerModes.get(turn)==PlayerMode.MANUAL;
		this.setEnabled(enable);
	}
}
