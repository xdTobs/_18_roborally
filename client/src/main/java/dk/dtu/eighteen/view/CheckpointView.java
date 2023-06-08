package dk.dtu.eighteen.view;

import dk.dtu.eighteen.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class CheckpointView extends SpaceView{
    public CheckpointView(@NotNull Space space) {
        super(space);
        String filePath = new File("Board_Element_Pictures/Checkpoint1.png").toURI().toString();
        appendStyle("-fx-background-image: url('" + filePath + "');" +
        "-fx-background-size: 100% 100%;" +
                "-fx-background-repeat: no-repeat;" +
                "-fx-background-position: center;" +
                "-fx-rotate: 270;");
    }
}
