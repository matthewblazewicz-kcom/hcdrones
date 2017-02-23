package town.boom.pizza;


import lombok.ToString;
import town.boom.Problem;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;


@ToString
public class Pizza extends Problem {

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

    @Override
    public void process(Scanner scanner) {
        numberOfRows = scanner.nextInt();
        numberOfCols = scanner.nextInt();
        minimumIngredients = scanner.nextInt();
        maximumIngredients = scanner.nextInt();

        layout = new String[numberOfRows][numberOfCols];
        IntStream.range(0, numberOfRows).forEach(row -> {
                        scanner.nextLine();
                        IntStream.range(0, numberOfCols).forEach(col -> {
                                layout[row][col] = scanner.findInLine("[MT]");
                        });
        });

    }

    @Override
    public List solve() {
        justDoIt();
        return cycleThroughShapes();
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
        for (Shape shape : findCorrectShapes()) {

            int currentRow = 0;
            int currentCol = 0;
            List<Slice> slices = new ArrayList<>();
            int coveredArea = 0;
            int shapeSize = shape.getHeight() * shape.getWidth();
            while (currentCol <= numberOfCols - shape.getWidth()) {
                while (currentRow <= numberOfRows - shape.getHeight()) {
                    Slice slice = new Slice(currentRow, currentCol, shape.getHeight(), shape.getWidth());
                    if (doesItFit(slice)) {
                        currentRow += shape.getHeight();
                        slices.add(slice);
                        coveredArea += shapeSize;
                    } else {
                        currentRow += 1;
                    }
                }
                currentRow = 0;
                currentCol += shape.getWidth();
            }

            if (maxArea < coveredArea) {
                maxArea = coveredArea;
                bestPizzaInTown = slices;
            }
        }
        return bestPizzaInTown;
    }
}
