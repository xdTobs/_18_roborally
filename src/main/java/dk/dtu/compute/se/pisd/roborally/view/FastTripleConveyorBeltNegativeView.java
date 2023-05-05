package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.roborally.model.ConveyorBelt;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class FastTripleConveyorBeltNegativeView extends SpaceView {
    public FastTripleConveyorBeltNegativeView(@NotNull ConveyorBelt conveyorBelt) {
        super(conveyorBelt);
        String filePath = new File("Board_Element_Pictures/blue-arrow-triple-negative.png").toURI().toString();
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

