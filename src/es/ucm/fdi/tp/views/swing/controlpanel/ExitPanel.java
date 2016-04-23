package es.ucm.fdi.tp.views.swing.controlpanel;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.views.swing.SwingView;

public class ExitPanel extends JPanel implements ActionListener, GameObserver {
	
	private static final long serialVersionUID = 967508544579472464L;
	
	private JButton exitButton;
	private JButton restartButton;
	
	private SwingView view;
	private Piece WINDOW_OWNER;
	
	/**
	 * A panel containg an exit button and a restart button if restartButton
	 * is true.
	 * @param c - controller over which actions will be applied
	 * @param restartOption - designates wether the restart button should be 
	 * visible or not
	 */
	public ExitPanel(SwingView v) {
		super(new FlowLayout());
		this.view = v;
		this.WINDOW_OWNER = v.getWindowOwner();
		
		this.exitButton = new JButton(" Quit ");
		this.exitButton.addActionListener(this);
		this.add(exitButton);
		
		if(WINDOW_OWNER == null) {
			this.restartButton = new JButton(" Restart ");
			this.restartButton.addActionListener(this);
			this.add(restartButton);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton target = (JButton) e.getSource();
		if(target == this.exitButton) {
			view.requestCloseGame();
		} else if (target == this.restartButton) {
			view.requestRestartGame();
		}
	}
	
	public void setEnabled(boolean b) {
		super.setEnabled(b);
		exitButton.setEnabled(b);
		if(restartButton != null) {
			restartButton.setEnabled(b);
		}
	}

	@Override
	public void onGameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn) {
		boolean enable = (turn.equals(WINDOW_OWNER) || (WINDOW_OWNER == null));
		this.setEnabled(enable);
	}

	@Override
	public void onGameOver(Board board, State state, Piece winner) {
		this.setEnabled(true);
	}

	@Override
	public void onMoveStart(Board board, Piece turn) {
		this.setEnabled(false);
	}

	@Override
	public void onMoveEnd(Board board, Piece turn, boolean success) {
		boolean enable = (turn.equals(WINDOW_OWNER) || (WINDOW_OWNER == null));
		this.setEnabled(enable);
	}

	@Override
	public void onChangeTurn(Board board, Piece turn) {
		boolean enable = (turn.equals(WINDOW_OWNER) || (WINDOW_OWNER == null));
		this.setEnabled(enable);
	}

	@Override
	public void onError(String msg) {}

}
