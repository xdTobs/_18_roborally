package dk.dtu.eighteen.roborally.controller.spaces;

import dk.dtu.eighteen.roborally.controller.Actions.IFieldAction;
import dk.dtu.eighteen.roborally.controller.GameController;
import dk.dtu.eighteen.roborally.model.Heading;
import dk.dtu.eighteen.roborally.model.Player;
import dk.dtu.eighteen.roborally.model.Space;

/**
 * @author Jakob Hansen, s224312@dtu.dk
 */
public class FastConveyorBelt implements IFieldAction {

    private final Heading heading;


    public FastConveyorBelt(Heading heading) {
        this.heading = heading;
    }

    public Heading getHeading() {
        return this.heading;
    }


    @Override
    public void doAction(GameController gameController, Space space) {
        Player player = space.getPlayer();

        player.setHeading(heading);
        gameController.moveForward(player);
        gameController.moveForward(player);

    }

}

