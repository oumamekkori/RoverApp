package nasa.app.rover.exception;

public class IncorrectPositionException extends RuntimeException {

	public IncorrectPositionException(String position) {
		super("Incorrect Position '" + position + "'!");
	}
}
