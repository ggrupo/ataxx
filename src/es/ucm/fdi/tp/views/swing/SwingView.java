package es.ucm.fdi.tp.views.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.basecode.bgame.Utils;
import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.*;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;

import es.ucm.fdi.tp.views.swing.boardpanel.BoardComponent;
import es.ucm.fdi.tp.views.swing.controlpanel.ControlPanelObserver;
import es.ucm.fdi.tp.views.swing.controlpanel.ControlPanel;

public abstract class SwingView extends JFrame implements GameObserver, ControlPanelObserver {
	
	static {
		setDefaultLookAndFeel();
	}

	private static final long serialVersionUID = -5822298297801045598L;
	
	private Observable<GameObserver> model;
	protected final Controller cntrl;
	private final Player randPlayer;
	private final Player aiPlayer;
	private Board board; //A read only board
	
	private Piece turn;
	private final Piece WINDOW_OWNER;
	private final Map<Piece,Color> pieceColors = new HashMap<Piece,Color>();
	private final Map<Piece, PlayerMode> playerModes = new HashMap<Piece,PlayerMode>();
	
	private ControlPanel controlPanel;
	private BoardComponent boardComponent;

	/**
	 * Player modes (manual, random, etc.)
	 */
	public enum PlayerMode {
		MANUAL("m", "Manual"), RANDOM("r", "Random"), AI("a", "Automatics");

		private String id;
		private String desc;

		PlayerMode(String id, String desc) {
			this.id = id;
			this.desc = desc;
		}

		public String getId() {
			return id;
		}

		public String getDesc() {
			return desc;
		}

		@Override
		public String toString() {
			return desc;
		}
	}
	
	public SwingView(final Observable<GameObserver> g, Controller c, Piece localPiece, 
			Player randPlayer, Player aiPlayer)
	{
		this.cntrl= c;
		this.randPlayer = randPlayer;
		this.aiPlayer = aiPlayer;
		this.model = g;
		
		this.WINDOW_OWNER = localPiece;
		
		initGUI();
		g.addObserver(SwingView.this);
		
		this.setDefaultWindowSize();
		this.setLocationByPlatform(true);
		
		//Exit dialog before closing the windows
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				closeGame();
			}
		});
	}
	
	protected abstract BoardComponent createBoard();
	
	protected final void redrawBoard() {
		this.boardComponent.redraw();
	}
	
	private void initGUI() {
		JPanel container = new JPanel(new BorderLayout(0,0));
		
		this.controlPanel = new ControlPanel(cntrl,this,playerModes,pieceColors, randPlayer, aiPlayer);
		container.add(controlPanel, BorderLayout.LINE_END);
		model.addObserver(controlPanel);

		this.setContentPane(container);
	}
	
	private void initBoard() {
		if(boardComponent == null) {
			this.boardComponent = createBoard();
			getContentPane().add(boardComponent, BorderLayout.CENTER);
			model.addObserver(boardComponent);
		}
	}

	
	final protected Color getPieceColor(Piece p) { 
		return pieceColors.get(p); 
	}
	
	final protected Color setPieceColor(Piece p, Color c) { 
		return pieceColors.put(p,c); 
	}
	
	final protected Map<Piece, Color> getPlayerColors() {
		return pieceColors;
	}
	
	final protected Map<Piece, PlayerMode> getPlayerModes() {
		return playerModes;
	}
	
	final protected Board getBoard() {
		return this.board; 
	}
	
	public final Piece getWindowOwner() {
		return WINDOW_OWNER;
	}
	
	final public void showMessage(String m) {
		this.controlPanel.showMessage(m);
	}
	
	/**
	 * Closes the game after asking for confirmation.
	 */
	public void closeGame() {
		if(WINDOW_OWNER == null || turn.equals(WINDOW_OWNER)) {
			if((new QuitDialog(SwingView.this)).getValue()) {
				SwingView.this.cntrl.stop();
			}
		}
	}
	
	protected void initDefaultColors(List<Piece> pieces) {
		for(Piece p : pieces) {
			if(!pieceColors.containsKey(p)) {
				pieceColors.put(p, Utils.randomColor());
			}
		}
	}
	
	private void initWindowTitle(String gameDesc) {
		String owner = WINDOW_OWNER != null ? " (" + WINDOW_OWNER + ")" : "";
		this.setTitle("Board Games: " + gameDesc + owner);
	}
	
	/**
	 * Initializes all players to manual at first.
	 * @param pieces
	 */
	private void initPlayers(List<Piece> pieces) {
		if(playerModes.isEmpty()) {
			for(Piece p: pieces) {
				playerModes.put(p, PlayerMode.MANUAL);
			}
		}
	}
	
	
	@Override
	public void onGameStart(final Board board, final String gameDesc, final List<Piece> pieces, final Piece turn) {
		this.board = board;
		initDefaultColors(pieces);
		initPlayers(pieces);
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				handleGameStart(board, gameDesc, pieces, turn);
			}
		});
	}
	
	private void handleGameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn) {
		initBoard();
		initWindowTitle(gameDesc);
		
		this.turn = turn;
		if(turn.equals(WINDOW_OWNER)) {
			this.requestFocus(true);
			this.toFront();
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
		if(state == State.Stopped) {
			this.setVisible(false);
			this.dispose();
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
		redrawBoard();
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
		this.turn = turn;
		if(turn.equals(WINDOW_OWNER)) {
			this.requestFocus(true);
			this.toFront();
		}
	}
	

	@Override
	public void onError(String msg) {
		new ErrorDialog(msg, this);
	}
	
	private void setDefaultWindowSize() {
		Rectangle screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		this.setSize(screenSize.width/2, screenSize.height*3/4);
		this.setMinimumSize(new Dimension(screenSize.width/2, screenSize.height/2));
		
	}
	
	private static void setDefaultLookAndFeel() {
		try {
			javax.swing.UIManager.setLookAndFeel(
					javax.swing.UIManager.getSystemLookAndFeelClassName());
	    } catch (Exception e) {
	    	System.err.println("Look and feel mode: " + e.getMessage());
		}
	}

	@Override
	public void onColorChange(Piece player, Color newColor) {
		this.boardComponent.onColorChange(player, newColor);
	}
	
	@Override
	public void onPlayerModesChange(Piece player, PlayerMode newMode) {
		this.boardComponent.onPlayerModesChange(player, newMode);
	}
	
	@Override
	public JRootPane createRootPane() {
		JRootPane rootPane = new JRootPane();
		KeyStroke stroke = KeyStroke.getKeyStroke("ESCAPE");

		@SuppressWarnings("serial")
		Action action = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				closeGame();
			}
		};
		InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap.put(stroke, "ESCAPE");
		rootPane.getActionMap().put("ESCAPE", action);
		return rootPane;
	}

}
