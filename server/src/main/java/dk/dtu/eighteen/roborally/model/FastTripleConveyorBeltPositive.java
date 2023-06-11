package dk.dtu.eighteen.roborally.model;

import dk.dtu.eighteen.roborally.controller.GameController;
import dk.dtu.eighteen.roborally.controller.spaces.ConveyorBelt;

public class FastTripleConveyorBeltPositive extends ConveyorBelt {

    public FastTripleConveyorBeltPositive(Heading heading) {
        super(heading);
    }

    @Override
    public boolean doAction(GameController gameController, Space space) {
        Player player = space.getPlayer();

        Heading playerHeading = player.getHeading();
        player.setHeading(super.getHeading());
        gameController.moveForward(player);
        player.setHeading(playerHeading);

        return true;
    }
}
