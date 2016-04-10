package es.ucm.fdi.tp.views.swing.controlpanel;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.views.swing.SwingView;
import es.ucm.fdi.tp.views.swing.controlpanel.textbox.MessagesBox;

public class ControlPanel extends JPanel {

	private static final long serialVersionUID = -5012854304785344947L;
	
	final private Controller cntrl; 
	final private Board board;
	final private SwingView view;
	
	private MessagesBox messagesBox;
	
	
	public ControlPanel(Controller c, Board b, SwingView v) {
		this.cntrl = c;
		this.board = b;
		this.view = v;
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		initGUI();
	}
	final private void initGUI() {
		addMessagesBox();
		addPlayerInformation();
		addExitPane();
	}
	
	private void addPlayerInformation(){
		PlayerInformation player = new PlayerInformation(board, view, null, null);
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
