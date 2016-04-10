package es.ucm.fdi.tp.views.swing.controlpanel;

import java.awt.Dimension;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.views.swing.SwingView;
import es.ucm.fdi.tp.views.swing.controlpanel.textbox.MessagesBox;

public class ControlPanel extends JPanel {

	private static final long serialVersionUID = -5012854304785344947L;
	
	final private Controller cntrl; 
	final private Board board;
	final private SwingView view;
	final private List<Piece> pieces;
	final private Map<Piece,SwingView.PlayerMode> modosdeJuego;
	
	private MessagesBox messagesBox;
	
	
	public ControlPanel(Controller c, Board board, SwingView v, 
		Map<Piece,SwingView.PlayerMode> playerModes, List<Piece> pieces) 
	{
		this.cntrl = c;
		this.board = board;
		this.view = v;
		this.pieces = pieces;
		this.modosdeJuego = playerModes;
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		initGUI();
	}
	final private void initGUI() {
		addMessagesBox();
		addPlayerInfoTable();
		addExitPane();
	}
	
	private void addPlayerInfoTable(){
		PlayerInformation infoTable = new PlayerInformation(board, view, modosdeJuego, pieces);
		infoTable.setMaximumSize(
				new Dimension(Integer.MAX_VALUE, infoTable.getHeight()));
		this.add(infoTable);
	}
	
	private void addExitPane() {
		ExitPane exit = new ExitPane(this.cntrl);
		exit.setMaximumSize(
				new Dimension(Integer.MAX_VALUE, exit.getHeight()));
		this.add(exit);
	}
	
	private void addMessagesBox() {
		messagesBox = new MessagesBox();
		this.add(messagesBox);
	}
	
	public void showMessage(String m) {
		if(m != null)
			this.messagesBox.append(m);
		else
			this.messagesBox.setText(null);
	}
}
