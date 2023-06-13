package dk.dtu.eighteen.view;

import dk.dtu.eighteen.roborally.controller.spaces.FastConveyorBelt;
import dk.dtu.eighteen.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

import static dk.dtu.eighteen.roborally.model.Heading.*;

/**
 * @author Jakob Hansen, s224312@dtu.dk
 */
public class FastConveyorBeltView extends SpaceView {
    public FastConveyorBeltView(@NotNull FastConveyorBelt fastconveyorBelt, Space space) {
        super(space);
        if (fastconveyorBelt.getHeading() == EAST) {
            appendStyle("-fx-background-image: url(imgs/blue-arrow-east.png);" +
                    "-fx-background-size: 100% 100%;" +
                    "-fx-background-repeat: no-repeat;" +
                    "-fx-background-position: center;");

        } else if (fastconveyorBelt.getHeading() == SOUTH) {
            appendStyle("-fx-background-image: url(imgs/blue-arrow-south.png);" +
                    "-fx-background-size: 100% 100%;" +
                    "-fx-background-repeat: no-repeat;" +
                    "-fx-background-position: center;");
        } else if (fastconveyorBelt.getHeading() == WEST) {
            appendStyle("-fx-background-image: url(imgs/blue-arrow-west.png);" +
                    "-fx-background-size: 100% 100%;" +
                    "-fx-background-repeat: no-repeat;" +
                    "-fx-background-position: center;");
        } else if (fastconveyorBelt.getHeading() == NORTH) {
            appendStyle("-fx-background-image: url(imgs/blue-arrow-north.png);" +
                    "-fx-background-size: 100% 100%;" +
                    "-fx-background-repeat: no-repeat;" +
                    "-fx-background-position: center;");
        }
    }
}

