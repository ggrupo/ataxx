package es.ucm.fdi.tp.views.swing.boardpanel;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.views.swing.controlpanel.ControlPanelObserver;;

/**
 * A board component where game boards will be painted.
 *
 */
public abstract class BoardComponent extends JPanel implements ControlPanelObserver, MouseListener {

	private static final long serialVersionUID = -7727802925463521690L;
	
	/**
	 * Board's background color.
	 */
	public static final Color BG_COLOR = new Color(51,51,51);
	
	/**
	 * Indicates whether the board is enabled or not.
	 */
	protected boolean enabled = true;
	
	/**
	 * Creates an empty board.
	 */
	protected BoardComponent() {
		this.setOpaque(true);
		this.setBackground(BG_COLOR);
		this.addMouseListener(this);
	}
	
	/**
	 * Enables or disables the board.
	 * true stands for enable and false for disable.
	 * @param b - enable/disable boolean
	 */
	@Override
	public void setEnabled(boolean b) {
		this.enabled = b;
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
	
	/**
	 * Redraws the board.
	 * @param board - the board raw info
	 */
	public abstract void redraw(Board board);

	
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

}
