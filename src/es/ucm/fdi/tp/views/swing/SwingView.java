package es.ucm.fdi.tp.views.swing;

import java.awt.BorderLayout;
import java.awt.Color;
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
	protected Map<Piece,Color> pieceColors;
	protected Map<Piece, PlayerMode> playerModes;
	
	private ControlPanel cntrlPanel;
	
	private static final Color[] DEF_COLORS = {
			Color.RED, 
			Color.BLUE, 
			Color.GREEN, 
			Color.YELLOW
	};

	/**
	 * Player modes (manual, random, etc.)
	 */
	enum PlayerMode {
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
		
		initGUI();
		
		g.addObserver(this);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public abstract void setEnabled(boolean b);
	
	protected abstract JBoard createBoard();
	
	protected abstract void redrawBoard();
	
	final protected void initGUI() {
		JPanel container = new JPanel(new BorderLayout(0,0));
		
		this.cntrlPanel = new ControlPanel(cntrl);
		container.add(cntrlPanel, BorderLayout.LINE_END);
		
		this.setContentPane(container);
	}
	
	final protected void initBoard(Board board) {
		this.board = board;
		getContentPane().add(createBoard(), BorderLayout.CENTER);
	}
	
	final protected Color getPieceColor(Piece p) { 
		return pieceColors.get(p); 
	} 
	
	final protected Color setPieceColor(Piece p, Color c) { 
		return pieceColors.put(p,c); 
	}
	
	final protected Board getBoard() { 
		return this.board; 
	}
	
	
	private void initDefaultColors(List<Piece> pieces) {
		this.pieceColors = new HashMap<Piece,Color>();
		for(char i=0; i<pieces.size(); i++) {
			pieceColors.put(pieces.get(i), DEF_COLORS[i]);
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
	private void initPlayerModes(List<Piece> pieces) {
		playerModes = new HashMap<Piece, PlayerMode>(pieces.size());
		for(Piece p: pieces) {
			playerModes.put(p, PlayerMode.MANUAL);
		}
	}

	@Override
	public void onGameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn) {
		// TODO Auto-generated method stub
		this.turn = turn;
		initDefaultColors(pieces);
		initWindowTitle(gameDesc);
		initPlayerModes(pieces);
		initBoard(board);
		this.cntrlPanel.showMessage(null);
		this.cntrlPanel.showMessage("Game started");
		showTurn();
	}

	@Override
	public void onGameOver(Board board, State state, Piece winner) {
		// TODO Auto-generated method stub
		this.cntrlPanel.showMessage("Game ended");
	}

	@Override
	public void onMoveStart(Board board, Piece turn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMoveEnd(Board board, Piece turn, boolean success) {
		// TODO Auto-generated method stub
		
	}
	
	private void showTurn() {
		if(turn.equals(WINDOW_OWNER)) {
			this.cntrlPanel.showMessage("Your turn!");
			this.requestFocus(); //Window becomes focused
		} else {
			this.cntrlPanel.showMessage("Turn for " + turn);
		}
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
		// TODO show alert box
		
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
