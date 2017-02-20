package town.boom.domain;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Location {

    private final int row;
    private final int col;

    public int distance(Location from) {
        return (int) Math.ceil(Math.sqrt((this.row * from.row) + (this.col * from.col)));
    }

    public static Location of(int row, int col) {
        return new Location(row, col);
    }
}
