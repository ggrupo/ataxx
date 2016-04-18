package es.ucm.fdi.tp.views.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.basecode.bgame.Utils;
import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.*;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;

import es.ucm.fdi.tp.views.swing.boardpanel.BoardComponent;
import es.ucm.fdi.tp.views.swing.controlpanel.ControlPanelObserver;
import es.ucm.fdi.tp.views.swing.controlpanel.ControlPanel;

public abstract class SwingView implements GameObserver, ControlPanelObserver {
	
	static {
		setDefaultLookAndFeel();
	}
	
	private final JFrame WINDOW; 
	
	private Observable<GameObserver> model;
	private Controller cntrl;
	private final Player randPlayer;
	private final Player aiPlayer;
	private Board board; //A read only board
	
	private State gameState;
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
		this.WINDOW = new JFrame();
		
		this.cntrl= c;
		this.randPlayer = randPlayer;
		this.aiPlayer = aiPlayer;
		this.model = g;
		
		this.WINDOW_OWNER = localPiece;
		
		initGUI();
		g.addObserver(SwingView.this);
		
		this.setDefaultWindowSize();
		WINDOW.setLocationByPlatform(true);
		
		//Exit dialog before closing the windows
		WINDOW.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		WINDOW.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				closeGame();
			}
		});
		WINDOW.setVisible(true);
	}
	
	public final Piece getWindowOwner() {
		return WINDOW_OWNER;
	}
	
	public final void showMessage(String m) {
		this.controlPanel.showMessage(m);
	}
	
	public boolean isPieceTurn(Piece p) {
		return p.equals(WINDOW_OWNER) || WINDOW_OWNER == null;
	}
	
	protected final Color getPieceColor(Piece p) { 
		return pieceColors.get(p); 
	}
	
	protected final Color setPieceColor(Piece p, Color c) { 
		return pieceColors.put(p,c); 
	}
	
	protected abstract BoardComponent createBoard();
	
	protected final void redrawBoard() {
		this.boardComponent.redraw(board);
	}
	
	protected final PlayerMode getPlayerMode(Piece p) {
		return playerModes.get(p);
	}
	
	protected final Board getBoard() {
		return this.board; 
	}
	
	protected final boolean requestPlayerMove(Player p) {
		try {
			cntrl.makeMove(p);
		} catch (GameError e) {
			return false;
		}
		return true;
	}
	
	private boolean requestAutomaticMove() {
		PlayerMode turnMode = playerModes.get(turn);
		boolean success = true;
		try {
			if(turnMode == PlayerMode.RANDOM) {
				requestRandomMove();
			} else if(turnMode == PlayerMode.AI) {
				requestAIMove();
			} else {
				success = false;
			}
		} catch(GameError e) {
			success = false;
		}
		return success;
	}
	
	private void requestRandomMove() {
		if(randPlayer == null) {
			String errorMessage = PlayerMode.RANDOM + " mode is not supported by this game. ";
			onError(errorMessage);
			throw new GameError(errorMessage);
		}
		
		this.cntrl.makeMove(randPlayer);
	}
	
	private void requestAIMove() {
		if(aiPlayer == null) {
			String errorMessage = PlayerMode.AI + " mode is not supported by this game. ";
			onError(errorMessage);
			throw new GameError(errorMessage);
		}
		
		this.cntrl.makeMove(aiPlayer);
	}
	
	private void initGUI() {
		JPanel container = new JPanel(new BorderLayout(0,0));
		
		this.controlPanel = new ControlPanel(cntrl,this,playerModes,pieceColors, randPlayer, aiPlayer);
		container.add(controlPanel, BorderLayout.LINE_END);
		model.addObserver(controlPanel);

		WINDOW.setContentPane(container);
	}
	
	private void initBoard() {
		if(boardComponent == null) {
			this.boardComponent = createBoard();
			WINDOW.getContentPane().add(boardComponent, BorderLayout.CENTER);
		}
	}
	
	/**
	 * Closes the game after asking for confirmation.
	 */
	private void closeGame() {
		if(isPieceTurn(turn) || gameState == State.Stopped) {
			if((new QuitDialog(WINDOW)).getValue()) {
				SwingView.this.cntrl.stop();
			}
		}
	}
	
	private void initDefaultColors(List<Piece> pieces) {
		for(Piece p : pieces) {
			if(!pieceColors.containsKey(p)) {
				pieceColors.put(p, Utils.randomColor());
			}
		}
	}
	
	private void initWindowTitle(String gameDesc) {
		String owner = WINDOW_OWNER != null ? " (" + WINDOW_OWNER + ")" : "";
		WINDOW.setTitle("Board Games: " + gameDesc + owner);
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
		
		handleChangeTurn(board, turn);
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
		this.gameState = state;
		boardComponent.setEnabled(false);
		if(state == State.Stopped) {
			WINDOW.setVisible(false);
			WINDOW.dispose();
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
		boardComponent.setEnabled(false);
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
		boardComponent.setEnabled(isPieceTurn(turn));
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
		boardComponent.setEnabled(isPieceTurn(turn));
		if(turn.equals(WINDOW_OWNER)) {
			WINDOW.toFront();
		}
		requestAutomaticMove();
		redrawBoard();
	}

	@Override
	public void onError(String msg) {
		if(isPieceTurn(turn)) {
			new ErrorDialog(msg, WINDOW);
		}
	}
	
	
	@Override
	public void onColorChange(Piece player, Color newColor) {
		boardComponent.onColorChange(player, newColor);
	}
	
	@Override
	public void onPlayerModesChange(Piece player, PlayerMode newMode) {
		if(player.equals(turn)) {
			requestAutomaticMove();
		}
		boardComponent.onPlayerModesChange(player, newMode);
	}
	
	
	private void setDefaultWindowSize() {
		Rectangle screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		WINDOW.setSize(screenSize.width/2, screenSize.height*3/4);
		WINDOW.setMinimumSize(new Dimension(screenSize.width/2, screenSize.height/2));
		
	}
	
	private static void setDefaultLookAndFeel() {
		try {
			javax.swing.UIManager.setLookAndFeel(
					javax.swing.UIManager.getSystemLookAndFeelClassName());
	    } catch (Exception e) {
	    	System.err.println("Look and feel mode: " + e.getMessage());
		}
	}
	

}
