package es.ucm.fdi.tp.views.swing.controlpanel;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;

public class AutomaticMoves extends JPanel implements ActionListener{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7869356863128774061L;
	
	private JButton randomButton;
	private JButton inteligentButton;
	
	private Controller cntrl;
	private Player randomPlayer;
	private Player inteligentPlayer;
	
	public AutomaticMoves(Controller c, Player randomPlayer, Player inteligentPlayer) {
		super(new FlowLayout());
		this.cntrl = c;
	
		this.setBorder(new TitledBorder("Automatic Moves"));
		
		this.randomPlayer = randomPlayer;
		this.inteligentPlayer = inteligentPlayer;
		
		this.randomButton = new JButton(" Random ");
		this.randomButton.addActionListener(this);
		this.add(randomButton);
		
		this.inteligentButton = new JButton(" Inteligent ");
		this.inteligentButton.addActionListener(this);
		this.add(inteligentButton);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton target = (JButton) e.getSource();
		if(target == this.randomButton) {
			cntrl.makeMove(randomPlayer);
		} else if (target == this.inteligentButton) {
			cntrl.makeMove(inteligentPlayer);
		}
	}
	
}
