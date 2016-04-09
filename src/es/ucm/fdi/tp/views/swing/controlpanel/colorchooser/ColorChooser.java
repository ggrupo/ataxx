package es.ucm.fdi.tp.views.swing.controlpanel.colorchooser;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

public class ColorChooser extends JDialog {

	private static final long serialVersionUID = 1L;

	private JColorChooser colorChooser;
	private Color color;

	public ColorChooser(JFrame parent, String title, Color initColor) {
		super(parent, title);
		setModalityType(DEFAULT_MODALITY_TYPE);//<-- Stops parent window execution
		colorChooser = new JColorChooser(initColor == null ? Color.WHITE : initColor);

		getContentPane().add(colorChooser);

		// Create button panel
		JPanel buttonPane = new JPanel();
		
		JButton buttonOK = new JButton("OK");
		buttonOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				color = colorChooser.getColor();
				closeDialog();
			}
		});
		buttonPane.add(buttonOK);

		JButton buttonCancel = new JButton("Cancel");
		buttonCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				closeDialog();
			}
		});
		buttonPane.add(buttonCancel);

		setResizable(false);
		getContentPane().add(buttonPane, BorderLayout.PAGE_END);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		centerWindow(parent);
		setVisible(true);
	}
	
	private void centerWindow(JFrame parent) {
		setLocationByPlatform(true);
		setLocationRelativeTo(parent);
	}

	private void closeDialog() {
		setVisible(false);
		dispose();
	}

	// override the createRootPane inherited by the JDialog, to create the
	// rootPane.
	// create functionality to close the window when "Escape" button is pressed
	public JRootPane createRootPane() {
		JRootPane rootPane = new JRootPane();
		KeyStroke stroke = KeyStroke.getKeyStroke("ESCAPE");

		@SuppressWarnings("serial")
		Action action = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				closeDialog();
			}
		};
		InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap.put(stroke, "ESCAPE");
		rootPane.getActionMap().put("ESCAPE", action);
		return rootPane;
	}

	public Color getColor() {
		return color;
	}

}