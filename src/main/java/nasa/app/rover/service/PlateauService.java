package nasa.app.rover.service;

import nasa.app.rover.model.Plateau;
import nasa.app.rover.model.Position;
import org.springframework.stereotype.Service;

@Service
public class PlateauService {

    public boolean isOccupied(Position position, Plateau plateau) {
        return plateau.getRovers()
                .stream()
                .anyMatch(rover -> position.equals(rover.getPosition()));
    }
}
