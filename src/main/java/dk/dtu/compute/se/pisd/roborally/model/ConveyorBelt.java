package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.controller.IFieldAction;

public class ConveyorBelt extends Space implements IFieldAction {

    Heading heading;

    public ConveyorBelt(Board board, int x, int y, Heading heading) {
        super(x, y);
        this.heading = heading;
    }


    @Override
    public boolean doAction(GameController gameController) {
        return false;
    }

}

