package es.ucm.fdi.tp.views.swing;

import java.awt.Window;
import javax.swing.JOptionPane;

/**
 * A dialog that asks for confirmation on quitting.
 *
 */
public class QuitDialog {

	final protected static String DIALOG_TITLE = "Quit"; 
	final protected static String DIALOG_MESSAGE = "Are you sure you want to quit?";
	
	private boolean accepted;
	
	/**
	 * Launches a dialog asking for confirmation on exit.
	 * @param parent - Parent window or null. If no specified 
	 * the dialog won't be centered on any window.
	 */
	public QuitDialog(Window parent) {
		if(parent !=null)
			parent.toFront();
		
		int op = JOptionPane.showConfirmDialog(
				parent, DIALOG_MESSAGE,DIALOG_TITLE, 
				JOptionPane.YES_NO_OPTION);
		
		accepted = (op == JOptionPane.YES_OPTION);
	}
	
	/**
	 * Returns whether it was clicked YES or NOT.
	 * @return true if clicked YES, false otherwise
	 */
	public boolean getValue() {
		return accepted;
	}
	
}
