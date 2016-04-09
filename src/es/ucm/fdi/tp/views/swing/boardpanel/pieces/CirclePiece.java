package es.ucm.fdi.tp.views.swing.boardpanel.pieces;

import java.awt.Color;
import java.awt.Graphics;

public class CirclePiece extends BoardPiece {
	
	private static final long serialVersionUID = 769265523186151460L;
	
	protected Color color;
	
	public CirclePiece(Color c) {
		super();
		this.color = c;
		
	}
	
	@Override
    protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(color != null) {
			int size = (int) (Math.min(getHeight(), getWidth())* 0.9f);
			g.setColor(color);
			g.fillArc((getWidth()-size)/2, (getHeight()-size)/2, size, size ,0,360);
		}
    }

	
}