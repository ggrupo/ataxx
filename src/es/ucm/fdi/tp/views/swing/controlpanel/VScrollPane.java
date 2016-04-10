package es.ucm.fdi.tp.views.swing.controlpanel;

import java.awt.Component;
import javax.swing.JScrollPane;

public class VScrollPane extends JScrollPane {

	private static final long serialVersionUID = -1372663418223129610L;
	
	/**
	 * Creates a JScrollPane that displays the view component in a viewport 
	 * whose view position can be controlled with a vertical scrollbar.
	 * @param c - the component to display in the scrollpanes viewport
	 */
	public VScrollPane(Component c) {
		super(c, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	}
	
	/**
	 * Creates an empty (no viewport view) VScrollPane where vertical scrollbar 
	 * appears when needed. No horizontal scrollbar.
	 */
	public VScrollPane() {
		super();
		this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	}
	
	@Override
	public Component add(Component comp) {
		return this.getViewport().add(comp);
	}
}
