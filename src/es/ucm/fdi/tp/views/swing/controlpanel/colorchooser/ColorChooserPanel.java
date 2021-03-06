package es.ucm.fdi.tp.views.swing.controlpanel.colorchooser;

import java.awt.Color;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.views.swing.controlpanel.ControlPanelObserver;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

/**
 * A panel containing a list of players and a button. When the button is
 *  pressed the user is able to choose a new color for the selected player 
 *  from a modal dialog.
 * @author german
 *
 */
public class ColorChooserPanel extends JPanel implements ActionListener, Observable<ControlPanelObserver>, GameObserver {

	private static final long serialVersionUID = 111272096861569383L;
	
	protected Map<Piece, Color> colorList;
	
	private JComboBox<Piece> piecesCombo;
	private final JButton changeColorBtn;
	
	private List<Piece> pieces;
	
	/**
	 * List of observers.
	 */
	private ArrayList<ControlPanelObserver> observers = new ArrayList<ControlPanelObserver>(4);
	
	/**
	 * Creates a panel to change the players' color.
	 * @param pieceColors - players' colors.
	 */
	public ColorChooserPanel(Map<Piece, Color> pieceColors) {
		this.colorList = pieceColors;
		
		piecesCombo = new JComboBox<Piece>();
		this.add(piecesCombo);

		changeColorBtn = new JButton("Choose color");
		changeColorBtn.addActionListener(this);
		this.add(changeColorBtn);
		
		
		this.setBorder(new TitledBorder("Piece Colors"));
	}
	
	/**
	 * Fires when the button is pressed. 
	 * Launches a dialog to choose a new color.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Piece selected = (Piece) piecesCombo.getSelectedItem();
		Window parent = SwingUtilities.getWindowAncestor(this);
		
		ColorChooserDialog chooser = new ColorChooserDialog(parent, "Choose your color", colorList.get(selected));
		Color color = chooser.getColor();
		
		if (color != null) {
			colorList.put(selected, color);
			notifyColorChange(selected, color);
		}
		
	}
	
	/**
	 * Enables or disables the button and the list inside the panel.
	 */
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		this.changeColorBtn.setEnabled(enabled);
		this.piecesCombo.setEnabled(enabled);
	}
	
	/**
	 * Refresh pieces list (combobox).
	 */
	public void refresh() {
		piecesCombo.removeAllItems();
		for(Piece p : pieces) {
			piecesCombo.addItem(p);
		}
	}
	
	
	/**
	 * Adds a color change observer. When the color of a player is changed all
	 * observers are notified.
	 */
	@Override
	public void addObserver(ControlPanelObserver o) {
		observers.add(o);
		
	}

	@Override
	public void removeObserver(ControlPanelObserver o) {
		observers.remove(o);
	}
	
	private void notifyColorChange(Piece player, Color newColor) {
		for (ControlPanelObserver o : observers) {
			o.onColorChange(player,newColor);
		}
	}

	@Override
	public void onGameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn) {
		this.pieces = pieces;
		this.refresh();
		this.setEnabled(true);
	}

	@Override
	public void onGameOver(Board board, State state, Piece winner) {
		this.setEnabled(false);
	}

	@Override
	public void onMoveStart(Board board, Piece turn) {}

	@Override
	public void onMoveEnd(Board board, Piece turn, boolean success) {}

	@Override
	public void onChangeTurn(Board board, Piece turn) {
		this.refresh();
	}

	@Override
	public void onError(String msg) {}

}
