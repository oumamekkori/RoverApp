package nasa.app.rover.exception;

public class IncorrectDimensionException extends RuntimeException {

	public IncorrectDimensionException(String dimension) {
		super("Incorrect Dimensions '" + dimension + "'!");
	}
}
