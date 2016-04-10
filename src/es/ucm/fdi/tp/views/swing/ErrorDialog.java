package es.ucm.fdi.tp.views.swing;

import java.awt.Window;
import javax.swing.JOptionPane;


public class ErrorDialog {
	
	final protected static String DIALOG_TITLE = "Error"; 
	
	public ErrorDialog(String error, Window parent) {
		JOptionPane.showMessageDialog(
				parent, error,
				DIALOG_TITLE, JOptionPane.ERROR_MESSAGE);
	}
}
