package dk.dtu.eighteen.view;

import dk.dtu.eighteen.roborally.controller.Actions.Checkpoint;
import dk.dtu.eighteen.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class CheckpointView extends SpaceView{
    public CheckpointView(@NotNull Space space, int checkpointNr) {
        super(space);
        //TODO: add all checkpoint numbers
        if(checkpointNr == 1) {
            String filePath = new File("client/src/main/resources/imgs/Checkpoint1.png").toURI().toString();
            appendStyle("-fx-background-image: url('" + filePath + "');" +
                    "-fx-background-size: 100% 100%;" +
                    "-fx-background-repeat: no-repeat;" +
                    "-fx-background-position: center;" +
                    "-fx-rotate: 270;");
        }

    }
}
