package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import org.jetbrains.annotations.NotNull;

public class ConveyorBeltView extends SpaceView {
    public ConveyorBeltView(@NotNull Space space) {
        super(space);


        ImageView greenArrow = new ImageView(new Image("C:\\Users\\rolle\\IdeaProjects\\_18_roborally\\Board_Element_Pictures\\Green_Arrow.PNG"));
        greenArrow.setFitHeight(SPACE_HEIGHT);
        greenArrow.setFitWidth(SPACE_WIDTH - 20);
        this.getChildren().add(greenArrow);
    }
}
