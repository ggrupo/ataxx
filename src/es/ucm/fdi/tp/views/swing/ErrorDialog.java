package es.ucm.fdi.tp.views.swing;

import java.awt.Window;
import javax.swing.JOptionPane;

/**
 * A dialog that shows an error message.
 * @author german
 *
 */
public class ErrorDialog {
	
	final protected static String DIALOG_TITLE = "Error"; 
	
	/**
	 * Launches a dialog with an error message.
	 * @param error - Error message in a string.
	 * @param parent - Parent window or null. If no specified 
	 * the dialog won't be centered on any window.
	 */
	public ErrorDialog(String error, Window parent) {
		JOptionPane.showMessageDialog(
				parent, error,
				DIALOG_TITLE, JOptionPane.ERROR_MESSAGE);
	}
}
