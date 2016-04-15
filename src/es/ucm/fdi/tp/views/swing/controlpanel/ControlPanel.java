package es.ucm.fdi.tp.views.swing.controlpanel;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.views.swing.SwingView;
import es.ucm.fdi.tp.views.swing.controlpanel.colorchooser.ColorChooserPane;
import es.ucm.fdi.tp.views.swing.controlpanel.textbox.MessagesBox;

public class ControlPanel extends JPanel implements GameObserver {

	private static final long serialVersionUID = -5012854304785344947L;
	
	final private Controller cntrl;
	final private SwingView view;
	
	final private Map<Piece,SwingView.PlayerMode> modosdeJuego;
	final private Map<Piece,Color> playerColors;
	
	private final Player randPlayer;
	private final Player aiPlayer;
	
	private List<Piece> pieces;
	private Board board;
	
	private MessagesBox messagesBox;
	private PlayersInfoTable infoTable;
	private ColorChooserPane colorChooser;
	private AutomaticMoves autoMovesPane;
	
	private final Piece WINDOW_OWNER;
	
	
	public ControlPanel(Controller c, SwingView v, 
		Map<Piece,SwingView.PlayerMode> playerModes, 
		Map<Piece, Color> playerColors,
		Player randPlayer, Player aiPlayer) 
	{
		this.cntrl = c;
		this.view = v;
		this.modosdeJuego = playerModes;
		this.playerColors = playerColors;
		this.WINDOW_OWNER = view.getWindowOwner();
		this.randPlayer = randPlayer;
		this.aiPlayer = aiPlayer;
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}
	
	final private void initGUI() {
		addMessagesBox();
		addPlayerInfoTable();
		addColorChangePane();
		addPlayerModesPane();
		addAutoMovesPane();
		addExitPane();
		
		this.colorChooser.addObserver(this.infoTable);
		this.colorChooser.addObserver(this.view);
	}
	
	private void addMessagesBox() {
		messagesBox = new MessagesBox();
		this.add(messagesBox);
	}
	
	private void addPlayerInfoTable(){
		this.infoTable = new PlayersInfoTable(board, playerColors, modosdeJuego, pieces);
		infoTable.setMaximumSize(
				new Dimension(Integer.MAX_VALUE, infoTable.getHeight()));
		this.add(infoTable);
	}
	
	private void addColorChangePane() {
		this.colorChooser = new ColorChooserPane(pieces,playerColors, WINDOW_OWNER);
		colorChooser.setMaximumSize(
				new Dimension(Integer.MAX_VALUE, colorChooser.getHeight()));
		this.add(colorChooser);
	}
	
	private void addPlayerModesPane() {
		PlayerModesPane modesPane = new PlayerModesPane();
		modesPane.setMaximumSize(
				new Dimension(Integer.MAX_VALUE, modesPane.getHeight()));
		this.add(modesPane);
	}
	
	private void addAutoMovesPane() {
		this.autoMovesPane = new AutomaticMoves(cntrl, randPlayer, aiPlayer);
		if(randPlayer != null || aiPlayer != null) {
			autoMovesPane.setMaximumSize(
				new Dimension(Integer.MAX_VALUE, autoMovesPane.getHeight()));
			this.add(autoMovesPane);
		}
	}
	
	private void addExitPane() {
		boolean singleView = view.getWindowOwner() == null;
		ExitPane exit = new ExitPane(this.cntrl,singleView);
		exit.setMaximumSize(
				new Dimension(Integer.MAX_VALUE, exit.getHeight()));
		this.add(exit);
	}
	
	public void showMessage(String m) {
		if(m != null)
			this.messagesBox.append(m);
		else
			this.messagesBox.setText(null);
	}
	
	public void onPiecesChange() {
		this.infoTable.refreshTable();
		this.colorChooser.refresh();
	}
	@Override
	public void onGameStart(Board board, final String gameDesc, List<Piece> pieces, final Piece turn) {
		// TODO Auto-generated method stub
		this.board = board;
		this.pieces = pieces;
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				initGUI();
				//^^^ Should not be done here

				ControlPanel.this.messagesBox.setText("Game started: " + gameDesc);
				ControlPanel.this.messagesBox.append(buildTurnString(turn));
			}

		});
		
	}
	@Override
	public void onGameOver(Board board, State state, Piece winner) {
		this.messagesBox.append("Game over: " + state);
		if(state == State.Draw) {
			this.messagesBox.append("Draw");
		} else if(state == State.Won) {
			if(winner.equals(WINDOW_OWNER)) {
				this.messagesBox.append("You won");
			} else {
				this.messagesBox.append("The winner is: " + winner);
			}
		}
		this.colorChooser.setEnabled(false);
		this.autoMovesPane.setEnabled(false);
	}
	@Override
	public void onMoveStart(Board board, Piece turn) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onMoveEnd(Board board, Piece turn, boolean success) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onChangeTurn(Board board, Piece turn) {
		this.messagesBox.append(buildTurnString(turn));
		//TODO update control panel (disable buttons or whatever)
	}
	
	/**
	 * Builds a String with the turn.
	 */
	private String buildTurnString(Piece turn) {
		if(turn.equals(WINDOW_OWNER)) {
			return "Your turn!";
		} else {
			return "Turn for " + turn;
		}
	}
	
	@Override
	public void onError(String msg) {}
}
