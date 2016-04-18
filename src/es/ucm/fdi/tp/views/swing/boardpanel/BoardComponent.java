package es.ucm.fdi.tp.views.swing.boardpanel;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.views.swing.controlpanel.ControlPanelObserver;;

public abstract class BoardComponent extends JPanel implements ControlPanelObserver, MouseListener {

	private static final long serialVersionUID = -7727802925463521690L;
	
	public static final Color BG_COLOR = new Color(51,51,51);
	
	protected boolean enabled = true;
	
	protected BoardComponent() {
		this.setOpaque(true);
		this.setBackground(BG_COLOR);
		this.addMouseListener(this);
	}
	
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
	
	public abstract void redraw(Board b);

	
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
