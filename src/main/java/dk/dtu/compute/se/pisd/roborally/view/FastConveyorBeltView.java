package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.roborally.model.ConveyorBelt;
import org.jetbrains.annotations.NotNull;

import java.io.File;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.*;

public class FastConveyorBeltView extends SpaceView {
    public FastConveyorBeltView(@NotNull ConveyorBelt conveyorBelt) {
        super(conveyorBelt);
        //String filePath = new File("Board_Element_Pictures/Blue_Arrow.png").toURI().toString();

        if (conveyorBelt.getHeading() == EAST) {
            String filePath = new File("Board_Element_Pictures/blue-arrow-east.png").toURI().toString();
            appendStyle("-fx-background-image: url('" + filePath + "');" +
                    "-fx-background-size: 100% 100%;" +
                    "-fx-background-repeat: no-repeat;" +
                    "-fx-background-position: center;");
        } else if (conveyorBelt.getHeading() == SOUTH) {
            String filePath = new File("Board_Element_Pictures/blue-arrow-south.png").toURI().toString();
            appendStyle("-fx-background-image: url('" + filePath + "');" +
                    "-fx-background-size: 100% 100%;" +
                    "-fx-background-repeat: no-repeat;" +
                    "-fx-background-position: center;");
        } else if (conveyorBelt.getHeading() == WEST) {
            String filePath = new File("Board_Element_Pictures/blue-arrow-west.png").toURI().toString();
            appendStyle("-fx-background-image: url('" + filePath + "');" +
                    "-fx-background-size: 100% 100%;" +
                    "-fx-background-repeat: no-repeat;" +
                    "-fx-background-position: center;");
        } else if (conveyorBelt.getHeading() == NORTH) {
            String filePath = new File("Board_Element_Pictures/blue-arrow-north.png").toURI().toString();
            appendStyle("-fx-background-image: url('" + filePath + "');" +
                    "-fx-background-size: 100% 100%;" +
                    "-fx-background-repeat: no-repeat;" +
                    "-fx-background-position: center;");
        }


    }
}
