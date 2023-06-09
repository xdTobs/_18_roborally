package dk.dtu.eighteen.roborally.controller.spaces;

import dk.dtu.eighteen.roborally.controller.Actions.IFieldAction;
import dk.dtu.eighteen.roborally.controller.GameController;
import dk.dtu.eighteen.roborally.model.Space;

public class RotateRight implements IFieldAction {

    public RotateRight(){}
    @Override
    public boolean doAction(GameController gameController, Space space) {
        space.getPlayer().setHeading(space.getPlayer().getHeading().next());;
        return true;
    }

}
