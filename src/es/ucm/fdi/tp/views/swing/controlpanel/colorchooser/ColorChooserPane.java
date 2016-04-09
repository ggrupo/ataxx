package es.ucm.fdi.tp.views.swing.controlpanel.colorchooser;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import es.ucm.fdi.tp.basecode.bgame.model.Observable;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

public class ColorChooserPane extends JPanel implements ActionListener, Observable<ColorChangeObserver>{

	private static final long serialVersionUID = 111272096861569383L;
	
	protected Map<Piece, Color> colorList;
	protected final JFrame parentFrame;
	
	private final JComboBox<Object> piecesCombo;
	private final JButton changeColorBtn;
	
	/**
	 * List of observers.
	 */
	private ArrayList<ColorChangeObserver> observers;
	
	public ColorChooserPane(Map<Piece, Color> pieceColors, JFrame parent) {
		this.colorList = pieceColors;
		this.parentFrame = parent;

		piecesCombo = new JComboBox<Object>(pieceColors.keySet().toArray());
		this.add(piecesCombo);
		
		changeColorBtn = new JButton("Choose color");
		changeColorBtn.addActionListener(this);
		this.add(changeColorBtn);
		
		this.setBorder(new TitledBorder("Piece Colors"));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Piece selected = (Piece) piecesCombo.getSelectedItem();
		ColorChooser chooser = new ColorChooser(parentFrame, "Choose your color", colorList.get(selected));
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
