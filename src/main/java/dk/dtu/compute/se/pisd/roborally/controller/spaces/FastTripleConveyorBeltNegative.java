package dk.dtu.compute.se.pisd.roborally.controller.spaces;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class FastTripleConveyorBeltNegative extends ConveyorBelt {

    public FastTripleConveyorBeltNegative(Heading heading) {
        super(heading);
    }

    @Override
    public boolean doAction(GameController gameController, Space space) {
        space.getPlayer().setHeading(super.getHeading());
        gameController.moveForward_2(space.getPlayer());
        return true;
    }
}
