package dk.dtu.eighteen.view;

import dk.dtu.eighteen.roborally.model.Space;

import java.io.File;

public class RotateLeftView extends SpaceView {
    public RotateLeftView(Space space) {
        super(space);
        String filePath = new File("client/src/main/resources/imgs/RotateLeft.png").toURI().toString();
        appendStyle(
                "-fx-background-image: url('" + filePath + "');" +
                        "-fx-background-size: 100% 100%;" +
                        "-fx-background-repeat: no-repeat;" +
                        "-fx-background-position: center;");
    }
}
