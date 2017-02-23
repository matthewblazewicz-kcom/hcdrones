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
}