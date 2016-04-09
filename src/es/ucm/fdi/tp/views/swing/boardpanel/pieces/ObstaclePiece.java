package es.ucm.fdi.tp.views.swing.boardpanel.pieces;

import java.awt.Color;
import java.awt.Graphics;

public class ObstaclePiece extends BoardPiece {

	private static final long serialVersionUID = 1L;
	protected static final Color LINE_COLOR = BoardPiece.BG_COLOR.darker();
	public ObstaclePiece() {
		super.setEnabled(false);
		this.setOpaque(false);
	}
	@Override
    protected void paintComponent(Graphics g) {
		g.setColor(LINE_COLOR);
		g.drawLine(0, 0, getWidth(), getHeight());
		g.drawLine(getWidth(), 0, 0, getHeight());
		g.drawRect(0, 0, getWidth()-1, getHeight()-1);
	}

	@Override
	public void setEnabled(boolean enabled) {}
}
