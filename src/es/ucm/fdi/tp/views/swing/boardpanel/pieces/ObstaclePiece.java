package es.ucm.fdi.tp.views.swing.boardpanel.pieces;

import java.awt.Color;
import java.awt.Graphics;

import es.ucm.fdi.tp.views.swing.boardpanel.JBoard;

public class ObstaclePiece extends BoardPiece {

	private static final long serialVersionUID = 1L;
	protected static final Color LINE_COLOR = BoardPiece.BG_COLOR;
	protected static final Color BG_COLOR = JBoard.BG_COLOR.brighter();
	
	public ObstaclePiece() {
		super.setEnabled(false);
		this.setOpaque(false);
	}
	@Override
    protected void paintComponent(Graphics g) {
		int height = this.getHeight();
		int width = this.getWidth();
		g.setColor(BG_COLOR);
		g.fillRect(0, 0, width, height);
		g.setColor(LINE_COLOR);
		g.drawLine(0, 0, width, height);
		g.drawLine(width, 0, 0, height);
		g.drawRect(0, 0, width-1, height-1);
		
	}

	@Override
	public void setEnabled(boolean enabled) {}
	@Override
	public Color getColor() {
		return null;
	}
}
