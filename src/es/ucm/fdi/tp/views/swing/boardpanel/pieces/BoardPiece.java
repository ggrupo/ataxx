package es.ucm.fdi.tp.views.swing.boardpanel.pieces;

import java.awt.Color;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class BoardPiece extends JPanel {
	
	public static final Color BG_COLOR = new Color(153,153,153);
	public static final Color HOVER_BG_COLOR = new Color(204,204,204);
	
	private PieceListener listener;
	
	protected BoardPiece() {
		super();
		this.setBackground(BG_COLOR);
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if(this.listener != null)
			this.listener.setEnabled(enabled);
	}
	
	public void addPieceListener(PieceListener l) {
		this.listener = l;
		this.addMouseListener(l);
	}
}
