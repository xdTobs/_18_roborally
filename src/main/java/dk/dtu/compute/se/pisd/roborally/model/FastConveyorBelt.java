package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;

public class FastConveyorBelt extends ConveyorBelt {

    public FastConveyorBelt(Board board, int x, int y, Heading heading) {
        super(board, x, y, heading);
    }

    @Override
    public boolean doAction(GameController gameController) {

        gameController.moveForward_2(this.getPlayer());

        return true;
    }
}
