package nasa.app.rover.exception;

public class PositionNotOnPlateauException extends RuntimeException {
	
	public PositionNotOnPlateauException() {
		super("Position is not on the plateau!");
	}
}
