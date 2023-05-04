package dk.dtu.compute.se.pisd.roborally.controller.spaces;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Space;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Checkpoint extends FieldAction {

    private int checkpointNumber;

    public Checkpoint(int checkpointNumber){
        this.checkpointNumber = checkpointNumber;
    }

    public int getCheckpointNumber() {
        return checkpointNumber;
    }




    @Override
    public boolean doAction(GameController gameController, Space space) {

        if(space.getPlayer().getCheckpointCounter()+1==checkpointNumber){
            space.getPlayer().incrementCheckpointCounter();
            return true;
        }
        return false;
    }
}
