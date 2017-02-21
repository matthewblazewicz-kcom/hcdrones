//package town.boom.drones;
//
//import lombok.ToString;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.Scanner;
//
//@ToString
//public class State {
//
//    private int numberOfCols;
//    private int numberOfRows;
//    private int numberOfDrones;
//    private Drone[] drones;
//    private int maxTurns;
//    private int droneCapacity;
//
//    private int numberOfProductTypes;
//    private int[] productWeights;
//
//    private int numberOfWarehouses;
//    private int[] whCols;
//    private int[] whRows;
//    private int[][] whProducts;
//
//    private int numberOfOrders;
//    private int[] orderRows;
//    private int[] orderCols;
//    private int[][] orderProducts;
//
//    private int[][] orderDeliveries;
//    private int[][] whProductsTaken;
//
//    private List<Command> commands = new ArrayList<>();
//
//
//    private void readInput(String filename) throws FileNotFoundException {
//        Scanner scanner = new Scanner(new File(filename));
//        scanner.useDelimiter(" |\n");
//        numberOfRows = scanner.nextInt();
//        numberOfCols = scanner.nextInt();
//        numberOfDrones = scanner.nextInt();
//        drones = new Drone[numberOfDrones];
//        for (int droneId = 0; droneId < numberOfDrones; droneId++) {
//            drones[droneId] = Drone.builder().id(droneId).row(0).col(0).build();
//        }
//        maxTurns = scanner.nextInt();
//        droneCapacity = scanner.nextInt();
//        scanner.nextLine();
//
//        // number of product types
//        numberOfProductTypes = scanner.nextInt();
//        scanner.nextLine();
//        productWeights = new int[numberOfProductTypes];
//        for (int i = 0; i < numberOfProductTypes; i++) {
//            productWeights[i] = scanner.nextInt();
//        }
//        scanner.nextLine();
//        numberOfWarehouses = scanner.nextInt();
//
//        scanner.nextLine();
//        whRows = new int[numberOfWarehouses];
//        whCols = new int[numberOfWarehouses];
//        whProducts = new int[numberOfWarehouses][numberOfProductTypes];
//        whProductsTaken = new int[numberOfWarehouses][numberOfProductTypes];
//        for (int i = 0; i < numberOfWarehouses; i++) {
//            whRows[i] = scanner.nextInt();
//            whCols[i] = scanner.nextInt();
//            scanner.nextLine();
//            for (int j = 0; j < numberOfProductTypes; j++) {
//                whProducts[i][j] = scanner.nextInt();
//            }
//            scanner.nextLine();
//        }
//
//        // customer orders
//        numberOfOrders = scanner.nextInt();
//
//        orderRows = new int[numberOfOrders];
//        orderCols = new int[numberOfOrders];
//        orderProducts = new int[numberOfOrders][numberOfProductTypes];
//        orderDeliveries = new int[numberOfOrders][numberOfProductTypes];
//        for (int i = 0; i < numberOfOrders; i++) {
//            scanner.nextLine();
//            orderRows[i] = scanner.nextInt();
//            orderCols[i] = scanner.nextInt();
//            scanner.nextLine();
//            int numberOfOrderItems = scanner.nextInt();
//            scanner.nextLine();
//            for (int j = 0; j < numberOfOrderItems; j++) {
//                int productTypeIndex = scanner.nextInt();
//                orderProducts[i][productTypeIndex]++;
//            }
//        }
//    }
//
//
//    private void solveIt() {
//        int currentDrone = 0;
//        for (int orderNumber = 0; orderNumber < numberOfOrders; orderNumber++) {
//            for (int productTypeNumber = 0; productTypeNumber < numberOfProductTypes; productTypeNumber++) {
//                while (orderProducts[orderNumber][productTypeNumber] - orderDeliveries[orderNumber][productTypeNumber] > 0) {
//                    // find the product
//                    int warehouse = 0;
//                    for (; warehouse < numberOfWarehouses; warehouse++) {
//                        if (whProducts[warehouse][productTypeNumber] - whProductsTaken[warehouse][productTypeNumber] > 0) {
//                            break; // we are guaranteed to find the product somewhere
//                        }
//                    }
//
//                    // send currentDrone to pick up the product
//                    Location warehouseLocation = Location.builder().id(warehouse).row(whRows[warehouse]).col(whCols[warehouse]).build();
//                    Command pickupCommand = Command.builder()
//                            .drone(drones[currentDrone])
//                            .location(warehouseLocation)
//                            .productId(productTypeNumber)
//                            .command("L")
//                            .numberOfProducts(1)
//                            .build();
//                    whProductsTaken[warehouse][productTypeNumber]++;
//                    commands.add(pickupCommand);
//
//                    // send the current drone to deliver the product
//                    Location customerLocation = Location.builder().id(orderNumber).row(orderRows[orderNumber]).col(orderCols[orderNumber]).build();
//                    Command deliverCommand = Command.builder()
//                            .drone(drones[currentDrone])
//                            .location(customerLocation)
//                            .productId(productTypeNumber)
//                            .command("D")
//                            .numberOfProducts(1)
//                            .build();
//                    orderDeliveries[orderNumber][productTypeNumber]++;
//                    commands.add(deliverCommand);
//
//                    currentDrone++;
//                    if (currentDrone >= numberOfDrones) {
//                        currentDrone = 0;
//                    }
//                }
//            }
//        }
//    }
//
//
//    public static void main(String... args) throws FileNotFoundException {
//        State s = new State();
////        s.readInput("C:\\dev\\workspace\\rars\\drones\\src\\main\\resources\\busy_day.in");
//        s.readInput("C:\\dev\\workspace\\rars\\drones\\src\\main\\resources\\1.in");
////        System.out.println(s);
////        System.out.println();
////        System.out.println();
////        System.out.println(String.join("\n", "" + s.whRows[2], "" + s.whCols[2], Arrays.toString(s.whProducts[2])));
////        System.out.println();
////        System.out.println();
////        System.out.println(String.join("\n", "" + s.orderRows[2], "" + s.orderCols[2], Arrays.toString(s.orderProducts[2])));
//        for (int i = 0; i < s.whProducts.length; i++) {
//            int[] whProduct = s.whProducts[i];
//            System.out.println("WH " + i + ": " + Arrays.toString(whProduct));
//        }
//        s.solveIt();
//        for (Command command : s.commands) {
//            System.out.println(command);
//        }
//        System.out.println("Commands issued: " + s.commands.size());
//        for (int i = 0; i < s.whProductsTaken.length; i++) {
//            int[] whProduct = s.whProductsTaken[i];
//            System.out.println("WH " + i + ": " + Arrays.toString(whProduct));
//        }
//        for (int i = 0; i < s.orderDeliveries.length; i++) {
//            int[] whProduct = s.orderDeliveries[i];
//            System.out.println("(OD " + i + ": " + Arrays.toString(whProduct));
//        }
//    }
//
//}
