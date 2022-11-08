package nasa.app.rover;

import nasa.app.rover.enumeration.Direction;
import nasa.app.rover.exception.NotDroppedException;
import nasa.app.rover.exception.PositionNotOnPlateauException;
import nasa.app.rover.exception.UnknownInstructionException;
import nasa.app.rover.model.Plateau;
import nasa.app.rover.model.Position;
import nasa.app.rover.model.Rover;
import nasa.app.rover.service.PlateauService;
import nasa.app.rover.service.PositionService;
import nasa.app.rover.service.RoverService;
import nasa.app.rover.tools.Converter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ActiveProfiles("test")
class RoverApplicationTests {

    @InjectMocks
    RoverService roverService;

    @Mock
    private PositionService positionService;

    @Mock
    private PlateauService plateauService;
    private Plateau plateau = new Plateau(5, 5);

    @BeforeEach
    void setMockOutput() {
        when(positionService.IsOnPlateau(any(Position.class),any(Plateau.class)))
                .thenCallRealMethod();
        when(plateauService.isOccupied(any(Position.class),any(Plateau.class)))
                .thenCallRealMethod();
        when(positionService.moveForward(any(Position.class),any(Direction.class)))
                .thenCallRealMethod();
    }

    @Test
    public void moving_rover_one_should_succeed() {
        Rover rover = new Rover("1");
        roverService.dropRover(rover, plateau, new Position(1, 2), Direction.NORTH);
        roverService.processInstructions(rover, Converter.getInstructionsFromString("LMLMLMLMM"));
        String report = roverService.reportPosition(rover);
        assertEquals("1 3 N", report);
    }


    @Test
    public void moving_rover_two_should_succeed() {
        Rover rover = new Rover("2");
        roverService.dropRover(rover, plateau, new Position(3, 3), Direction.EAST);
        roverService.processInstructions(rover,Converter.getInstructionsFromString("MMRMMRMRRM"));

        String report = roverService.reportPosition(rover);
        assertEquals("5 1 E", report);
    }

    @Test
    public void moving_rover_beyond_plateau_should_throw() {
        Rover rover = new Rover("1");
        roverService.dropRover(rover, plateau, new Position(2, 2), Direction.NORTH);
        try {
            roverService.processInstructions(rover,Converter.getInstructionsFromString("MMMMMMMM"));
        }
        catch (PositionNotOnPlateauException ex) {
            assertEquals("Position is not on the plateau!", ex.getMessage());
        }
    }

    @Test
    public void dropping_rover_beyond_plateau_should_throw() {
        Rover rover = new Rover("1");
        try {
            roverService.dropRover(rover, plateau, new Position(6, 6), Direction.NORTH);
        }
        catch (PositionNotOnPlateauException ex) {
            assertEquals("Position is not on the plateau!", ex.getMessage());
        }
    }

    @Test
    public void not_dropped_rover_should_report_properly() {
        Rover rover = new Rover("1");
        String report = roverService.reportPosition(rover);
        assertEquals("Rover is not yet dropped", report);
    }

    @Test
    public void moving_an_undropped_rover_should_throw() {
        Rover rover = new Rover("1");
        try {
            roverService.processInstructions(rover, Converter.getInstructionsFromString("MMMMMM"));
        }
        catch (NotDroppedException ex) {
            assertEquals("Rover was not dropped on the plateau!", ex.getMessage());
        }
    }

    @Test
    public void unknown_instruction_should_throw() {
        Rover rover = new Rover("1");
        try {
            roverService.processInstructions(rover, Converter.getInstructionsFromString("XXXX"));
        }
        catch (UnknownInstructionException ex) {
            assertEquals("Unknown instruction 'X'!", ex.getMessage());
        }
    }

    @Test
    public void moving_rover_over_another_rover_should_throw() {
        roverService.dropRover("one",plateau, "3 5 E");

        try {
            roverService.dropRover("two", plateau, "3 5 N");
            assertTrue(false, "Should have thrown before!");
        }
        catch (RuntimeException ex) {
            assertEquals("Plateau is already occupied by a rover!", ex.getMessage());
        }
    }

    @Test
    public void dropping_at_10_10_should_give_exact_report() {
        Plateau plat = new Plateau(20, 20);
        Rover one = roverService.dropRover("one", plat, "10 10 N");
        String report = roverService.reportPosition(one);
        assertEquals("10 10 N", report);
    }

}
