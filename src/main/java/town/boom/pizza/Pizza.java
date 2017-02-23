package town.boom.pizza;


import lombok.ToString;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;


@ToString
public class Pizza {

    private int numberOfRows;
    private int numberOfCols;
    private int minimumIngredients;
    private int maximumIngredients;
    private String[][] layout;

    private int numberOfMushrooms;
    private int numberOfTomatoes;

    int[][] tomatoesInRow;
    int[][] mushroomsInRow;
    int[][] tomatoes;
    int[][] mushrooms;

    int[][] leftovers;
    int[][] aggregatedLeftovers;
    int[][] yetAnotherAggregatedLeftovers;

    public static Pizza fromFile(Scanner scanner) {
        Pizza pizza = new Pizza();
        pizza.numberOfRows = scanner.nextInt();
        pizza.numberOfCols = scanner.nextInt();
        pizza.minimumIngredients = scanner.nextInt();
        pizza.maximumIngredients = scanner.nextInt();

        pizza.layout = new String[pizza.numberOfRows][pizza.numberOfCols];
        pizza.leftovers = new int[pizza.numberOfRows][pizza.numberOfCols];
        pizza.aggregatedLeftovers = new int[pizza.numberOfRows][pizza.numberOfCols];
        pizza.yetAnotherAggregatedLeftovers = new int[pizza.numberOfRows][pizza.numberOfCols];
        IntStream.range(0, pizza.numberOfRows).forEach(row -> {
                        scanner.nextLine();
                        IntStream.range(0, pizza.numberOfCols).forEach(col -> {
                                pizza.layout[row][col] = scanner.findInLine("[MT]");
                        });
        });

        return pizza;
    }

    public void slice() {
//        System.out.println(this);
        justDoIt();
        deliverPizza(cycleThroughShapes());
    }

    public static void main(String... args) {
        String pathname = "resources\\big.in";
//        String pathname = args[0];
        try (Scanner scanner = new Scanner (new File(pathname))) {
            Pizza pizza = fromFile(scanner);
            pizza.slice();
        }
        catch (FileNotFoundException fnfe) {
            throw new RuntimeException(fnfe);
        }
    }

    private void justDoIt() {

        tomatoesInRow = new int[numberOfRows][numberOfCols];
        mushroomsInRow = new int[numberOfRows][numberOfCols];
        tomatoes = new int[numberOfRows][numberOfCols];
        mushrooms = new int[numberOfRows][numberOfCols];

        for (int row = 0; row < numberOfRows; row++) {
            for (int col = 0; col < numberOfCols; col++) {
                if ("M".equals(layout[row][col])) {
                    numberOfMushrooms++;
                    if (col == 0) {
                        mushroomsInRow[row][col] = 1;
                        tomatoesInRow[row][col] = 0;
                    } else {
                        mushroomsInRow[row][col] = mushroomsInRow[row][col - 1] + 1;
                        tomatoesInRow[row][col] = tomatoesInRow[row][col - 1];
                    }
                } else {
                    numberOfTomatoes++;
                    if (col == 0) {
                        mushroomsInRow[row][col] = 0;
                        tomatoesInRow[row][col] = 1;
                    } else {
                        mushroomsInRow[row][col] = mushroomsInRow[row][col - 1];
                        tomatoesInRow[row][col] = tomatoesInRow[row][col - 1] + 1;
                    }
                }
            }
        }


        for (int row = 0; row < numberOfRows; row++) {
            for (int col = 0; col < numberOfCols; col++) {
                if (row == 0) {
                    mushrooms[row][col] = mushroomsInRow[row][col];
                    tomatoes[row][col] = tomatoesInRow[row][col];
                } else {
                    mushrooms[row][col] = mushrooms[row - 1][col] + mushroomsInRow[row][col];
                    tomatoes[row][col] = tomatoes[row - 1][col] + tomatoesInRow[row][col];
                }
            }
        }
//        for (int row = 0; row < numberOfRows; row++) {
//            for (int col = 0; col < numberOfCols; col++) {
//                System.out.print(layout[row][col]);
//            }
//            System.out.println();
//        }
//        System.out.println();
//        for (int row = 0; row < numberOfRows; row++) {
//            for (int col = 0; col < numberOfCols; col++) {
//                System.out.print(String.format("%4d", mushroomsInRow[row][col]));
//            }
//            System.out.println();
//        }
//        System.out.println();
//        for (int row = 0; row < numberOfRows; row++) {
//            for (int col = 0; col < numberOfCols; col++) {
//                System.out.print(String.format("%4d", tomatoesInRow[row][col]));
//            }
//            System.out.println();
//        }


//        System.out.println();
//        System.out.println();
//        for (int row = 0; row < numberOfRows; row++) {
//            for (int col = 0; col < numberOfCols; col++) {
//                System.out.print(String.format("%4d", mushrooms[row][col]));
//            }
//            System.out.println();
//        }
//
//        System.out.println();
//
//        System.out.println();
//        for (int row = 0; row < numberOfRows; row++) {
//            for (int col = 0; col < numberOfCols; col++) {
//                System.out.print(String.format("%4d", tomatoes[row][col]));
//            }
//            System.out.println();
//        }
//        System.out.println();
//        System.out.println(numberOfMushrooms);
//        System.out.println(numberOfTomatoes);
    }

