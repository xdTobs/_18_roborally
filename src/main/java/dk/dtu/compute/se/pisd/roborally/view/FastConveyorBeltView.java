package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.NotNull;

public class FastConveyorBeltView extends SpaceView{
    public FastConveyorBeltView(@NotNull Space space) {
        super(space);
        ImageView blueArrow = new ImageView(new Image("C:\\Users\\rolle\\IdeaProjects\\_18_roborally\\Board_Element_Pictures\\Blue_Arrow.PNG"));
        blueArrow.setFitHeight(SPACE_HEIGHT);
        blueArrow.setFitWidth(SPACE_WIDTH - 20);
        this.getChildren().add(blueArrow);
    }
}
