package es.ucm.fdi.tp.views.swing.controlpanel.textbox;

import javax.swing.JTextArea;

import es.ucm.fdi.tp.views.swing.controlpanel.VScrollPane;

public class ScrollableTextarea extends VScrollPane {

	private static final long serialVersionUID = 6933014250472432783L;
	
	protected JTextArea textarea;
	
	/**
	 * Constructs a new ScrollableTextArea. A default model is set, the initial 
	 * string is null, and rows/columns are set to 0.
	 */
	public ScrollableTextarea() {
		textarea = new JTextArea();
		this.formatTextArea();
		
		this.setViewportView(textarea);
	}
	
	/**
	 * Constructs a new ScrollableTextArea with the specified text displayed. 
	 * A default model is created and rows/columns are set to 0.
	 * @param text
	 */
	public ScrollableTextarea(String text) {
		textarea = new JTextArea(text);
		this.formatTextArea();
		this.setViewportView(textarea);
	}
	
	/**
	 * Constructs a new empty ScrollableTextArea with the specified number of 
	 * rows and columns. A default model is created, and the initial string is null.
	 * @param rows - the number of rows >= 0
	 * @param cols - the number of columns >= 0
	 */
	public ScrollableTextarea(int rows, int cols) {
		textarea = new JTextArea(rows, cols);
		this.formatTextArea();
		this.setViewportView(textarea);
	}
	
	/**
	 * Constructs a new ScrollableTextArea with the specified text and number of 
	 * rows and columns. A default model is created. It can be vertically 
	 * scrolled if needed.
	 * @param text - the text to be displayed, or null
	 * @param rows - the number of rows >= 0
	 * @param cols - the number of columns >= 0
	 */
	public ScrollableTextarea(String text, int rows, int cols) {
		textarea = new JTextArea(text, rows, cols);
		this.formatTextArea();
		this.setViewportView(textarea);
	}
	
	/**
	 * Gives properties to the textarea object. It is called by the constructors.
	 */
	protected void formatTextArea() {
		this.textarea.setLineWrap(true);
		this.textarea.setWrapStyleWord(true);
	}
	
	/**
	 * Appends the given text to the end of the document. Does nothing if the 
	 * model is null or the string is null or empty.
	 * @param str - the text to insert
	 */
	public void append(String str) {
		textarea.append(str);
	}
	
	/**
	 * Sets the text of this TextComponent to the specified text. If the text is
	 * null or empty, has the effect of simply deleting the old text. When text
	 * has been inserted, the resulting caret location is determined by the 
	 * implementation of the caret class.
	 * Note that text is not a bound property, so no PropertyChangeEvent is 
	 * fired when it changes. To listen for changes to the text, use 
	 * DocumentListener.
	 * @param str - the new text to be set
	 */
	public void setText(String str) {
		textarea.setText(str);
	}
	
	/**
	 * Sets the specified boolean to indicate whether or not this TextComponent 
	 * should be editable. A PropertyChange event ("editable") is fired when the 
	 * state is changed.
	 * @param b - the boolean to be set
	 */
	public void setEditable(boolean b) {
		textarea.setEditable(b);
	}

}
