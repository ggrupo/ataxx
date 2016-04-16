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
import java.util.Iterator;
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

import es.ucm.fdi.tp.basecode.bgame.Utils;
import es.ucm.fdi.tp.basecode.bgame.control.*;
import es.ucm.fdi.tp.basecode.bgame.model.*;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.views.swing.boardpanel.BoardComponent;
import es.ucm.fdi.tp.views.swing.controlpanel.ControlPanel;
import es.ucm.fdi.tp.views.swing.controlpanel.colorchooser.ColorChangeObserver;

public abstract class SwingView extends JFrame implements GameObserver, ColorChangeObserver {
	
	static {
		setDefaultLookAndFeel();
	}

	private static final long serialVersionUID = -5822298297801045598L;
	
	protected Controller cntrl;
	final private Player randPlayer;
	final private Player aiPlayer;
	private Board board; //A read only board
	
	private Piece turn;
	final private Piece WINDOW_OWNER;
	final protected Map<Piece,Color> pieceColors = new HashMap<Piece,Color>();
	final protected Map<Piece, PlayerMode> playerModes = new HashMap<Piece, PlayerMode>();
	protected List<Piece> pieces;
	
	private ControlPanel controlPanelComponent;
	protected BoardComponent boardComponent;

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
	
	public SwingView(Observable<GameObserver> g, Controller c, Piece localPiece, 
			Player randPlayer, Player aiPlayer)
	{
		this.cntrl= c;
		this.randPlayer = randPlayer;
		this.aiPlayer = aiPlayer;
		
		this.WINDOW_OWNER = localPiece;
		this.turn = null;
		
		initGUI(g);
		
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
		
		g.addObserver(this);
	}
	
	public abstract void setEnabled(boolean b);
	
	protected abstract BoardComponent createBoard();
	
	protected final void redrawBoard() {
		this.boardComponent.redraw();
	}
	
	private void initGUI(Observable<GameObserver> g) {
		JPanel container = new JPanel(new BorderLayout(0,0));
		
		this.controlPanelComponent = new ControlPanel(cntrl,this,playerModes,pieceColors, randPlayer, aiPlayer);
		container.add(controlPanelComponent, BorderLayout.LINE_END);
		g.addObserver(controlPanelComponent);
		
		this.boardComponent = createBoard();
		container.add(boardComponent, BorderLayout.CENTER);
		g.addObserver(boardComponent);
		
		this.setContentPane(container);
	}

	
	final public Color getPieceColor(Piece p) { 
		return pieceColors.get(p); 
	} 
	
	final protected Color setPieceColor(Piece p, Color c) { 
		return pieceColors.put(p,c); 
	}
	
	final protected Board getBoard() {
		return this.board; 
	}
	
	public final Piece getWindowOwner() {
		return WINDOW_OWNER;
	}
	
	final public void showMessage(String m) {
		this.controlPanelComponent.showMessage(m);
	}
	
	/**
	 * Closes the game after asking for confirmation.
	 */
	public void closeGame() {
		if((new QuitDialog(SwingView.this)).getValue()) {
			SwingView.this.cntrl.stop();
		}
	}
	
	protected void initDefaultColors(List<Piece> pieces) {
		Iterator<Color> colorGen = Utils.colorsGenerator();
		for(Piece p : pieces) {
			pieceColors.put(p, colorGen.next());
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
		this.pieces = pieces;
		for(Piece p: pieces) {
			playerModes.put(p, PlayerMode.MANUAL);
		}
	}
	
	
	@Override
	public void onGameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn) {
		//Init everything
		this.turn = turn;
		this.board = board;
		initDefaultColors(pieces);
		initPlayers(pieces);

		initWindowTitle(gameDesc);
	}

	@Override
	public void onGameOver(Board board, State state, Piece winner) {
		if(state == State.Stopped) {
			this.setVisible(false);
			this.dispose();
		}
	}

	@Override
	public void onMoveStart(Board board, Piece turn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMoveEnd(Board board, Piece turn, boolean success) {
		redrawBoard();
	}
	
	@Override
	public void onChangeTurn(Board board, Piece turn) {
		this.turn = turn;
		//TODO update board view
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
