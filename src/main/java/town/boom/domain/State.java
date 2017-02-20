package town.boom.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.IntStream;

import static lombok.AccessLevel.PRIVATE;

@Getter
@NoArgsConstructor(access = PRIVATE)
public class State {

    private int numberOfRows;
    private int numberOfCols;
    private int numberOfDrones;
    private int maxTurns;
    private int droneCapacity;
    private int numberOfProductTypes;
    private Map<Integer, Integer> productTypes = new HashMap<>();
    private int numberOfWarehouses;
    private Map<Integer, Warehouse> warehouses = new HashMap<>();
    private int numberOfOrders;
    private Map<Integer, Order> orders = new HashMap<>();

    public static State create(Scanner scanner) {
        State state = new State();
        // first line
        state.numberOfRows = scanner.nextInt();
        state.numberOfCols = scanner.nextInt();
        state.numberOfDrones = scanner.nextInt();
        state.maxTurns = scanner.nextInt();
        state.droneCapacity = scanner.nextInt();

        // product types
        scanner.nextLine();
        state.numberOfProductTypes = scanner.nextInt();
        scanner.nextLine();
        IntStream.range(0, state.numberOfProductTypes).forEach(index ->
                state.productTypes.put(index, scanner.nextInt()));
        scanner.nextLine();

        // warehouses
        state.numberOfWarehouses = scanner.nextInt();
        IntStream.range(0, state.numberOfWarehouses).forEach(index -> {
            scanner.nextLine();
            state.warehouses.put(index, Warehouse.create(scanner, index, state.numberOfProductTypes));
        });

        // customer orders
        scanner.nextLine();
        state.numberOfOrders = scanner.nextInt();
        IntStream.range(0, state.numberOfOrders).forEach(index -> {
            scanner.nextLine();
            state.orders.put(index, Order.create(scanner, index));
        });
        return state;
    }
}
