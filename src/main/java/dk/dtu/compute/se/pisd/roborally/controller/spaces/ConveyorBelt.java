package dk.dtu.compute.se.pisd.roborally.controller.spaces;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class ConveyorBelt extends FieldAction {

    private Heading heading;


    public ConveyorBelt(Heading heading) {
        this.heading = heading;
    }

    public Heading getHeading() {
        return this.heading;
    }


    @Override
    public boolean doAction(GameController gameController, Space space) {
        space.getPlayer().setHeading(heading);
        gameController.moveForward(space.getPlayer());

        return true;
    }

}

