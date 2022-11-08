package nasa.app.rover.service;

import nasa.app.rover.exception.IncorrectPositionException;
import nasa.app.rover.model.Plateau;
import nasa.app.rover.model.Position;
import nasa.app.rover.model.Rover;
import nasa.app.rover.enumeration.Direction;
import nasa.app.rover.enumeration.Instruction;
import nasa.app.rover.exception.NotDroppedException;
import nasa.app.rover.exception.PositionNotOnPlateauException;
import nasa.app.rover.tools.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoverService {

    @Autowired
    private PlateauService plateauService;

    @Autowired
    private PositionService positionService;

    public Rover dropRover(String name, Plateau plateau, String args){
        Rover rover = new Rover(name);
        String[] parts = args.split(" ");
        if(parts == null || parts.length != 3){
            throw new IncorrectPositionException(args);
        }
        int x = Integer.parseInt(parts[0]);
        int y = Integer.parseInt(parts[1]);
        Direction direction = Converter.convertToDirectionEnum(parts[2].toCharArray()[0]);
        dropRover(rover, plateau, new Position(x, y), direction);
        return rover;
    }

    public void dropRover(Rover rover, Plateau plateau, Position position, Direction heading) {

        if (!positionService.IsOnPlateau(position, plateau)) {
            throw new PositionNotOnPlateauException();
        }

        if (plateauService.isOccupied(position, plateau)) {
            throw new RuntimeException("Plateau is already occupied by a rover!");
        }

        plateau.getRovers().add(rover);
        rover.setPlateau(plateau);
        rover.setPosition(position);
        rover.setDirection(heading);


    }

    public String reportStatus(Rover rover) {
        StringBuilder sb = new StringBuilder(rover.getName());
        sb.append(": ");
        sb.append(reportPosition(rover));
        return sb.toString();
    }

    public String reportPosition(Rover rover) {
        if (rover.getPosition() == null || rover.getDirection() == null) {
            return "Rover is not yet dropped";
        }
        return rover.getPosition().toString() + " " + Converter.convertFromDirectionEnum(rover.getDirection());
    }

    public void processInstructions(Rover rover, List<Instruction> instructions) {
        instructions.forEach(instr -> processInstruction(rover, instr));
    }

    private void processInstruction(Rover rover, Instruction instruction) {
        if (rover.getPlateau() == null || rover.getDirection() == null) {
            throw new NotDroppedException();
        }

        switch (instruction) {
            case LEFT: rover.setDirection(turnLeft(rover.getDirection())); break;
            case MOVE: moveForward(rover); break;
            case RIGHT: rover.setDirection(turnRight(rover.getDirection())); break;
            default: throw new RuntimeException("Should not get here!");
        }
    }

    private Direction turnLeft(Direction direction) {
        switch (direction) {
            case EAST: direction = Direction.NORTH; break;
            case NORTH: direction = Direction.WEST; break;
            case SOUTH: direction = Direction.EAST; break;
            case WEST: direction = Direction.SOUTH; break;
            default: throw new RuntimeException("Should not get here!");
        }
        return direction;
    }

    private Direction turnRight(Direction direction) {
        switch (direction) {
            case EAST: direction = Direction.SOUTH; break;
            case NORTH: direction = Direction.EAST; break;
            case SOUTH: direction = Direction.WEST; break;
            case WEST: direction = Direction.NORTH; break;
            default: throw new RuntimeException("Should not get here!");
        }
        return direction;
    }

    private void moveForward(Rover rover) {
        Position newPosition = positionService.moveForward(rover.getPosition(), rover.getDirection());
        if (!positionService.IsOnPlateau(newPosition,rover.getPlateau())) {
            throw new PositionNotOnPlateauException();
        }
        rover.setPosition(newPosition);
    }

}
