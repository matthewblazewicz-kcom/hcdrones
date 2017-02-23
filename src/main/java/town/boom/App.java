package town.boom;

import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

@RequiredArgsConstructor
public class App {

    private final Problem problem;

    private List solution;

    public static App withProblem(Problem problem) {
        return new App(problem);
    }

    public App from(File input) {
        try (Scanner scanner = new Scanner(input)) {
            problem.process(scanner);
            return this;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public App solve() {
        solution = problem.solve();
        return this;
    }

    public void writeOutput(File output) {
        try (FileWriter outputWriter = new FileWriter(output)){
            outputWriter.write(Integer.toString(solution.size()));
            for (Object o : solution) {
                outputWriter.write("\n" + o.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
