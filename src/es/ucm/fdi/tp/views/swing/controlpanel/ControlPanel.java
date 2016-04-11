package es.ucm.fdi.tp.views.swing.controlpanel;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.views.swing.SwingView;
import es.ucm.fdi.tp.views.swing.controlpanel.colorchooser.ColorChooserPane;
import es.ucm.fdi.tp.views.swing.controlpanel.textbox.MessagesBox;

public class ControlPanel extends JPanel {

	private static final long serialVersionUID = -5012854304785344947L;
	
	final private Controller cntrl;
	final private Board board;
	final private SwingView view;
	final private List<Piece> pieces;
	final private Map<Piece,SwingView.PlayerMode> modosdeJuego;
	final private Map<Piece,Color> playerColors;
	
	private MessagesBox messagesBox;
	private PlayersInfoTable infoTable;
	private ColorChooserPane colorChooser;
	
	
	public ControlPanel(Controller c, Board board, SwingView v, 
		Map<Piece,SwingView.PlayerMode> playerModes, 
		Map<Piece, Color> playerColors, List<Piece> pieces) 
	{
		this.cntrl = c;
		this.board = board;
		this.view = v;
		this.pieces = pieces;
		this.modosdeJuego = playerModes;
		this.playerColors = playerColors;
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		initGUI();
	}
	final private void initGUI() {
		addMessagesBox();
		addPlayerInfoTable();
		addColorChangePane();
		addAutoMovesPane();
		addExitPane();
		
		this.colorChooser.addObserver(this.infoTable);
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
		this.colorChooser = new ColorChooserPane(pieces,playerColors, view.getWindowOwner());
		colorChooser.setMaximumSize(
				new Dimension(Integer.MAX_VALUE, colorChooser.getHeight()));
		this.add(colorChooser);
	}
	
	private void addAutoMovesPane() {
		AutomaticMoves autoMovesPane = new AutomaticMoves(cntrl, null, null);
		autoMovesPane.setMaximumSize(
				new Dimension(Integer.MAX_VALUE, autoMovesPane.getHeight()));
		this.add(autoMovesPane);
	}
	
	private void addExitPane() {
		ExitPane exit = new ExitPane(this.cntrl);
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
}
