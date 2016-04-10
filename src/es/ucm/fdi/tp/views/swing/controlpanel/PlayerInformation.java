package es.ucm.fdi.tp.views.swing.controlpanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.Scrollable;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableCellRenderer;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.views.swing.SwingView;

public class PlayerInformation extends JTable implements Scrollable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1771957667026716116L;

	public PlayerInformation(Board board, SwingView vista, Map<Piece,SwingView.PlayerMode> modosJuego, List<Piece> listaPiezas){
		
		final List<SwingView.PlayerMode> arrayModosJuego = new ArrayList<SwingView.PlayerMode>();
		final List<Integer> numeroDePiezas = new ArrayList<Integer>();
		final List<Color> colores = new ArrayList<Color>();
		for (Piece p : listaPiezas) {
			numeroDePiezas.add(board.getPieceCount(p));
			arrayModosJuego.add(modosJuego.get(p));
			colores.add(vista.getPieceColor(p));
		}
		final String[] tableHeader = {"Player","Mode","#Pieces"};
		final Object[][] tableBody =  {listaPiezas.toArray(), arrayModosJuego.toArray(), numeroDePiezas.toArray()};
		
		JTable table = new JTable(tableBody, tableHeader) {
			private static final long serialVersionUID = 1L;

			// THIS IS HOW WE CHANGE THE COLOR OF EACH ROW
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
				Component comp = super.prepareRenderer(renderer, row, col);

				// the color of row 'row' is taken from the colors table, if
				// 'null' setBackground will use the parent component color.
				comp.setBackground(colores.get(row));
				return comp;
			}
		};
		//aqui colororear las filas de la tabla segun la pieza
		table.setEnabled(false);
		VScrollPane scroll = new VScrollPane(table);
		scroll.setBorder(new TitledBorder("Player information"));
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		scroll.setMaximumSize(
				new Dimension(Integer.MAX_VALUE, (int) scroll.getHeight()));
		this.add(scroll);
	}
}
