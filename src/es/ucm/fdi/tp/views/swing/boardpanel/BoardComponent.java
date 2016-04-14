package es.ucm.fdi.tp.views.swing.boardpanel;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.views.swing.controlpanel.colorchooser.ColorChangeObserver;

public abstract class BoardComponent extends JPanel implements GameObserver, ColorChangeObserver, MouseListener {

	private static final long serialVersionUID = -7727802925463521690L;
	
	public static final Color BG_COLOR = new Color(51,51,51);
	
	protected boolean enabled = true;
	
	final protected Controller cntrl;
	final protected Map<Piece,Color> pieceColors;
	
	private Board board;
	
	
	protected BoardComponent(Controller c, Map<Piece,Color> colors) {
		this.cntrl = c;
		this.pieceColors = colors;
		
		this.setOpaque(true);
		this.setBackground(BG_COLOR);
		this.addMouseListener(this);
	}
	
	@Override
	public void setEnabled(boolean b) {
		super.setEnabled(b);
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
	public void onGameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn) {
		this.board = board;
		redraw();
	}

	@Override
	public void onGameOver(Board board, State state, Piece winner) {
		this.setEnabled(false);
	}
	
	@Override
	public void onColorChange(Piece player, Color newColor) {
		this.redraw();
	}
	
}
