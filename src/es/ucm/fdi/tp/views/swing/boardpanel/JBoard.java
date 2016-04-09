package es.ucm.fdi.tp.views.swing.boardpanel;

import java.awt.Color;
import java.awt.LayoutManager;
import java.util.Map;

import javax.swing.JPanel;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.views.swing.controlpanel.colorchooser.ColorChangeObserver;

public abstract class JBoard extends JPanel implements GameObserver, ColorChangeObserver{

	private static final long serialVersionUID = -7727802925463521690L;
	
	public static final Color BG_COLOR = new Color(51,51,51);
	
	protected Controller cntrl;
	protected Map<Piece,Color> pieceColors;
	
	
	public JBoard(LayoutManager layout, Controller c, Map<Piece,Color> colors) {
		super(layout);
		this.cntrl = c;
		this.pieceColors = colors;
		initGUI();
	}
	
	/**
	 * Disables the board when it is not the player's turn.
	 */
	public abstract void disable();
	
	/**
	 * Enables the board when it is not the player's turn.
	 */
	public abstract void enable();
	
	@Override
	final public void setEnabled(boolean enabled) {
		if(enabled)
			enable();
		else
			disable();
	}
	
	private void initGUI() {
		this.setBackground(BG_COLOR);
	}
	
}
