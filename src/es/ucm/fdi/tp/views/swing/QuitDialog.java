package es.ucm.fdi.tp.views.swing;

import java.awt.Window;
import javax.swing.JOptionPane;

public class QuitDialog {

	final protected static String DIALOG_TITLE = "Quit"; 
	final protected static String DIALOG_MESSAGE = "Are you sure you want to quit?";
	
	private boolean accepted;
	
	public QuitDialog(Window parent) {
		int op = JOptionPane.showConfirmDialog(
				parent, DIALOG_MESSAGE,DIALOG_TITLE, 
				JOptionPane.YES_NO_OPTION);
		
		accepted = (op == JOptionPane.YES_OPTION);
	}
	
	public boolean getValue() {
		return accepted;
	}
	
}
