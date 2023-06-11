package dk.dtu.eighteen.roborally.model;

import dk.dtu.eighteen.roborally.controller.GameController;
import dk.dtu.eighteen.roborally.controller.spaces.ConveyorBelt;

public class FastTripleConveyorBeltNegative extends ConveyorBelt {

    public FastTripleConveyorBeltNegative(Heading heading) {
        super(heading);
    }

    @Override
    public boolean doAction(GameController gameController, Space space) {

        Player player = gameController.board.getPlayers().stream().filter(p -> p.x == space.x && p.y == space.y).findFirst().orElse(null);
        Heading playerHeading = player.getHeading();
        player.setHeading(super.getHeading());
        gameController.moveForward(player);
        player.setHeading(playerHeading);
        return true;
    }
}
