package es.ucm.fdi.tp.views.swing.controlpanel;

import java.awt.Color;

import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.views.swing.SwingView.PlayerMode;

/**
 * An interface provided to listen to control panel events such as a color 
 * change or a player mode change. Must be implemented in order to be able
 * to listent to events from an {@code Observer<ControlPanelObserver>} class.
 */
public interface ControlPanelObserver {
	
	/**
	 * Fires when a color change occurs.
	 * @param player - player whose color changed
	 * @param newColor - new player's color
	 */
	public void onColorChange(Piece player, Color newColor);
	
	/**
	 * Fires when a player mode changes.
	 * @param player - player whose mode changed
	 * @param newMode - new moed of the player
	 */
	public void onPlayerModesChange(Piece player, PlayerMode newMode);
}
