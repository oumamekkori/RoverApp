package nasa.app.rover.exception;

public class NotDroppedException extends RuntimeException {

	public NotDroppedException() {
		super("Rover was not dropped on the plateau!");
	}
}
