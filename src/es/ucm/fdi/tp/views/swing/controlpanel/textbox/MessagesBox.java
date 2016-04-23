package es.ucm.fdi.tp.views.swing.controlpanel.textbox;

import java.awt.Font;

import javax.swing.border.TitledBorder;

/**
 * A messages box where messages can be added one by one, like a list.
 *
 */
public class MessagesBox extends ScrollableTextarea {

	private static final long serialVersionUID = 768586332299945974L;

	final protected static String ENDLINE = System.getProperty("line.separator");
	final protected static String BULLET = " \u2022 ";
	
	/**
	 * Creates an empty messages box. 
	 * The initial string is null, and rows/columns are set to 0.
	 */
	public MessagesBox() {
		super();
	}
	
	/**
	 * Creates a new messages box. Rows/columns are set to 0.
	 * @param text - initial message
	 */
	public MessagesBox(String text) {
		super(text);
	}
	
	/**
	 * Creates an empty messages box. 
	 * @param rows - number of preferred rows
	 * @param cols - number of preferred cols or letters
	 */
	public MessagesBox(int rows, int cols) {
		super(rows,cols);
	}
	
	/**
	 * Creates a new messages box.
	 * @param text - initial text.
	 * @param rows - number of preferred rows
	 * @param cols - number of preferred cols or letters
	 */
	public MessagesBox(String text, int rows, int cols) {
		super(text,rows,cols);
	}
	
	
	@Override
	protected void formatTextArea() {
		super.formatTextArea();
		this.textarea.setEditable(false);
		this.textarea.setHighlighter(null);
		this.setBorder(new TitledBorder("Status Messages"));
		this.textarea.setFont(new Font("SansSerif", Font.PLAIN, 11));
	}
	
	/**
	 * @throws UnsupportedOperationException when editable is set to true.
	 */
	@Override
	public void setEditable(boolean b) {
		if(b) throw new UnsupportedOperationException(
				"This textarea object is read-only to users. ");
		else this.textarea.setEditable(false);
	}
	
	/**
	 * Adds a new message to the box list. Does nothing if the model is null or 
	 * the string is null or empty.
	 */
	@Override
	public void append(String str) {
		try {
			textarea.append(BULLET + str + ENDLINE);
		} catch(NullPointerException e) {
			textarea.setText(null);
		}
	}
	
	@Override
	public void setText(String str) {
		try {
			textarea.setText(BULLET + str + ENDLINE);
		} catch(NullPointerException e) {
			textarea.setText(null);
		}
	}
}
