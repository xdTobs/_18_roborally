package dk.dtu.eighteen.roborally.model;

import dk.dtu.eighteen.roborally.controller.GameController;
import dk.dtu.eighteen.roborally.controller.spaces.ConveyorBelt;

public class FastTripleConveyorBeltNegative extends ConveyorBelt {

    public FastTripleConveyorBeltNegative(Heading heading) {
        super(heading);
    }

    @Override
    public boolean doAction(GameController gameController, Space space) {
        Heading playerHeading = space.getPlayer().getHeading();
        space.getPlayer().setHeading(super.getHeading());
        gameController.moveForward_2(space.getPlayer());
        space.getPlayer().setHeading(playerHeading);
        return true;
    }
}
