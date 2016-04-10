package es.ucm.fdi.tp.views.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import es.ucm.fdi.tp.basecode.bgame.control.*;
import es.ucm.fdi.tp.basecode.bgame.model.*;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.views.swing.boardpanel.JBoard;
import es.ucm.fdi.tp.views.swing.controlpanel.ControlPanel;

public abstract class SwingView extends JFrame implements GameObserver {
	
	static {
		setDefaultLookAndFeel();
	}

	private static final long serialVersionUID = -5822298297801045598L;
	
	protected Controller cntrl;
	private Player randPlayer;
	private Player aiPlayer;
	private Board board; //A read only board
	
	final private Piece WINDOW_OWNER;
	private Piece turn;
	final protected Map<Piece,Color> pieceColors = new HashMap<Piece,Color>();
	final protected Map<Piece, PlayerMode> playerModes = new HashMap<Piece, PlayerMode>(pieces.size());
	protected List<Piece> pieces;
	
	private ControlPanel controlPanelComponent;
	protected JBoard boardComponent;
	
	private static final Color[] DEF_COLORS = {
			Color.RED, 
			Color.BLUE, 
			Color.GREEN, 
			Color.YELLOW
	};

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
			return id;
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
		
		
		
		
		g.addObserver(this);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setDefaultSize();
	}
	
	public abstract void setEnabled(boolean b);
	
	protected abstract JBoard createBoard();
	
	protected abstract void redrawBoard();
	
	final protected void initGUI() {
		JPanel container = new JPanel(new BorderLayout(0,0));
		
		this.boardComponent = createBoard();
		getContentPane().add(boardComponent, BorderLayout.CENTER);
		
		this.controlPanelComponent = new ControlPanel(cntrl,this,playerModes,pieces);
		container.add(controlPanelComponent, BorderLayout.LINE_END);
		
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
	
	
	private void initDefaultColors(List<Piece> pieces) {
		for(char i=0; i<pieces.size(); i++) {
			pieceColors.put(pieces.get(i), DEF_COLORS[i]);
		}
	}
	private void initWindowTitle(String gameDesc) {
		String owner = WINDOW_OWNER != null ? " (" + WINDOW_OWNER + ")" : "";
		this.setTitle("Board Games: " + gameDesc + owner);
	}
	
	private void showTurn() {
		if(turn.equals(WINDOW_OWNER)) {
			this.controlPanelComponent.showMessage("Your turn!");
			this.requestFocus(); //Window becomes focused
		} else {
			this.controlPanelComponent.showMessage("Turn for " + turn);
		}
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
		
		//Build GUI
		initGUI();
		initWindowTitle(gameDesc);
		
		//Do things
		this.controlPanelComponent.showMessage(null);
		this.controlPanelComponent.showMessage("Game started");
		showTurn();
	}

	@Override
	public void onGameOver(Board board, State state, Piece winner) {
		// TODO Auto-generated method stub
		this.controlPanelComponent.showMessage("Game ended");
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
		// TODO Auto-generated method stub
		this.turn = turn;
		showTurn();
		//TODO update board view
		//TODO update control panel (disable buttons or whatever)
	}

	@Override
	public void onError(String msg) {
		new ErrorDialog(msg, this);
	}
	
	private void setDefaultSize() {
		Rectangle screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		this.setMinimumSize(new Dimension(screenSize.width/2, screenSize.height/2));
	}
	
	final protected static void setDefaultLookAndFeel() {
		try {
			javax.swing.UIManager.setLookAndFeel(
					javax.swing.UIManager.getSystemLookAndFeelClassName());
	    } catch (Exception e) {
	    	System.err.println("Look and feel mode: " + e.getMessage());
		}
	}

}
