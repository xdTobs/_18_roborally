package dk.dtu.eighteen.view;

import dk.dtu.eighteen.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * @Author Frederik Rolsted, s22499@dtu.dk
 * @Author Jakob Hansen
 */
public class CheckpointView extends SpaceView {
    public CheckpointView(@NotNull Space space, int checkpointNr) {
        super(space);
        if (checkpointNr == 1) {
            appendStyle("-fx-background-image: url(imgs/Checkpoint1.png);" +
                    "-fx-background-size: 100% 100%;" +
                    "-fx-background-repeat: no-repeat;" +
                    "-fx-background-position: center;");
        }
        if (checkpointNr == 2) {
            appendStyle("-fx-background-image: url(imgs/Checkpoint2.png);" +
                    "-fx-background-size: 100% 100%;" +
                    "-fx-background-repeat: no-repeat;" +
                    "-fx-background-position: center;");
        }
        if (checkpointNr == 3) {
            appendStyle("-fx-background-image: url(imgs/Checkpoint3.png);" +
                    "-fx-background-size: 100% 100%;" +
                    "-fx-background-repeat: no-repeat;" +
                    "-fx-background-position: center;");
        }
        if (checkpointNr == 4) {
            appendStyle("-fx-background-image: url(imgs/Checkpoint4.png);" +
                    "-fx-background-size: 100% 100%;" +
                    "-fx-background-repeat: no-repeat;" +
                    "-fx-background-position: center;");
        }

    }
}
