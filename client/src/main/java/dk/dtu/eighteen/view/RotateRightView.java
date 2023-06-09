package dk.dtu.eighteen.view;
import dk.dtu.eighteen.roborally.controller.spaces.RotateRight;
import dk.dtu.eighteen.roborally.model.Space;
import org.jetbrains.annotations.NotNull;
import java.io.File;

public class RotateRightView extends SpaceView {
    public RotateRightView(@NotNull RotateRight rotateright, Space space) {
        super(space);
        String filePath = new File("client/src/main/resources/imgs/RotateRight.png").toURI().toString();
        appendStyle(
                "-fx-background-image: url('" + filePath + "');" +
                        "-fx-background-size: 100% 100%;" +
                        "-fx-background-repeat: no-repeat;" +
                        "-fx-background-position: center;");
    }
}
