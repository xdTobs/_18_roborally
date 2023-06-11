package dk.dtu.eighteen.roborally.controller.Actions;

import dk.dtu.eighteen.roborally.controller.GameController;
import dk.dtu.eighteen.roborally.model.Player;
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
        Player player = gameController.board.getPlayers().stream().filter(p -> p.x == space.x && p.y == space.y).findFirst().orElse(null);

        if (player.getCheckpointCounter() + 1 == checkpointNumber) {
            player.incrementCheckpointCounter();
            return true;
        }
        return false;
    }
}
