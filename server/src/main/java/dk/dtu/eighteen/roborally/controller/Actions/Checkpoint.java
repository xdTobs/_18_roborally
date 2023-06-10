package dk.dtu.eighteen.roborally.controller.Actions;

import dk.dtu.eighteen.roborally.controller.GameController;
import dk.dtu.eighteen.roborally.model.Space;

public class Checkpoint implements IFieldAction {

    private int checkpointNumber;

    public Checkpoint(int checkpointNumber) {
        this.checkpointNumber = checkpointNumber;
    }

    public int getCheckpointNumber() {
        return checkpointNumber;
    }


    @Override
    public boolean doAction(GameController gameController, Space space) {

        if (space.getPlayer().getCheckpointCounter() + 1 == checkpointNumber) {
            space.getPlayer().incrementCheckpointCounter();
            return true;
        }
        return false;
    }
}
