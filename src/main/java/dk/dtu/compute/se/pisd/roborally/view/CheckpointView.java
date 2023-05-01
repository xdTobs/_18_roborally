package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

public class CheckpointView extends SpaceView{
    public CheckpointView(@NotNull Space space) {
        super(space);
        appendStyle("-fx-background-color: yellow;");
    }
}
