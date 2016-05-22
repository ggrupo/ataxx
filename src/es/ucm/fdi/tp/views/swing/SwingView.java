package es.ucm.fdi.tp.views.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
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

/**
 * View class in the MVC, powered by Java Swing widgets.
 * Will show a window containing the game divided in two main regions: 
 * the board and the control panel.<br/>
 * May be used as a single view for everyone or as a window per player (multiviews).
 */
public abstract class SwingView implements GameObserver, ControlPanelObserver {
	
	static {
		setDefaultLookAndFeel();
	}
	
	/**
	 * The container game window.
	 * It's inside like an attribute instead of inheritance to avoid 
	 * others who has a reference to close the window.
	 */
	private final JFrame WINDOW;
	
	private static byte currentlyOpen = 0;
	
	private Observable<GameObserver> model;
	private Controller cntrl;
	private final Player randPlayer;
	private final Player aiPlayer;
	private Board board; //A read only board
	
	private State gameState;
	private Piece turn;
	private final Piece WINDOW_OWNER;
	
	/**
	 * Map of colors, each for one player. 
	 * Is initialized at {@link #initDefaultColors(List)}.
	 */
	private final Map<Piece,Color> pieceColors = new HashMap<Piece,Color>();
	
	/**
	 * Map of players' modes. This view ignores the console options and
	 * initializes them all to MANUAL in {@link #initPlayers(List)}.
	 */
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
	
	/**
	 * Creates a window which shows the given game. It is also able to send
	 * responses to the game through the controller.
	 * 
	 * Give a value to localPiece if you want one player's perspective or null.
	 * You also may want to create multiple {@linkplain SwingView} if you have
	 * multiple players.
	 * @param g - Game model observable. Needed to recieve game events.
	 * @param c - Game controller
	 * @param localPiece - Player which will own this window, null if 
	 * everyone plays in the same window.
	 * @param randPlayer - Random player if supported or null.
	 * @param aiPlayer - AI player if supported or null.
	 */
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
		this.setDefaultLocation();
		
		//Exit dialog before closing the windows
		WINDOW.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		WINDOW.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				requestCloseGame();
			}
		});
		WINDOW.setVisible(true);
		
		SwingView.currentlyOpen++;
	}
	
	/**
	 * Returns a piece which is the window owner.
	 * @return owner
	 */
	public final Piece getWindowOwner() {
		return WINDOW_OWNER;
	}
	
	/**
	 * Shows a message in the messages box section.
	 * @param m - Message to be displayed.
	 */
	public final void showMessage(String m) {
		this.controlPanel.showMessage(m);
	}
	
	/**
	 * Request the player to close the game if it is their turn or
	 * if the game ended.
	 */
	public final void requestCloseGame() {
		if(gameState != State.InPlay || isPieceTurn(turn)) {
			if((new QuitDialog(WINDOW)).getValue()) {
				SwingView.this.cntrl.stop();
			}
		}
	}
	
	/**
	 * Request a game restart. Only possible in the players' turn or
	 * if the game ended.
	 */
	public final void requestRestartGame() {
		if(isPieceTurn(turn) || gameState != State.InPlay) {
			cntrl.restart();
		}
	}
	
	/**
	 * Returns whether is the piece's turn or not. If it is their turn it
	 * returns true, else it returns false.
	 * @param p Piece to check the turn with.
	 * @return whether is the piece's turn or not.
	 */
	public boolean isPieceTurn(Piece p) {
		return p.equals(WINDOW_OWNER) || WINDOW_OWNER == null;
	}
	
	/**
	 * Returns the color aof the given piece.
	 * @param p Piece to check the color.
	 * @return the color of the piece.
	 */
	protected final Color getPieceColor(Piece p) { 
		return pieceColors.get(p); 
	}
	
	/**
	 * Sets a color for a piece.
	 * @param p piece whose color will be changed
	 * @param c color of the piece to be changed
	 * @return the previous color of the piece or null if it didn't exist
	 */
	protected final Color setPieceColor(Piece p, Color c) { 
		return pieceColors.put(p,c); 
	}
	
	/**
	 * Builds a full board to be added in the view. It may be called to be
	 * redrawn or disabled/enabled at any time after this method.
	 * @return the built board
	 */
	protected abstract BoardComponent createBoard();
	
	/**
	 * Redraws the board component. 
	 */
	protected final void redrawBoard() {
		this.boardComponent.redraw(board);
	}
	
	/**
	 * Returns the player mode for a given player.
	 * @param p player piece.
	 * @return the player's gaming mode
	 */
	protected final PlayerMode getPlayerMode(Piece p) {
		return playerModes.get(p);
	}
	
	/**
	 * Returns a reference to the board.
	 */
	protected final Board getBoard() {
		return this.board; 
	}
	
	/**
	 * Requests a move to the game for the given player.
	 * @param p player
	 * @return true if successfull, false otherwise.
	 */
	protected final boolean requestPlayerMove(Player p) {
		try {
			cntrl.makeMove(p);
		} catch (GameError e) {
			return false;
		}
		return true;
	}
	
	/**
	 * Request a random move for the player in the turn. 
	 * May be random or AI.
	 * @return whether the move was successful or not
	 */
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
	 * Inits colors for players. Gives them random colors if they have no color.
	 * @param pieces list of pieces to add a color
	 */
	private void initDefaultColors(List<Piece> pieces) {
		for(Piece p : pieces) {
			if(!pieceColors.containsKey(p)) {
				pieceColors.put(p, Utils.randomColor());
			}
		}
	}
	
	/**
	 * Sets the window title with the game's description and 
	 * owner's name (if any).
	 * @param gameDesc
	 */
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
		this.gameState = State.InPlay;
		initDefaultColors(pieces);
		initPlayers(pieces);
		
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					handleGameStart(board, gameDesc, pieces, turn);
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		try {
			boardComponent.setEnabled(false);
			
		} catch(Exception e) {
			
		} finally { //Ensure game closes
			if(state == State.Stopped) {
				WINDOW.setVisible(false);
				WINDOW.dispose();
				SwingView.currentlyOpen--;
			}
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
		this.board = board;
		this.turn = turn;
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
		this.board = board;
		this.turn = turn;
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
		this.board = board;
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
	
	/**
	 * Sets default window size for the window game.
	 * By default half the screen width and 3/5 the screen height.
	 */
	private void setDefaultWindowSize() {
		Rectangle screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		WINDOW.setSize(screenSize.width/2, screenSize.height*3/5);
		WINDOW.setMinimumSize(new Dimension(screenSize.width/2, screenSize.height/2));
	}
	
	/**
	 * Sets the default location for the windows.
	 * Scatters them across the four quadrants on the screen if in multiviews.
	 */
	private void setDefaultLocation() {
		if(SwingView.currentlyOpen > 4) {
			WINDOW.setLocationByPlatform(true);
			throw new RuntimeException("Too many views open at the same time.");
		}
		
		if(WINDOW_OWNER == null) {
			WINDOW.setLocationByPlatform(true);
		} else {
			Rectangle screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
			int col = SwingView.currentlyOpen % 2;
			int row = SwingView.currentlyOpen / 2;
			WINDOW.setLocation(col*(screenSize.width/2), row*(screenSize.height/2));
		}
	}
	
	/**
	 * Adapts widgets look and feel to system's instead of the default one. 
	 */
	private static void setDefaultLookAndFeel() {
		try {
			javax.swing.UIManager.setLookAndFeel(
					javax.swing.UIManager.getSystemLookAndFeelClassName());
	    } catch (Exception e) {
	    	System.err.println("Look and feel mode: " + e.getMessage());
		}
	}
	

}