    private boolean doesItFit(Slice slice) {
        int mushroomsNumber = mushrooms[slice.getSliceRow() + slice.getSliceHeight() - 1][slice.getSliceCol() + slice.getSliceWidth() - 1];
        int tomatoesNumber = tomatoes[slice.getSliceRow() + slice.getSliceHeight() - 1][slice.getSliceCol() + slice.getSliceWidth() - 1];
        if (slice.getSliceRow() > 0) {
            mushroomsNumber -= mushrooms[slice.getSliceRow() - 1][slice.getSliceCol() + slice.getSliceWidth() - 1];
            tomatoesNumber -= tomatoes[slice.getSliceRow() - 1][slice.getSliceCol() + slice.getSliceWidth() - 1];
        }
        if (slice.getSliceCol() > 0) {
            mushroomsNumber -= mushrooms[slice.getSliceRow() + slice.getSliceHeight() - 1][slice.getSliceCol() - 1];
            tomatoesNumber -= tomatoes[slice.getSliceRow() + slice.getSliceHeight() - 1][slice.getSliceCol() - 1];
        }
        if (slice.getSliceRow() > 0 && slice.getSliceCol() > 0) {
            mushroomsNumber += mushrooms[slice.getSliceRow() - 1][slice.getSliceCol() - 1];
            tomatoesNumber += tomatoes[slice.getSliceRow() - 1][slice.getSliceCol() - 1];
        }

        return (mushroomsNumber >= minimumIngredients && tomatoesNumber >= minimumIngredients);
    }

    private List<Shape> findCorrectShapes() {
        List<Shape> shapes = new ArrayList<>();
        for (int width = maximumIngredients; width > 0; width--) {
            for (int height = 1; width * height <= maximumIngredients; height++) {
                shapes.add(new Shape(height, width));
            }
        }
        return shapes;
    }

    private List<Slice> cycleThroughShapes() {

        int maxArea = 0;
        List<Slice> bestPizzaInTown = null;
        List<Shape> correctShapes = findCorrectShapes();

        int subpizzaRow = 0;
        int subpizzaCol = 0;
        int subpizzaMaxRow = numberOfRows - 1;
        int subpizzaMaxCol = numberOfCols - 1;

        for (Shape shape : correctShapes) {
            for (int r = 0; r < numberOfRows; r++) {
                for (int c = 0; c < numberOfCols; c++) {
                    leftovers[r][c] = 0;
                }
            }

            System.out.println("Shape: " + shape.getWidth() + " " + shape.getHeight());
            int currentRow = subpizzaRow;
            int currentCol = subpizzaCol;
            List<Slice> slices = new ArrayList<>();
            int coveredArea = 0;
            int shapeSize = shape.getHeight() * shape.getWidth();
//            while (currentCol <= 1 + subpizzaMaxCol - shape.getWidth()) {
            while (currentCol <= subpizzaMaxCol) {
                if (currentCol > 1 + subpizzaMaxCol - shape.getWidth()) {
                    for (int col = currentCol; col <= subpizzaMaxCol ; col ++) {
                        for (int r = currentRow; r < subpizzaMaxRow; r++) {
                            leftovers[currentRow][col] = 1;
                        }
                    }
                    break;
                }
                while (currentRow <= 1 + subpizzaMaxRow - shape.getHeight()) {

                    Slice slice = new Slice(currentRow, currentCol, shape.getHeight(), shape.getWidth());
                    if (doesItFit(slice)) {
                        currentRow += shape.getHeight();
                        slices.add(slice);
                        coveredArea += shapeSize;
                    } else {
                        for (int col = currentCol; col < currentCol + shape.getWidth(); col ++) {
                            leftovers[currentRow][col] = 1;
                        }
                        currentRow += 1;
                    }
                }

                for (int col = currentCol; col < currentCol + shape.getWidth(); col ++) {
                    for (int r = currentRow; r < subpizzaMaxRow; r++) {
                        leftovers[currentRow][col] = 1;
                    }
                }
                currentRow = 0;
                currentCol += shape.getWidth();
            }

            if (maxArea < coveredArea) {
                maxArea = coveredArea;
                bestPizzaInTown = slices;
            }

            for (int r = 0; r < numberOfRows; r++) {
                for (int c = 0; c < numberOfCols; c++) {
                    if (c == 0) {
                        aggregatedLeftovers[r][c] = leftovers[r][c];
                    } else {
                        if (leftovers[r][c] == 0) {
                            aggregatedLeftovers[r][c] = 0;
                        } else {
                            aggregatedLeftovers[r][c] = aggregatedLeftovers[r][c - 1] + 1;
                        }
                    }
                }
            }


        System.out.println();
        for (int row = 0; row < numberOfRows; row++) {
            for (int col = 0; col < numberOfCols; col++) {
                System.out.print(String.format("%4d", leftovers[row][col]));
            }
            System.out.println();
        }

            int largestArea = 0;
            int subRow = 0;
            int subCol = 0;
            int subMaxRow = 0;
            int subMaxCol = 0;

            for (int c = 0; c < numberOfCols; c++) {
                int goBack = 0;
                int currentMinimum = 0;
                for (int r = 0; r < numberOfRows; r++) {
                    if (aggregatedLeftovers[r][c] > 0) {
                        goBack++;
                        if (currentMinimum > aggregatedLeftovers[r][c]) {
                            currentMinimum = aggregatedLeftovers[r][c];
                        }
                        int currentArea = goBack * currentMinimum;
                        if (largestArea < currentArea) {
                            largestArea = currentArea;
                            subMaxRow = r;
                            subMaxCol = c;
                            subRow = r - goBack + 1;
                            subCol = c - currentMinimum + 1;
                        }
                    } else {
                        goBack = 0;
                    }
                }
            }

            System.out.println("Leftovers");
            System.out.println(subRow);
            System.out.println(subCol);
            System.out.println(subMaxRow);
            System.out.println(subMaxCol);

        }
        return bestPizzaInTown;
    }

    private void deliverPizza(List<Slice> slices) {
        System.out.println(slices.size());
        for (Slice slice : slices) {
            System.out.println(slice);
        }
    }
}
