package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;

public class ConveyorBelt extends AbstractSpace {

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
    public boolean doAction(GameController gameController) {

        /*if (this.getPlayer().hasMovedThisTurn()) {
            return true;
        }*/

        this.getPlayer().setHeading(heading);
        gameController.moveForward(this.getPlayer());

        return true;
    }

    @Override
    void playerChanged() {

    }
}

