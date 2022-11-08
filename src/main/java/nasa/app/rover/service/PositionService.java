package nasa.app.rover.service;

import nasa.app.rover.model.Plateau;
import nasa.app.rover.model.Position;
import nasa.app.rover.enumeration.Direction;
import org.springframework.stereotype.Service;

@Service
public class PositionService {

    public boolean IsOnPlateau(Position position, Plateau plateau) {
        int x = position.getX();
        int y = position.getY();
        return (x >= 0 && x <= plateau.getXCordinate()) && (y >= 0 && y <= plateau.getYCordinate());
    }

    public Position moveForward(Position position, Direction direction) {
        switch (direction) {
            case EAST: return new Position(position.getX() + 1, position.getY());
            case NORTH: return new Position(position.getX(), position.getY() + 1);
            case SOUTH: return new Position(position.getX(), position.getY() - 1);
            case WEST: return new Position(position.getX() - 1, position.getY());
            default: throw new RuntimeException("Should not get here!");
        }
    }
}
