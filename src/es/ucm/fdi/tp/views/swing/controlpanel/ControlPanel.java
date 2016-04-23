package es.ucm.fdi.tp.views.swing.controlpanel;

import java.awt.Color;
import java.awt.Dimension;
import java.util.LinkedList;
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

public final class ControlPanel extends JPanel implements GameObserver {

	private static final long serialVersionUID = -5012854304785344947L;
	
	final private Controller cntrl;
	final private SwingView view;
	
	final private Map<Piece,SwingView.PlayerMode> playerModes;
	final private Map<Piece,Color> playerColors;
	
	private final Player randPlayer;
	private final Player aiPlayer;
	private final Piece WINDOW_OWNER;

	/**
	 * Text area where messages eare shown.
	 * Needed to add messages from the outside.
	 */
	private MessagesBox messagesBox;
	
	/**
	 * Table where players information is displayed.
	 * Needed to add it as observer to the color chooser.
	 */
	private PlayersInfoTable infoTable;
	
	/**
	 * Enables the user to change players' colors.
	 * Needed to add observers to it on color change.
	 */
	private ColorChooserPane colorChooser;
	
	/**
	 * Enables the user to change players' modes.
	 * Needed to add observers to it on mode change.
	 */
	private PlayerModesPane playerModesPanel;

	private LinkedList<GameObserver> internalObservers = new LinkedList<GameObserver>();
	
	public ControlPanel(Controller c, SwingView v, 
		Map<Piece,SwingView.PlayerMode> playerModes, 
		Map<Piece, Color> playerColors,
		Player randPlayer, Player aiPlayer) 
	{
		this.cntrl = c;
		this.view = v;
		this.playerModes = playerModes;
		this.playerColors = playerColors;
		this.WINDOW_OWNER = view.getWindowOwner();
		this.randPlayer = randPlayer;
		this.aiPlayer = aiPlayer;

		initGUI();
	}
	
	public void showMessage(String m) {
		if(m != null)
			this.messagesBox.append(m);
		else
			this.messagesBox.setText(null);
	}
	
	private void initGUI() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		addMessagesBox();
		addPlayerInfoTable();
		addColorChangePane();
		addPlayerModesPane();
		addAutoMovesPane();
		addExitPane();
		
		colorChooser.addObserver(this.infoTable);
		colorChooser.addObserver(this.view);
		playerModesPanel.addObserver(this.infoTable);
		playerModesPanel.addObserver(this.view);
	}
	
	private void addMessagesBox() {
		messagesBox = new MessagesBox();
		this.add(messagesBox);
	}
	
	private void addPlayerInfoTable(){
		this.infoTable = new PlayersInfoTable(playerColors, playerModes, WINDOW_OWNER);
		/*infoTable.setMaximumSize(
				new Dimension(Integer.MAX_VALUE, infoTable.getHeight()));*/
		this.add(infoTable);
		this.internalObservers.add(infoTable);
	}
	
	private void addColorChangePane() {
		this.colorChooser = new ColorChooserPane(playerColors);
		colorChooser.setMaximumSize(
				new Dimension(Integer.MAX_VALUE, colorChooser.getHeight()));
		this.add(colorChooser);
		this.internalObservers.add(colorChooser);
	}
	
	private void addPlayerModesPane() {
		byte mode = PlayerModesPane.MANUAL_ONLY;
		if(randPlayer != null && aiPlayer != null)
			mode = PlayerModesPane.MANUAL_RANDOM_AI;
		else if(randPlayer != null)
			mode = PlayerModesPane.MANUAL_RANDOM;
		else if(aiPlayer != null)
			mode = PlayerModesPane.MANUAL_AI;
		
		this.playerModesPanel = new PlayerModesPane(playerModes, mode, WINDOW_OWNER);
		playerModesPanel.setMaximumSize(
				new Dimension(Integer.MAX_VALUE, playerModesPanel.getHeight()));
		
		if(randPlayer != null || aiPlayer != null) {
			this.add(playerModesPanel);
			this.internalObservers.add(playerModesPanel);
		}
	}
	
	private void addAutoMovesPane() {
		AutomaticMoves autoMovesPane = new AutomaticMoves(cntrl, randPlayer, aiPlayer, WINDOW_OWNER);
		if(randPlayer != null || aiPlayer != null) {
			autoMovesPane.setMaximumSize(
				new Dimension(Integer.MAX_VALUE, autoMovesPane.getHeight()));
			this.add(autoMovesPane);
			this.internalObservers.add(autoMovesPane);
		}
	}
	
	private void addExitPane() {
		ExitPanel exit = new ExitPanel(this.view);
		exit.setMaximumSize(
				new Dimension(Integer.MAX_VALUE, exit.getHeight()));
		this.add(exit);
		this.internalObservers.add(exit);
	}
	
	
	@Override
	public void onGameStart(final Board board, final String gameDesc, final List<Piece> pieces, final Piece turn) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				handleGameStart(board, gameDesc, pieces, turn);
			}
		});
	}
	
	private void handleGameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn) {
		messagesBox.setText("Game started: " + gameDesc);
		messagesBox.append(buildTurnString(turn));
		
		notifyGameStart(board, gameDesc, pieces, turn);
	}
	
	private void notifyGameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn) {
		for(GameObserver o : internalObservers) {
			o.onGameStart(board, gameDesc, pieces, turn);
		}
	}
	
	
	@Override
	public void onGameOver(final Board board, final State state, final Piece winner) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				handleGameOver(board, state, winner);
			}
		});
	}
	
	private void handleGameOver(Board board, State state, Piece winner) {
		messagesBox.append("Game over: " + state);
		if(state == State.Draw) {
			messagesBox.append("Draw");
		} else if(state == State.Won) {
			if(winner.equals(WINDOW_OWNER)) {
				messagesBox.append("You won");
			} else {
				messagesBox.append("The winner is: " + winner);
			}
		}
		
		notifyGameOver(board, state, winner);
	}
	
	private void notifyGameOver(Board board, State state, Piece winner) {
		for(GameObserver o : internalObservers) {
			o.onGameOver(board, state, winner);
		}
	}
	
	
	@Override
	public void onMoveStart(final Board board, final Piece turn) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				handleMoveStart(board, turn);
			}
		});
	}
	
	private void handleMoveStart(Board board, Piece turn) {
		notifyMoveStart(board, turn);
	}
	
	private void notifyMoveStart(Board board, Piece turn) {
		for(GameObserver o : internalObservers) {
			o.onMoveStart(board, turn);
		}
	}
	
	
	@Override
	public void onMoveEnd(final Board board, final Piece turn, final boolean success) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				handleMoveEnd(board, turn, success);
			}
		});
	}
	
	private void handleMoveEnd(Board board, Piece turn, boolean success) {
		notifyMoveEnd(board, turn, success);
	}
	
	private void notifyMoveEnd(Board board, Piece turn, boolean success) {
		for(GameObserver o : internalObservers) {
			o.onMoveEnd(board, turn, success);
		}
	}
	
	
	@Override
	public void onChangeTurn(final Board board, final Piece turn) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				handleChangeTurn(board, turn);
			}
		});
	}
	
	private void handleChangeTurn(Board board, Piece turn) {
		messagesBox.append(buildTurnString(turn));
		notifyChangeTurn(board, turn);
	}
	
	private void notifyChangeTurn(Board board, Piece turn) {
		for(GameObserver o : internalObservers) {
			o.onChangeTurn(board, turn);
		}
	}
	
	
	@Override
	public void onError(String msg) {
		//Errors are not welcome here!
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
	
}
