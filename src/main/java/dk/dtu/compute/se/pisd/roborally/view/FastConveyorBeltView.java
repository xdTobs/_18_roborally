package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.roborally.model.ConveyorBelt;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class FastConveyorBeltView extends SpaceView {
    public FastConveyorBeltView(@NotNull ConveyorBelt conveyorBelt) {
        super(conveyorBelt);
        String filename = switch (conveyorBelt.getHeading()) {
            case NORTH -> "blue-arrow-north.png";
            case EAST -> "blue-arrow-east.png";
            case SOUTH -> "blue-arrow-south.png";
            case WEST -> "blue-arrow-west.png";
        };
        String imageFile = new File("Board_Element_Pictures/" + filename).toURI().toString();
        appendStyle("-fx-background-image: url('" + imageFile + "');" +
                "-fx-background-size: 100% 100%;" +
                "-fx-background-repeat: no-repeat;" +
                "-fx-background-position: center;");
    }
}
