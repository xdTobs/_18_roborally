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

        String filePath = new File("Board_Element_Pictures/Green_Arrow.PNG").getAbsolutePath();
        ImageView greenArrow = new ImageView(new Image(filePath));
        greenArrow.setFitHeight(SPACE_HEIGHT);
        greenArrow.setFitWidth(SPACE_WIDTH - 20);
        if (conveyorBelt.getHeading() == EAST) {
            greenArrow.setRotate(90);
        } else if (conveyorBelt.getHeading() == SOUTH) {
            greenArrow.setRotate(180);
        } else if (conveyorBelt.getHeading() == WEST) {
            greenArrow.setRotate(270);
        }
        this.getChildren().add(greenArrow);
    }
}
