package town.boom.drones;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
@Getter
public class Drone {

    private int id;
    private int row;
    private int col;
}
