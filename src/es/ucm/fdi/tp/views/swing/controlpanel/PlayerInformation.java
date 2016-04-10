package es.ucm.fdi.tp.views.swing.controlpanel;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.views.swing.SwingView;
import es.ucm.fdi.tp.views.swing.controlpanel.colorchooser.ColorChangeObserver;

public class PlayerInformation extends VScrollPane implements ColorChangeObserver {

	private static final long serialVersionUID = 1771957667026716116L;

	public PlayerInformation(Board board, SwingView vista, Map<Piece,SwingView.PlayerMode> modosJuego, List<Piece> listaPiezas){
	
		PlayerInfoTableModel tableModel = new PlayerInfoTableModel(board, modosJuego);

		final List<Color> colores = new ArrayList<Color>();
		for (Piece p : listaPiezas) {
			tableModel.addPlayer(p);
			colores.add(vista.getPieceColor(p));
		}
		JTable table = new JTable(tableModel) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
				Component comp = super.prepareRenderer(renderer, row, col);

				// the color of row 'row' is taken from the colors table, if
				// 'null' setBackground will use the parent component color.
				comp.setBackground(colores.get(row));
				return comp;
			}
		};
		table.setEnabled(false);
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		this.add(table);
		this.setBorder(new TitledBorder("Players information"));
	}

	@Override
	public void onColorChange(Piece player, Color newColor) {
		// TODO Auto-generated method stub
		
	}
	
	private static class PlayerInfoTableModel extends DefaultTableModel {
		
		private static final long serialVersionUID = 7917034667342447386L;

		private static final String[] tableHeader = {"Player","Mode","#Pieces"};
		
		private Board board;
		private Map<Piece,SwingView.PlayerMode> modes;
		
		private List<Object[]> players;
		
		public PlayerInfoTableModel(Board board, Map<Piece,SwingView.PlayerMode> playerModes) {
			this.board = board;
			this.modes = playerModes;
			this.players = new ArrayList<Object[]>(4);
		}
		
		@Override
		public String getColumnName(int col) {
			return tableHeader[col];
		}
		
		@Override
		public int getColumnCount() {
			return tableHeader.length;
		}
		
		@Override
		public int getRowCount() {
			return players != null ? players.size() : 0;
		}
		
		public void addPlayer(Piece p) {
			Integer pCount = board.getPieceCount(p);
			Object[] newPlayer = {
					p, 
					modes.get(p).getDesc(), 
					pCount != null ? pCount : 0
			};
			this.players.add(newPlayer);
			refresh();
		}
		
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return players.get(rowIndex)[columnIndex];
		}
		
		private void refresh() {
			fireTableDataChanged();
		}
	}
	
}
