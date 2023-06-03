package dk.dtu.eighteen.roborally.model;

import dk.dtu.eighteen.roborally.controller.IFieldAction;
import dk.dtu.eighteen.roborally.controller.GameController;

public class ConveyorBelt extends Space implements IFieldAction {

    Heading heading;

    public ConveyorBelt(Board board, int x, int y, Heading heading) {
        super(board, x, y);
        this.heading = heading;
    }

    public Heading getHeading() {
        return this.heading;
    }

    public void setHeading(Heading heading) {
        this.heading = heading;
    }


    @Override
    public boolean doAction(GameController gameController, Space space) {
        /*if (this.getPlayer().hasMovedThisTurn()) {
            return true;
        }*/

        this.getPlayer().setHeading(heading);
        gameController.moveForward(this.getPlayer());

        return true;

    }
}

