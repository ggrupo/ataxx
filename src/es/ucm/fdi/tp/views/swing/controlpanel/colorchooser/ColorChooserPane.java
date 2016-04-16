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
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

public class ColorChooserPane extends JPanel implements ActionListener, Observable<ColorChangeObserver>, GameObserver {

	private static final long serialVersionUID = 111272096861569383L;
	
	protected Map<Piece, Color> colorList;
	protected final Window parentWindow;
	
	private JComboBox<Piece> piecesCombo;
	private final JButton changeColorBtn;
	
	private List<Piece> pieces;
	
	/**
	 * List of observers.
	 */
	private ArrayList<ColorChangeObserver> observers = new ArrayList<ColorChangeObserver>(4); //should be static?
	
	public ColorChooserPane(Map<Piece, Color> pieceColors) {
		this.colorList = pieceColors;
		
		piecesCombo = new JComboBox<Piece>();
		this.add(piecesCombo);

		changeColorBtn = new JButton("Choose color");
		changeColorBtn.addActionListener(this);
		this.add(changeColorBtn);
		
		
		this.setBorder(new TitledBorder("Piece Colors"));
		
		this.parentWindow = SwingUtilities.getWindowAncestor(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Piece selected = (Piece) piecesCombo.getSelectedItem();
		ColorChooser chooser = new ColorChooser(parentWindow, "Choose your color", colorList.get(selected));
		Color color = chooser.getColor();
		
		if (color != null) {
			colorList.put(selected, color);
			notifyColorChange(selected, color);
		}
		
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		this.changeColorBtn.setEnabled(enabled);
		this.piecesCombo.setEnabled(enabled);
	}
	
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
	public void addObserver(ColorChangeObserver o) {
		observers.add(o);
		
	}

	@Override
	public void removeObserver(ColorChangeObserver o) {
		observers.remove(o);
	}
	
	private void notifyColorChange(Piece player, Color newColor) {
		for (ColorChangeObserver o : observers) {
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
