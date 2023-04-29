package dk.dtu.compute.se.pisd.roborally.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Checkpoint extends Space {

    int checkpointNumber;
    List<Player> playersArrived = new ArrayList<>();

    public Checkpoint(Board board, int x, int y, int checkpointNumber) {
        super(board, x, y);
        this.checkpointNumber = checkpointNumber;
    }

    //@todo checkpoit created, but not implemented
    public void playerLandedHere(Player p){
        playersArrived.add(p);
    }
}
