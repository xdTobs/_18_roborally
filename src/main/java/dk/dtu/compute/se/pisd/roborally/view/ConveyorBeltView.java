package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.roborally.model.ConveyorBelt;
import dk.dtu.compute.se.pisd.roborally.model.FastConveyorBelt;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import org.jetbrains.annotations.NotNull;

import java.io.File;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.*;

public class ConveyorBeltView extends SpaceView {
    public ConveyorBeltView(@NotNull ConveyorBelt conveyorBelt) {
        super(conveyorBelt);
        String filePath = new File("Board_Element_Pictures/Green_Arrow.png").toURI().toString();

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
