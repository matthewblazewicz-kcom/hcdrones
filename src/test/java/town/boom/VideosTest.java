package town.boom;

import org.junit.Before;
import org.junit.Test;
import town.boom.videos.StreamingService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.assertTrue;

public class VideosTest {

    private File output;

    @Before
    public void setupOutput() throws IOException {
        output = Files.createTempFile("output-", ".out").toFile();
    }

    @Test
    public void kittens() {
        App.withProblem(new StreamingService()).from(new File("src/main/resources/kittens.in"))
                .solve()
                .writeOutput(new File("src/main/resources/kittens.out"));

        assertTrue(output.exists());
        assertTrue(output.isFile());
    }

    @Test
    public void spreading() {
        App.withProblem(new StreamingService()).from(new File("src/main/resources/videos_worth_spreading.in"))
                .solve()
                .writeOutput(new File("src/main/resources/videos_worth_spreading.out"));

        assertTrue(output.exists());
        assertTrue(output.isFile());
    }

    @Test
    public void zoo() {
        App.withProblem(new StreamingService()).from(new File("src/main/resources/me_at_the_zoo.in"))
                .solve()
                .writeOutput(new File("src/main/resources/me_at_the_zoo.out"));

        assertTrue(output.exists());
        assertTrue(output.isFile());
    }

    @Test
    public void trending() {
        App.withProblem(new StreamingService()).from(new File("src/main/resources/trending_today.in"))
                .solve()
                .writeOutput(new File("src/main/resources/trending_today.out"));

        assertTrue(output.exists());
        assertTrue(output.isFile());
    }
}
