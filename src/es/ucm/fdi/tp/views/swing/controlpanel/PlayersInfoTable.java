package es.ucm.fdi.tp.views.swing.controlpanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.views.swing.SwingView;
import es.ucm.fdi.tp.views.swing.SwingView.PlayerMode;

public class PlayersInfoTable extends VScrollPane implements ControlPanelObserver, GameObserver {

	private static final long serialVersionUID = 1771957667026716116L;
	
	private PlayerInfoTableModel tableModel;
	
	private List<Color> colors = new ArrayList<Color>();
	private List<Piece> pieceList;
	
	private final Piece WINDOW_OWNER;
	
	final private JTable table;
	
	private Map<Piece,Color> colorMap;
	private Map<Piece,SwingView.PlayerMode> playerModes;
	private Board board;

	public PlayersInfoTable(Map<Piece,Color> playerColors, Map<Piece,SwingView.PlayerMode> playerModes, Piece windowOwner) {
		this.colorMap = playerColors;
		this.playerModes = playerModes;
		this.WINDOW_OWNER = windowOwner;
		
		this.tableModel = new PlayerInfoTableModel();
		this.table = new JTable(tableModel) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
				Component comp = super.prepareRenderer(renderer, row, col);

				// the color of row 'row' is taken from the colors table, if
				// 'null' setBackground will use the parent component color.
				comp.setBackground(colors.get(row));
				return comp;
			}
		};
		
		this.table.setEnabled(false);
		this.table.setPreferredScrollableViewportSize(table.getPreferredSize());
		this.setMinimumSize(new Dimension(100, 100));
		this.add(this.table);
		this.setBorder(new TitledBorder("Players information"));
	}

	public void refreshTable() {
		tableModel.clear();
		colors.clear();
		for (Piece p : pieceList) {
			tableModel.addPlayer(p);
			colors.add(colorMap.get(p));
		}
		table.repaint();
	}
	
	private class PlayerInfoTableModel extends DefaultTableModel {
		
		private static final long serialVersionUID = 7917034667342447386L;

		private final String[] tableHeader = {"Player","Mode","#Pieces"};
		
		private List<Object[]> players;
		
		public PlayerInfoTableModel() {
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
			String pMode = " - ";
			if(WINDOW_OWNER == null || p.equals(WINDOW_OWNER)) {
				pMode = playerModes.get(p).getDesc();
			}
			Object[] newPlayer = {p, pMode, pCount != null ? pCount : " - "};
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
		
		private void clear() {
			this.players.clear();
			refresh();
		}
	}
	
	@Override
	public void onColorChange(Piece player, Color newColor) {
		refreshTable();
	}
	
	@Override
	public void onPlayerModesChange(Piece player, PlayerMode newMode) {
		refreshTable();
	}

	@Override
	public void onGameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn) {
		this.pieceList = pieces;
		this.board = board;
		
		refreshTable();
	}
	
	@Override
	public void onMoveEnd(Board board, Piece turn, boolean success) {
		refreshTable();
	}

	@Override
	public void onChangeTurn(Board board, Piece turn) {}

	@Override
	public void onGameOver(Board board, State state, Piece winner) {}

	@Override
	public void onMoveStart(Board board, Piece turn) {}

	@Override
	public void onError(String msg) {}
	
}
