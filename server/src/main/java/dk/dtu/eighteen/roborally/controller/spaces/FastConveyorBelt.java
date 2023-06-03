package dk.dtu.eighteen.roborally.controller.spaces;

import dk.dtu.eighteen.roborally.controller.GameController;
import dk.dtu.eighteen.roborally.model.Heading;
import dk.dtu.eighteen.roborally.model.Space;

public class FastConveyorBelt extends ConveyorBelt {


    public FastConveyorBelt(Heading heading) {
        super(heading);
    }

    @Override
    public boolean doAction(GameController gameController, Space space) {
        space.getPlayer().setHeading(super.getHeading());
        gameController.moveForward_2(space.getPlayer());
        return true;
    }
}
