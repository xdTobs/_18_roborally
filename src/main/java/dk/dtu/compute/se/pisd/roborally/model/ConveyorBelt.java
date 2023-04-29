package dk.dtu.compute.se.pisd.roborally.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ConveyorBelt extends Space {

    static final Set<String> VALID_TYPES = new HashSet<>(Arrays.asList("GREEN", "BLUE"));
    String type;
    char direction;

    public ConveyorBelt(Board board, int x, int y) {
        super(board, x, y);

    }

    public void setType(String type) {
        if (VALID_TYPES.contains(type)) {
            this.type = type;
        } else {
            throw new IllegalArgumentException("Type must be either 'BLUE' or 'GREEN'.");
        }
    }

    public String getType() {
        return type;
    }

    public void setDirection(char direction) {
        this.direction = direction;
    }

    public char getDirection() {
        return direction;
    }


}
