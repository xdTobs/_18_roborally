package dk.dtu.eighteen.view;

import dk.dtu.eighteen.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

/**
 * @author Frederik Rolsted, s224299@dtu.dk
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
            appendStyle("-fx-background-image: url(imgs/checkpoint2.png);" +
                    "-fx-background-size: 100% 100%;" +
                    "-fx-background-repeat: no-repeat;" +
                    "-fx-background-position: center;");
        }
        if (checkpointNr == 3) {
            appendStyle("-fx-background-image: url(imgs/checkpoint3.png);" +
                    "-fx-background-size: 100% 100%;" +
                    "-fx-background-repeat: no-repeat;" +
                    "-fx-background-position: center;");
        }
        if (checkpointNr == 4) {
            appendStyle("-fx-background-image: url(imgs/checkpoint4.png);" +
                    "-fx-background-size: 100% 100%;" +
                    "-fx-background-repeat: no-repeat;" +
                    "-fx-background-position: center;");
        }

    }
}
