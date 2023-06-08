package dk.dtu.eighteen.view;

import dk.dtu.eighteen.roborally.controller.spaces.ConveyorBelt;
import dk.dtu.eighteen.roborally.controller.spaces.FastConveyorBelt;
import dk.dtu.eighteen.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

import java.io.File;

import static dk.dtu.eighteen.roborally.model.Heading.*;

public class FastConveyorBeltView extends SpaceView {
    public FastConveyorBeltView(@NotNull FastConveyorBelt fastconveyorBelt, Space space) {
        super(space);
        if (fastconveyorBelt.getHeading() == EAST) {
            String filePath = new File("client/src/main/resources/imgs/blue-arrow-east.png").toURI().toString();
            appendStyle("-fx-background-image: url('" + filePath + "');" +
                    "-fx-background-size: 100% 100%;" +
                    "-fx-background-repeat: no-repeat;" +
                    "-fx-background-position: center;");

        } else if (fastconveyorBelt.getHeading() == SOUTH) {
            String filePath = new File("client/src/main/resources/imgs/blue-arrow-south.png").toURI().toString();
            appendStyle("-fx-background-image: url('" + filePath + "');" +
                    "-fx-background-size: 100% 100%;" +
                    "-fx-background-repeat: no-repeat;" +
                    "-fx-background-position: center;");
        } else if (fastconveyorBelt.getHeading() == WEST) {
            String filePath = new File("client/src/main/resources/imgs/blue-arrow-west.png").toURI().toString();
            appendStyle("-fx-background-image: url('" + filePath + "');" +
                    "-fx-background-size: 100% 100%;" +
                    "-fx-background-repeat: no-repeat;" +
                    "-fx-background-position: center;");
        } else if (fastconveyorBelt.getHeading() == NORTH) {
            String filePath = new File("client/src/main/resources/imgs/blue-arrow-north.png").toURI().toString();
            appendStyle("-fx-background-image: url('" + filePath + "');" +
                    "-fx-background-size: 100% 100%;" +
                    "-fx-background-repeat: no-repeat;" +
                    "-fx-background-position: center;");
        }
    }
}

//package dk.dtu.eighteen.view;
//
//import dk.dtu.eighteen.roborally.model.Space;
//import org.jetbrains.annotations.NotNull;
//
//public class FastConveyorBeltView extends SpaceView {
//    public FastConveyorBeltView(@NotNull Space space) {
//        super(space);
////        super(conveyorBelt);
////        String filename = switch (conveyorBelt.getHeading()) {
////            case NORTH -> "blue-arrow-north.png";
////            case EAST -> "blue-arrow-east.png";
////            case SOUTH -> "blue-arrow-south.png";
////            case WEST -> "blue-arrow-west.png";
////        };
////        String imageFile = new File("Board_Element_Pictures/" + filename).toURI().toString();
////        appendStyle("-fx-background-image: url('" + imageFile + "');" +
////                "-fx-background-size: 100% 100%;" +
////                "-fx-background-repeat: no-repeat;" +
////                "-fx-background-position: center;");
//    }
//}
