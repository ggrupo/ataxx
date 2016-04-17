package es.ucm.fdi.tp.views.swing.controlpanel;

import java.awt.Color;

import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.views.swing.SwingView.PlayerMode;

public interface ControlPanelObserver {
	
	public void onColorChange(Piece player, Color newColor);
	
	public void onPlayerModesChange(Piece player, PlayerMode newMode);
}
