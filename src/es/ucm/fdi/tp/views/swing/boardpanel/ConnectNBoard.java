package es.ucm.fdi.tp.views.swing.boardpanel;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;

import es.ucm.fdi.tp.basecode.bgame.control.Controller;
import es.ucm.fdi.tp.basecode.bgame.model.Board;
import es.ucm.fdi.tp.basecode.bgame.model.FiniteRectBoard;
import es.ucm.fdi.tp.basecode.bgame.model.Game.State;
import es.ucm.fdi.tp.views.swing.boardpanel.pieces.CirclePiece;
import es.ucm.fdi.tp.views.swing.boardpanel.pieces.ObstaclePiece;
import es.ucm.fdi.tp.views.swing.boardpanel.pieces.PieceListener;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;

public class ConnectNBoard extends JFiniteRectBoard {

	private static final long serialVersionUID = -4187463114811199234L;

	public ConnectNBoard(FiniteRectBoard board, Controller c, Map<Piece,Color> colors) {
		super(board,c,colors);
		initBoard(board);
	}
	
	private void initBoard(FiniteRectBoard board) {
		Piece curr = null;
		for(int i=0; i<rows; i++) {
			for(int j=0; j<cols; j++) {
				curr = board.getPosition(i, j);
				if(curr == null) {
					casillas[i][j] = new CirclePiece(null);
				} else {
					if(pieceColors.containsKey(curr))
						casillas[i][j] = new CirclePiece(pieceColors.get(curr));
					else
						casillas[i][j] = new ObstaclePiece();
				}
				this.add(casillas[i][j]);
				casillas[i][j].addPieceListener(new PieceListener() {
					
					@Override
					public void mouseRightClicked(MouseEvent e) {
						// TODO Auto-generated method stub
						System.out.println("Clicked right button");
					}
					
					@Override
					public void mouseLeftClicked(MouseEvent e) {
						// TODO Auto-generated method stub
						System.out.println("Clicked left button");
					}
				});
			}
		}
	}

	@Override
	public void onGameStart(Board board, String gameDesc, List<Piece> pieces, Piece turn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameOver(Board board, State state, Piece winner) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMoveStart(Board board, Piece turn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMoveEnd(Board board, Piece turn, boolean success) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onChangeTurn(Board board, Piece turn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(String msg) {
		// TODO Auto-generated method stub
		
	}

}
