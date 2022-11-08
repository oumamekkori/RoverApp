package nasa.app.rover.model;

import java.util.*;

public class Plateau {

	private int xCordinate;

	private int yCordinate;
	
	private List<Rover> rovers = new ArrayList<Rover>();
	
	public Plateau(int xCordinate, int yCordinate) {
		this.xCordinate = xCordinate;
		this.yCordinate = yCordinate;
	}

	public int getXCordinate() {
		return xCordinate;
	}

	public void setXCordinate(int xCordinate) {
		this.xCordinate = xCordinate;
	}

	public int getYCordinate() {
		return yCordinate;
	}

	public void setYCordinate(int yCordinate) {
		this.yCordinate = yCordinate;
	}

	public List<Rover> getRovers() {
		return rovers;
	}

	public void setRovers(List<Rover> rovers) {
		this.rovers = rovers;
	}
}
