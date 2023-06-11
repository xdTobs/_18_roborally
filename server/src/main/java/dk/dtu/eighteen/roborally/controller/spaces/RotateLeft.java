package dk.dtu.eighteen.roborally.controller.spaces;

import dk.dtu.eighteen.roborally.controller.Actions.IFieldAction;
import dk.dtu.eighteen.roborally.controller.GameController;
import dk.dtu.eighteen.roborally.model.Player;
import dk.dtu.eighteen.roborally.model.Space;

public class RotateLeft implements IFieldAction {

    public RotateLeft(){}
    @Override
    public boolean doAction(GameController gameController, Space space) {
        Player player = gameController.board.getPlayers().stream().filter(p -> p.x == space.x && p.y == space.y).findFirst().orElse(null);
        player.setHeading(player.getHeading().prev());
        return true;
    }

}
