package es.ucm.fdi.tp.views.swing.controlpanel;

import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.views.swing.QuitDialog;

public class ExitPane extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 967508544579472464L;
	
	private JButton exitButton;
	private JButton restartButton;
	
	private Controller cntrl;
	
	/**
	 * A panel containg an exit button and a restart button.
	 * @param c - controller over which actions will be applied
	 */
	public ExitPane(Controller c) {
		this(c,true);
	}
	
	/**
	 * A panel containg an exit button and a restart button if restartButton
	 * is true.
	 * @param c - controller over which actions will be applied
	 * @param restartOption - designates wether the restart button should be 
	 * visible or not
	 */
	public ExitPane(Controller c, boolean restartOption) {
		super(new FlowLayout());
		this.cntrl = c;
		
		this.exitButton = new JButton(" Quit ");
		this.exitButton.addActionListener(this);
		this.add(exitButton);
		
		if(restartOption) {
			this.restartButton = new JButton(" Restart ");
			this.restartButton.addActionListener(this);
			this.add(restartButton);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton target = (JButton) e.getSource();
		if(target == this.exitButton) {
			Window parent = SwingUtilities.getWindowAncestor(this);
			if(new QuitDialog(parent).getValue()) {
				cntrl.stop();
			}
		} else if (target == this.restartButton) {
			cntrl.restart();
		}
	}
	
	public void setEnabled(boolean b) {
		super.setEnabled(b);
		exitButton.setEnabled(b);
		if(restartButton != null) {
			restartButton.setEnabled(b);
		}
	}

}
