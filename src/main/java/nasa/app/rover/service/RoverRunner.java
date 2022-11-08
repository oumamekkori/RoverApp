package nasa.app.rover.service;

import nasa.app.rover.model.Plateau;
import nasa.app.rover.model.Rover;
import nasa.app.rover.enumeration.Instruction;
import nasa.app.rover.tools.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.*;

@Component
@Profile("!test")
public class RoverRunner implements CommandLineRunner {

    @Autowired
    private RoverService roverService;

    @Override
    public void run(String... args) {
        if(args.length == 0){
            throw new RuntimeException("No file passed in arguments !");
        }

        String line;
        try (InputStream fis = new FileInputStream(args[0]);
             BufferedReader br = new BufferedReader(new InputStreamReader(fis))) {

            List<String> inputs = Converter.readFileContent(br);
            String dimensions = inputs.get(0);
            Plateau plateau = Converter.createPlateauFromDimensions(dimensions);
            AtomicInteger counter = new AtomicInteger(1);
            Map<String,String> roversDataMap = Converter.splitRoversDataToMap(inputs);

            roversDataMap.forEach((drop,instr) -> {
                System.out.println("****************************");
                System.out.println("Initial position : " + drop);
                System.out.println("Commands : " + instr);
                String name = "Rover " + counter.get();
                Rover rover = roverService.dropRover(name, plateau, drop);
                out.println("Initial report : " + roverService.reportStatus(rover));
                List<Instruction> instructionsCollection = Converter.getInstructionsFromString(instr);
                roverService.processInstructions(rover, instructionsCollection);
                out.println("Final report : " + roverService.reportStatus(rover));
                counter.incrementAndGet();
            });

        } catch (IndexOutOfBoundsException e){
            System.out.println("Dimensions of the plateau or positions are incorrect !");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
