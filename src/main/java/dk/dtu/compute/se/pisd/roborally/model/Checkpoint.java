package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.controller.IFieldAction;

public class Checkpoint extends Space implements IFieldAction {

    int checkpointNumber;

    public Checkpoint(Board board, int x, int y, int checkpointNumber) {
        super(board, x, y);
        this.checkpointNumber = checkpointNumber;
    }


    @Override
    public boolean doAction(GameController gameController) {

        if(this.getPlayer().getCheckpointCounter()+1==checkpointNumber){
            this.getPlayer().incrementCheckpointCounter();
            return true;
        }
        return false;
    }
}
