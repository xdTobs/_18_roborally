package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.roborally.model.ConveyorBelt;
import dk.dtu.compute.se.pisd.roborally.model.FastConveyorBelt;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.NotNull;

import java.io.File;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.*;

public class FastConveyorBeltView extends SpaceView {
    public FastConveyorBeltView(@NotNull ConveyorBelt conveyorBelt) {
        super(conveyorBelt);

        String filePath = new File("Board_Element_Pictures/Blue_Arrow.PNG").getAbsolutePath();
        ImageView blueArrow = new ImageView(new Image(filePath));
        blueArrow.setFitHeight(SPACE_HEIGHT);
        blueArrow.setFitWidth(SPACE_WIDTH - 20);
        if (conveyorBelt.getHeading() == EAST) {
            blueArrow.setRotate(90);
        } else if (conveyorBelt.getHeading() == SOUTH) {
            blueArrow.setRotate(180);
        } else if (conveyorBelt.getHeading() == WEST) {
            blueArrow.setRotate(270);
        }
        this.getChildren().add(blueArrow);
    }
}
