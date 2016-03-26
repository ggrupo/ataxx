package es.ucm.fdi.tp.views.swing;

public class MessagesBox extends ScrollableTextarea {

	private static final long serialVersionUID = 768586332299945974L;

	
	public MessagesBox() {
		super();
	}
	
	public MessagesBox(String text) {
		super(text);
	}
	
	public MessagesBox(int rows, int cols) {
		super(rows,cols);
	}
	
	public MessagesBox(String text, int rows, int cols) {
		super(text,rows,cols);
	}
	
	
	@Override
	protected void formatTextArea() {
		super.formatTextArea();
		this.textarea.setEditable(false);
		this.textarea.setHighlighter(null);
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
		textarea.append("* " + str + "\n");
	}
}
