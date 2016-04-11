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

import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

public class ColorChooserPane extends JPanel implements ActionListener, Observable<ColorChangeObserver>{

	private static final long serialVersionUID = 111272096861569383L;
	
	protected Map<Piece, Color> colorList;
	protected final Window parentWindow;
	
	private JComboBox<Object> piecesCombo;
	private final JButton changeColorBtn;
	
	private final List<Piece> pieces;
	
	private final Piece WINDOW_OWNER;
	
	/**
	 * List of observers.
	 */
	private ArrayList<ColorChangeObserver> observers = new ArrayList<ColorChangeObserver>(4); //should be static?
	
	public ColorChooserPane(List<Piece> pieces, Map<Piece, Color> pieceColors, final Piece windowOwner) {
		this.colorList = pieceColors;
		//this.parentFrame = parent;
		this.pieces = pieces;
		this.WINDOW_OWNER = windowOwner;
		
		if(WINDOW_OWNER == null) {
			piecesCombo = new JComboBox<Object>(pieces.toArray());
			this.add(piecesCombo);
		} else {
			piecesCombo = new JComboBox<Object>();
			piecesCombo.addItem(WINDOW_OWNER);
			//combobox object not added to the layout
		}
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
		if(WINDOW_OWNER == null) {
			piecesCombo.removeAll();
			for(Piece p : pieces) {
				piecesCombo.addItem(p);
			}
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

}
