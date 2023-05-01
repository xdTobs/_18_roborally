package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

public class FastConveyorBeltView extends SpaceView{
    public FastConveyorBeltView(@NotNull Space space) {
        super(space);
        appendStyle("-fx-background-color: blue;");
    }
}
