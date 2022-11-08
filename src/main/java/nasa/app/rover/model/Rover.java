package nasa.app.rover.model;

import nasa.app.rover.enumeration.Direction;

public class Rover {

	private String name;
	private Plateau plateau;
	private Position position;	
	private Direction direction;
	
	public Rover(String name) {
		this.name = name;
	}

	public Plateau getPlateau() {
		return plateau;
	}

	public Position getPosition() {
		return position;
	}

	public Direction getDirection() {
		return direction;
	}

	public String getName() {
		return name;
	}

	public void setPlateau(Plateau plateau) {
		this.plateau = plateau;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public void setName(String name) {
		this.name = name;
	}
}
