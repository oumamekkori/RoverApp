package nasa.app.rover.tools;

import nasa.app.rover.enumeration.Direction;
import nasa.app.rover.enumeration.Instruction;
import nasa.app.rover.exception.IncorrectDimensionException;
import nasa.app.rover.exception.UnknownInstructionException;
import nasa.app.rover.model.Plateau;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Converter {

    public static Direction convertToDirectionEnum(char heading) {
        switch (heading) {
            case 'N': return Direction.NORTH;
            case 'W': return Direction.WEST;
            case 'S': return Direction.SOUTH;
            case 'E': return Direction.EAST;
            default: throw new RuntimeException("Unsupported character '" + heading + "'!");
        }
    }

    public static char convertFromDirectionEnum(Direction heading) {
        switch (heading) {
            case NORTH: return 'N';
            case WEST: return 'W';
            case SOUTH: return 'S';
            case EAST: return 'E';
            default: throw new RuntimeException("Unsupported heading '" + heading + "'!");
        }
    }

    public static List<String> readFileContent(BufferedReader br) throws IOException {
        List<String> inputs = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            if (!line.trim().isEmpty()) {
                inputs.add(line);
            }
        }
        if (inputs.isEmpty()) {
            throw new RuntimeException("File is Empty !");
        }

        return inputs;
    }

    public static Map<String, String> splitRoversDataToMap(List<String> inputs){
        Map<String,String> instructionsMap = new HashMap<>();
        AtomicInteger counter = new AtomicInteger(1);
        try{
            inputs.forEach(input -> {
                int i = counter.get();
                if(i % 2 != 0 && i < inputs.size()){
                    instructionsMap.put(inputs.get(i),inputs.get(i + 1));
                }
                counter.incrementAndGet();
            });
        } catch(IndexOutOfBoundsException e){
            System.out.println("Rover "+ (instructionsMap.size() + 1) + " does not have "
                    + "correct initial position or commands");
        }

        return instructionsMap;
    }

    public static Plateau createPlateauFromDimensions(String dimensions) {
        String[] parts = dimensions.split(" ");
        if(parts == null || parts.length != 2){
            throw new IncorrectDimensionException(dimensions);
        }
        int dimX = Integer.parseInt(parts[0]);
        int dimY = Integer.parseInt(parts[1]);
        return new Plateau(dimX, dimY);
    }

    public static List<Instruction> getInstructionsFromString(String instructions) {
        List<Instruction> result = new ArrayList<Instruction>();
        instructions.chars().mapToObj(c -> (char) c).forEach(c -> {
            switch (c) {
                case 'L': result.add(Instruction.LEFT); break;
                case 'M': result.add(Instruction.MOVE); break;
                case 'R': result.add(Instruction.RIGHT); break;
                default: throw new UnknownInstructionException(c);
            }
        });

        return result;
    }

}
