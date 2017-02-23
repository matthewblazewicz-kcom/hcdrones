package town.boom;

import org.junit.Before;
import org.junit.Test;
import town.boom.pizza.Pizza;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.assertTrue;

public class AppTest {

    private File output;

    @Before
    public void setupOutput() throws IOException {
        output = Files.createTempFile("output-", ".out").toFile();
    }

    @Test
    public void small() {
        App.withProblem(new Pizza()).from(new File("src/main/resources/small.in"))
                .solve()
                .writeOutput(new File("target/small.out"));

        assertTrue(output.exists());
        assertTrue(output.isFile());
    }

    @Test
    public void medium() {
        App.withProblem(new Pizza()).from(new File("src/main/resources/medium.in"))
                .solve()
                .writeOutput(new File("target/medium.out"));

        assertTrue(output.exists());
        assertTrue(output.isFile());
    }

//    @Test
//    public void big() {
//        App.withProblem(new Pizza()).from(new File("src/main/resources/big.in"))
//                .solve()
//                .writeOutput(new File("target/big.out"));
//
//        assertTrue(output.exists());
//        assertTrue(output.isFile());
//    }
}
