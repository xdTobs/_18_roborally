package dk.dtu.eighteen.roborally.controller.spaces;

import dk.dtu.eighteen.roborally.controller.Actions.IFieldAction;
import dk.dtu.eighteen.roborally.controller.GameController;
import dk.dtu.eighteen.roborally.model.Space;

public class Rotate implements IFieldAction {

    public Rotate(){}
    @Override
    public boolean doAction(GameController gameController, Space space) {
        space.getPlayer().setHeading(space.getPlayer().getHeading().prev());;
        return true;
    }

}
