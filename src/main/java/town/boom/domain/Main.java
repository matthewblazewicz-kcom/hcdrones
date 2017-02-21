package town.boom.domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        State state;
        try (Scanner scanner = new Scanner (new File("C:\\Users\\gstyevko\\projects\\hcdrones\\src\\main\\resources\\1.in"))) {
            state = State.create(scanner);
        }
        catch (FileNotFoundException fnfe) {
            throw new RuntimeException(fnfe);
        }

//        state.getOrders().forEach(
//
//        );
//
    }
}
