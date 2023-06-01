package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.spaces.ConveyorBelt;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class ConveyorBeltView extends SpaceView {
    public ConveyorBeltView(@NotNull Space space) {
        super(space);
        ConveyorBelt conveyorBelt = (ConveyorBelt) space.getActions().get(0);
        String filePath = new File("Board_Element_Pictures/Green_Arrow.png").toURI().toString();
        int rotation = switch (conveyorBelt.getHeading()) {
            case EAST -> 90;
            case SOUTH -> 180;
            case WEST -> 270;
            case NORTH -> 0;
        };

        appendStyle(
                "-fx-background-image: url('" + filePath + "');" +
                        "-fx-background-size: 100% 100%;" +
                        "-fx-background-repeat: no-repeat;" +
                        "-fx-background-position: center;" +
                        "-fx-rotate: " + rotation + ";");
    }
}
