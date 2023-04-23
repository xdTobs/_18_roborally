package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

public class ConveyorBeltView extends SpaceView{
    public ConveyorBeltView(@NotNull Space space) {
        super(space);
        appendStyle("-fx-background-color: green;");
    }
}
