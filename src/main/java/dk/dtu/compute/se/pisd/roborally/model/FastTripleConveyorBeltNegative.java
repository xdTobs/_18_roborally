package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;

public class FastTripleConveyorBeltNegative extends ConveyorBelt {

    public FastTripleConveyorBeltNegative(Board board, int x, int y, Heading heading) {super(board, x, y, heading);}

    @Override
    public boolean doAction(GameController gameController) {

        /*if (this.getPlayer().hasMovedThisTurn()) {
            return true;
        }*/

        this.getPlayer().setHeading(heading);
        gameController.moveForward_2(this.getPlayer());

        return true;
    }
}
