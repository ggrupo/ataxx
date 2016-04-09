package es.ucm.fdi.tp.views.swing;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;

public class QuitDialog extends JDialog implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4321185942579015448L;
	
	private JButton yesButton;
	private JButton notButton;
	private JLabel pregunta;
	private JPanel panelPregunta;
	private JPanel panelBotones;
	private JPanel panel;
	
	private Controller cntrl;
	
	public QuitDialog(String tittle, Controller c){
		super.setTitle(tittle);
		this.cntrl = c;
		setSize(300,100);
		setLocationRelativeTo(null);
		panelPregunta = new JPanel();
		
		this.pregunta = new JLabel("Are sure you want to quit?");
		panelPregunta.add(pregunta);
		
		panelBotones = new JPanel();
		
		this.yesButton = new JButton(" Yes ");
		this.yesButton.addActionListener(this);
		panelBotones.add(yesButton);
		
		this.notButton = new JButton(" Not ");
		this.notButton.addActionListener(this);
		panelBotones.add(notButton);
		
		panel = new JPanel(new BorderLayout());
		panel.add(panelPregunta, BorderLayout.NORTH);
		panel.add(panelBotones,BorderLayout.SOUTH);
	    this.add(panel);
	    
	    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	    setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton target = (JButton) e.getSource();
		if(target == this.yesButton) {
			dispose();
			cntrl.stop();
		} else if (target == this.notButton) {
			dispose();
		}
		
	}
	
}
