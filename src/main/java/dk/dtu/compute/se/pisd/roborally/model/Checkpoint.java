package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.controller.IFieldAction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Checkpoint extends Space implements IFieldAction {

    int checkpointNumber;
    Set<Player> playerLanded = new HashSet<>();

    public Checkpoint(Board board, int x, int y, int checkpointNumber) {
        super(board, x, y);
        this.checkpointNumber = checkpointNumber;
    }

    public Set<Player> getPlayersLanded() {
        return playerLanded;
    }

    @Override
    public boolean doAction(GameController gameController) {
        playerLanded.add(getPlayer());
        return true;
    }
}
