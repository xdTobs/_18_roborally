package dk.dtu.eighteen.roborally.controller.spaces;

import dk.dtu.eighteen.roborally.controller.Actions.IFieldAction;
import dk.dtu.eighteen.roborally.controller.GameController;
import dk.dtu.eighteen.roborally.model.Heading;
import dk.dtu.eighteen.roborally.model.Player;
import dk.dtu.eighteen.roborally.model.Space;

public class FastConveyorBelt implements IFieldAction {

    private Heading heading;


    public FastConveyorBelt(Heading heading) {
        this.heading = heading;
    }

    public Heading getHeading() {
        return this.heading;
    }


    @Override
    public boolean doAction(GameController gameController, Space space) {
        Player player = gameController.board.getPlayers().stream().filter(p -> p.x == space.x && p.y == space.y).findFirst().orElse(null);
        player.setHeading(heading);
        gameController.moveForward(player);
        gameController.moveForward(player);

        return true;
    }

}

