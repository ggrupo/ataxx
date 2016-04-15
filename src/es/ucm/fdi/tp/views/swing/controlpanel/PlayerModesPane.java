package es.ucm.fdi.tp.views.swing.controlpanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class PlayerModesPane extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 7482372784681144842L;

	public PlayerModesPane() {
		JButton setButton = new JButton(" Set ");
		setButton.addActionListener(this);
		this.add(setButton);
		
		this.setBorder(new TitledBorder("Player modes"));
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("Clicked change mode button!");
	}
}
