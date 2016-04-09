package es.ucm.fdi.tp.views.swing.boardpanel.pieces;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public abstract class PieceListener implements MouseListener {
	
	protected final static Cursor POINTER_CURSOR = new Cursor(Cursor.HAND_CURSOR);
	protected final static Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);
	
	protected boolean enabled = true;
	
	/**
	 * Invoked when the mouse right button has been clicked (pressed and released)
	 * on a piece.
	 * @param e
	 */
	public abstract void mouseRightClicked(MouseEvent e);
	
	/**
	 * Invoked when the mouse left button has been clicked (pressed and released)
	 * on a piece.
	 * @param e
	 */
	public abstract void mouseLeftClicked(MouseEvent e);
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(enabled) {
			if(e.getButton() == MouseEvent.BUTTON1)
				mouseLeftClicked(e);
			else
				mouseRightClicked(e);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if(enabled) {
			BoardPiece p = (BoardPiece) e.getSource();
			p.setCursor(POINTER_CURSOR);
			p.setBackground(BoardPiece.HOVER_BG_COLOR);
		}
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if(enabled) {
			BoardPiece p = (BoardPiece) e.getSource();
			p.setCursor(DEFAULT_CURSOR);
			p.setBackground(BoardPiece.BG_COLOR);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}
	
	/**
	 * Enables or disables all event listeners.
	 * @param enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
