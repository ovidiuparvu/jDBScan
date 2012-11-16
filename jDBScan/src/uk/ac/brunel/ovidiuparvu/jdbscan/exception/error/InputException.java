package uk.ac.brunel.ovidiuparvu.jdbscan.exception.error;

/**
 * Exception which addresses all input errors
 */
@SuppressWarnings("serial")
public class InputException extends Exception {

	private final static String MESSAGE_INTRO = "Error! ";
	
	public InputException(String message) {
		super(MESSAGE_INTRO + message);
	}
	
}
