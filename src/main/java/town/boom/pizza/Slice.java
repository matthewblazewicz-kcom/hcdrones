package town.boom.pizza;

public class Slice {
    private final int sliceRow;
    private final int sliceCol;
    private final int sliceHeight;
    private final int sliceWidth;

    public Slice(int sliceRow, int sliceCol, int sliceHeight, int sliceWidth) {
        this.sliceRow = sliceRow;
        this.sliceCol = sliceCol;
        this.sliceHeight = sliceHeight;
        this.sliceWidth = sliceWidth;
    }

    public int getSliceRow() {
        return sliceRow;
    }

    public int getSliceCol() {
        return sliceCol;
    }

    public int getSliceHeight() {
        return sliceHeight;
    }

    public int getSliceWidth() {
        return sliceWidth;
    }

    public String toString() {
        return String.join(" ",
                "" + sliceRow,
                "" + sliceCol,
                "" + (sliceRow + sliceHeight - 1),
                "" + (sliceCol + sliceWidth - 1));
    }
}
