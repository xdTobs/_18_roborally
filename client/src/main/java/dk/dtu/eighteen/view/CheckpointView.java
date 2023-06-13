package dk.dtu.eighteen.view;

import dk.dtu.eighteen.roborally.controller.Actions.Checkpoint;
import dk.dtu.eighteen.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class CheckpointView extends SpaceView {
    public CheckpointView(@NotNull Space space, int checkpointNr) {
        super(space);
        if (checkpointNr == 1) {
            String filePath = new File("client/src/main/resources/imgs/Checkpoint1.png").toURI().toString();
            appendStyle("-fx-background-image: url('" + filePath + "');" +
                    "-fx-background-size: 100% 100%;" +
                    "-fx-background-repeat: no-repeat;" +
                    "-fx-background-position: center;");
        }
        if (checkpointNr == 2) {
            String filePath = new File("client/src/main/resources/imgs/Checkpoint2.png").toURI().toString();
            appendStyle("-fx-background-image: url('" + filePath + "');" +
                    "-fx-background-size: 100% 100%;" +
                    "-fx-background-repeat: no-repeat;" +
                    "-fx-background-position: center;");
        }
        if (checkpointNr == 3) {
            String filePath = new File("client/src/main/resources/imgs/Checkpoint3.png").toURI().toString();
            appendStyle("-fx-background-image: url('" + filePath + "');" +
                    "-fx-background-size: 100% 100%;" +
                    "-fx-background-repeat: no-repeat;" +
                    "-fx-background-position: center;");
        }
        if (checkpointNr == 4) {
            String filePath = new File("client/src/main/resources/imgs/Checkpoint4.png").toURI().toString();
            appendStyle("-fx-background-image: url('" + filePath + "');" +
                    "-fx-background-size: 100% 100%;" +
                    "-fx-background-repeat: no-repeat;" +
                    "-fx-background-position: center;");
        }

    }
}
