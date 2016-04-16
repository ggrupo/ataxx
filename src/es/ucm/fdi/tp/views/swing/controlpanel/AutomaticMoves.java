package es.ucm.fdi.tp.views.swing.controlpanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.control.Player;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

public class AutomaticMoves extends JPanel implements ActionListener, GameObserver {

	private static final long serialVersionUID = 7869356863128774061L;
	
	private JButton randomButton;
	private JButton aiButton;
	
	private Controller cntrl;
	private Player randomPlayer;
	private Player aiPlayer;
	
	private final Piece WINDOW_OWNER;
	
	public AutomaticMoves(Controller c, Player randomPlayer, Player aiPlayer, Piece windowOwner) {
		this.cntrl = c;
		this.WINDOW_OWNER = windowOwner;
		this.randomPlayer = randomPlayer;
		this.aiPlayer = aiPlayer;
		
		this.randomButton = new JButton(" Random ");
		if(randomPlayer != null) {
			this.randomButton.addActionListener(this);
			this.add(randomButton);
		}
		
		this.aiButton = new JButton(" Inteligent ");
		if(aiPlayer != null) {
			this.aiButton.addActionListener(this);
			this.add(aiButton);
		}
		
		this.setBorder(new TitledBorder("Automatic Moves"));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton target = (JButton) e.getSource();
		if(target == this.randomButton) {
			cntrl.makeMove(randomPlayer);
		} else if (target == this.aiButton) {
			cntrl.makeMove(aiPlayer);
		}
	}
	
	@Override
	public void setEnabled(boolean b) {
		super.setEnabled(b);
		randomButton.setEnabled(b);
		aiButton.setEnabled(b);
	}

	@Override
	public void onGameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn) {
		boolean enable = (WINDOW_OWNER == null) || turn.equals(WINDOW_OWNER);
		this.setEnabled(enable);
	}

	@Override
	public void onGameOver(Board board, State state, Piece winner) {
		this.setEnabled(false);
	}

	@Override
	public void onMoveStart(Board board, Piece turn) {
		this.setEnabled(false);
	}

	@Override
	public void onMoveEnd(Board board, Piece turn, boolean success) {
		boolean enable = (WINDOW_OWNER == null) || turn.equals(WINDOW_OWNER);
		this.setEnabled(enable);
	}

	@Override
	public void onChangeTurn(Board board, Piece turn) {
		boolean enable = (WINDOW_OWNER == null) || turn.equals(WINDOW_OWNER);
		this.setEnabled(enable);
	}
	
	@Override
	public void onError(String msg) {}
	
}
