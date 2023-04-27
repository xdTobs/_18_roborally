package dk.dtu.compute.se.pisd.roborally.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Checkpoint extends Space {
    List<Player> playersArrived = new ArrayList<>();

    public Checkpoint(Board board, int x, int y) {
        super(board, x, y);
    }
//@todo checkpoit created, but not implemented
    public void playerLandedHere(Player p){
        playersArrived.add(p);
    }
}
