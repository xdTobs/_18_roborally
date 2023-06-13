package dk.dtu.eighteen.view;

import dk.dtu.eighteen.roborally.model.Space;

/**
 * @author Jakob Hansen, s224312@dtu.dk
 */
public class RotateRightView extends SpaceView {
    public RotateRightView(Space space) {
        super(space);
        appendStyle(
                "-fx-background-image: url(imgs/RotateRight.png);" +
                        "-fx-background-size: 100% 100%;" +
                        "-fx-background-repeat: no-repeat;" +
                        "-fx-background-position: center;");
    }
}
