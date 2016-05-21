package es.ucm.fdi.tp.views.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.control.multiplayer.GameServer;
import es.ucm.fdi.tp.control.multiplayer.NetObserver;
import es.ucm.fdi.tp.views.swing.controlpanel.textbox.MessagesBox;

public class ServerView implements NetObserver {
	
	static {
		setDefaultLookAndFeel();
	}
	
	private final JFrame WINDOW;
	private MessagesBox textarea;
	
	private GameServer server;
	
	public ServerView(GameServer s) {
		this.WINDOW = new JFrame();
		this.server = s;
		server.setView(this);
		try {
			SwingUtilities.invokeAndWait(new Runnable() {

				@Override
				public void run() {
					initGUI();
				}
				
			});
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	private void initGUI() {
		JPanel panel = new JPanel(new BorderLayout());
		
		this.textarea = new MessagesBox(8, 32);
		panel.add(textarea, BorderLayout.CENTER);
		
		JButton quitBtn = new JButton("Disconnect all");
		panel.add(quitBtn, BorderLayout.SOUTH);
		quitBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				confirmAndExit();
				
			}
		});
		
		WINDOW.setContentPane(panel);
		WINDOW.setMinimumSize(new Dimension(512,256));
		WINDOW.setLocationByPlatform(true);
		
		WINDOW.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		WINDOW.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				confirmAndExit();
			}
		});
	}

	@Override
	public void onPlayerConnected(Piece p) {
		log("Player " + p + " connected (" + 
				server.playersConnected()  + "/" + server.requiredPlayers() + ")" );
	}

	@Override
	public void onPlayerDisconected(Piece p) {
		log("Player " + p + " disconnected (" + 
				server.playersConnected()  + "/" + server.requiredPlayers() + ")" );
	}

	@Override
	public void onServerOpened(String gameDesc) {
		WINDOW.setTitle("Server: " + gameDesc);
		WINDOW.setVisible(true);
		
		log("Server started. Waiting for players... (" 
				+ server.playersConnected()  + "/" + server.requiredPlayers() + ")");
	}

	@Override
	public void onServerClosed() {
		WINDOW.setVisible(false);
		WINDOW.dispose();
	}
	
	public void log(String msg) {
		textarea.append(msg);
	}
	
	private void confirmAndExit() {
		if((new QuitDialog(WINDOW)).getValue())
			server.stop();
	}
	
	private static void setDefaultLookAndFeel() {
		try {
			javax.swing.UIManager.setLookAndFeel(
					javax.swing.UIManager.getSystemLookAndFeelClassName());
	    } catch (Exception e) {
	    	System.err.println("Look and feel mode: " + e.getMessage());
		}
	}
}
