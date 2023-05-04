package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.roborally.controller.spaces.ConveyorBelt;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

import java.io.File;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.*;

public class FastConveyorBeltView extends SpaceView {
    public FastConveyorBeltView(@NotNull Space space) {
        super(space);
        ConveyorBelt conveyorBelt = (ConveyorBelt) space.getActions().get(0);
        String filePath = new File("Board_Element_Pictures/Blue_Arrow.png").toURI().toString();

        if (conveyorBelt.getHeading() == EAST) {
            appendStyle("-fx-background-image: url('" + filePath + "');" +
                    "-fx-background-size: 100% 100%;" +
                    "-fx-background-repeat: no-repeat;" +
                    "-fx-background-position: center;" +
                    "-fx-rotate: 90;");
        } else if (conveyorBelt.getHeading() == SOUTH) {
            appendStyle("-fx-background-image: url('" + filePath + "');" +
                    "-fx-background-size: 100% 100%;" +
                    "-fx-background-repeat: no-repeat;" +
                    "-fx-background-position: center;" +
                    "-fx-rotate: 180;");
        } else if (conveyorBelt.getHeading() == WEST) {
            appendStyle("-fx-background-image: url('" + filePath + "');" +
                    "-fx-background-size: 100% 100%;" +
                    "-fx-background-repeat: no-repeat;" +
                    "-fx-background-position: center;" +
                    "-fx-rotate: 270;");
        } else if (conveyorBelt.getHeading() == NORTH) {
            appendStyle("-fx-background-image: url('" + filePath + "');" +
                    "-fx-background-size: 100% 100%;" +
                    "-fx-background-repeat: no-repeat;" +
                    "-fx-background-position: center;");
        }


    }
}
