package town.boom.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.IntStream;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class Warehouse {

    private final int id;
    private final Location location;

    private Map<Integer, Integer> inventory = new HashMap<>();

    public static Warehouse create(Scanner scanner, int id, int numberOfProductTypes) {
        Warehouse warehouse = new Warehouse(id, Location.of(scanner.nextInt(), scanner.nextInt()));
        scanner.nextLine();
        IntStream.range(0, numberOfProductTypes).forEach(index -> {
                    int stock = scanner.nextInt();
                    if (stock > 0) {
                        warehouse.inventory.put(index, stock);
                    }
        });
        return warehouse;
    }
}
