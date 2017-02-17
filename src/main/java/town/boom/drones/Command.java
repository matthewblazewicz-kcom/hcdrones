package town.boom.drones;

import lombok.Builder;

@Builder
public class Command {

    private String command;
    private int productId;
    private int numberOfProducts;

    private Drone drone;
    private Location location;

    public int calculateDistance() {
        int rowDist = location.getRow() - drone.getRow();
        int colDist = location.getCol() - drone.getCol();
        double distance = Math.sqrt(rowDist * rowDist + colDist * colDist);
        return (int) Math.ceil(distance);
    }

    public int calculateTime() {
        return calculateDistance() + 1;
    }

    public String toString() {
        return String.join(" ", "" + drone.getId(), command, "" + location.getId(), "" + productId, "" + numberOfProducts);
    }
}
