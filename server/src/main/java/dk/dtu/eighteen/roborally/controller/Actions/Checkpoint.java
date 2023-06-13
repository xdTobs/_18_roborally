package dk.dtu.eighteen.roborally.controller.Actions;

import dk.dtu.eighteen.roborally.controller.GameController;
import dk.dtu.eighteen.roborally.model.Player;
import dk.dtu.eighteen.roborally.model.Space;

public class Checkpoint implements IFieldAction {

    private final int checkpointNumber;

    public Checkpoint(int checkpointNumber) {
        this.checkpointNumber = checkpointNumber;
    }

    public int getCheckpointNumber() {
        return checkpointNumber;
    }


    @Override
    public void doAction(GameController gameController, Space space) {
        Player player = space.getPlayer();


        if (player.getCheckpointCounter() + 1 == checkpointNumber) {
            player.incrementCheckpointCounter();
        }
    }
}
