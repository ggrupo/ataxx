package es.ucm.fdi.tp.views.swing.controlpanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.views.swing.SwingView;
import es.ucm.fdi.tp.views.swing.SwingView.PlayerMode;

public class PlayerModesPane extends JPanel implements ActionListener, GameObserver {
	
	private static final long serialVersionUID = 7482372784681144842L;
	
	public static final byte MANUAL_ONLY = 0;
	public static final byte MANUAL_RANDOM = 1;
	public static final byte MANUAL_AI = 2;
	public static final byte MANUAL_RANDOM_AI = 3;
	
	private JComboBox<PlayerMode> modesCombo;
	private JComboBox<Piece> playersCombo;
	private JButton setButton;
	
	private List<Piece> pieces;
	private final Map<Piece, SwingView.PlayerMode> playerModes;
	
	private Piece selectedPlayer;
	private PlayerMode selectedMode;
	
	protected final boolean canChangePlayer;
	protected final byte MODE;

	public PlayerModesPane(Map<Piece,PlayerMode> playerModes, byte mode, final Piece windowOwner) {
		this.playerModes = playerModes;
		this.MODE = mode;
		this.canChangePlayer = (windowOwner == null);
		
		buildPieceList(windowOwner);
		buildModeList();
		
		this.setButton = new JButton(" Set ");
		setButton.addActionListener(this);
		this.add(setButton);
		
		this.setBorder(new TitledBorder("Player modes"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		playerModes.put(selectedPlayer, selectedMode);
		
	}
	
	private void buildPieceList(final Piece windowOwner) {
		if(canChangePlayer) {
			this.playersCombo = new JComboBox<Piece>();
			
			playersCombo.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					selectedPlayer = (Piece) playersCombo.getSelectedItem();
				}
			});
			this.add(playersCombo);
			
		} else {
			selectedPlayer = windowOwner;
		}
	}
	
	private void buildModeList() {
		if(MODE != MANUAL_ONLY) {
			this.modesCombo = new JComboBox<PlayerMode>();
			
			modesCombo.addItem(PlayerMode.MANUAL);
			if((MODE & MANUAL_RANDOM) != 0) {
				modesCombo.addItem(PlayerMode.RANDOM);
			}
			if((MODE & MANUAL_AI) != 0) {
				modesCombo.addItem(PlayerMode.AI);
			}
			
			this.add(modesCombo);
			modesCombo.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					selectedMode = (PlayerMode) modesCombo.getSelectedItem();
					
				}
			});
		} else {
			selectedMode = PlayerMode.MANUAL;
		}
	}
	
	private void updatePlayers() {
		if(canChangePlayer) {
			this.playersCombo.removeAllItems();
			for(Piece p : pieces) {
				playersCombo.addItem(p);
			}
		}
	}
	
	@Override
	public void setEnabled(boolean b) {
		super.setEnabled(b);
		this.setButton.setEnabled(b);
		if((MODE & MANUAL_RANDOM) != 0) {
			modesCombo.setEnabled(b);
		}
		if((MODE & MANUAL_AI) != 0) {
			modesCombo.setEnabled(b);
		}
		if(canChangePlayer) {
			playersCombo.setEnabled(b);
		}
	}

	@Override
	public void onGameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn) {
		this.setEnabled(true);
		this.pieces = pieces;
		this.updatePlayers();
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
		this.updatePlayers();
	}

	@Override
	public void onError(String msg) {}
}
