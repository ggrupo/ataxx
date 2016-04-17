package es.ucm.fdi.tp.views.swing.controlpanel.colorchooser;

import java.awt.Color;

import es.ucm.fdi.tp.basecode.bgame.model.Piece;

public interface ColorChangeObserver {
	
	public void onColorChange(Piece player, Color newColor);
}
