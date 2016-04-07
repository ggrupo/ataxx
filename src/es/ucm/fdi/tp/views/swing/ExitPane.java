package es.ucm.fdi.tp.views.swing;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;

public class ExitPane extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 967508544579472464L;
	
	private JButton exitButton;
	private JButton restartButton;
	
	private Controller cntrl;
	
	public ExitPane(Controller c) {
		super(new FlowLayout());
		this.cntrl = c;
		
		this.exitButton = new JButton(" Quit ");
		this.exitButton.addActionListener(this);
		this.add(exitButton);
		
		this.restartButton = new JButton(" Restart ");
		this.restartButton.addActionListener(this);
		this.add(restartButton);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton target = (JButton) e.getSource();
		if(target == this.exitButton) {
			cntrl.stop();
		} else if (target == this.restartButton) {
			cntrl.restart();
		}
	}

}
