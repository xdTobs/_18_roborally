package dk.dtu.eighteen.roborally.controller.spaces;

import dk.dtu.eighteen.roborally.controller.Actions.IFieldAction;
import dk.dtu.eighteen.roborally.controller.GameController;
import dk.dtu.eighteen.roborally.model.Player;
import dk.dtu.eighteen.roborally.model.Space;

public class RotateRight implements IFieldAction {

    public RotateRight() {
    }

    @Override
    public void doAction(GameController gameController, Space space) {
        Player player = space.getPlayer();
        if(player != null){
            player.setHeading(player.getHeading().next());
        }
    }

}
