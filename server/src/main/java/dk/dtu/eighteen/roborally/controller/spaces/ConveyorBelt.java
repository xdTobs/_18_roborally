package dk.dtu.eighteen.roborally.controller.spaces;

import dk.dtu.eighteen.roborally.controller.GameController;
import dk.dtu.eighteen.roborally.controller.IFieldAction;
import dk.dtu.eighteen.roborally.model.Heading;
import dk.dtu.eighteen.roborally.model.Space;

public class ConveyorBelt implements IFieldAction {

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

