package dk.dtu.eighteen.view;

import dk.dtu.eighteen.roborally.controller.spaces.ConveyorBelt;
import dk.dtu.eighteen.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class ConveyorBeltView extends SpaceView {
    public ConveyorBeltView(@NotNull ConveyorBelt conveyorBelt, Space space) {
        super(space);
        int rotation = switch (conveyorBelt.getHeading()) {
            case EAST -> 90;
            case SOUTH -> 180;
            case WEST -> 270;
            case NORTH -> 0;
        };
        appendStyle(
                "-fx-background-image: url(imgs/Green_Arrow.png);" +
                        "-fx-background-size: 100% 100%;" +
                        "-fx-background-repeat: no-repeat;" +
                        "-fx-background-position: center;" +
                        "-fx-rotate: " + rotation + ";");
    }
}
