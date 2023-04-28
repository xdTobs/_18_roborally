package dk.dtu.compute.se.pisd.roborally.model;

public class ConveyorBelt extends Space {

    int checkpointNumber;

    public ConveyorBelt(Board board, int x, int y, int checkpointNumber) {
        super(board, x, y);
        this.checkpointNumber  = checkpointNumber;
    }




}
