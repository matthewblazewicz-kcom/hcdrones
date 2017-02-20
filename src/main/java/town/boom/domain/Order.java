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
public class Order {

    private final int index;
    private final Location location;

    private Map<Integer, Integer> items = new HashMap<>();

    public static Order create(Scanner scanner, int index) {
        Order order = new Order(index, Location.of(scanner.nextInt(), scanner.nextInt()));
        scanner.nextLine();
        int numberOfOrderItems = scanner.nextInt();
        scanner.nextLine();
        IntStream.range(0, numberOfOrderItems).forEach(productId -> {
            int productTypeIndex = scanner.nextInt();
            order.items.compute(productTypeIndex, (key, value) -> value == null ? 1 : value++);
        });
        return order;
    }
}
