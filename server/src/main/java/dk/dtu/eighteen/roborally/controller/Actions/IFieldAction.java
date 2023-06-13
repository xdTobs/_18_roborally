package dk.dtu.eighteen.roborally.controller.Actions;

import dk.dtu.eighteen.roborally.controller.GameController;
import dk.dtu.eighteen.roborally.model.Space;

/**
 * Different actions that a field can have, e.g., Laser, ConveyorBelt,
 * Rotator etc. Walls are not included in IFieldAction, but is instead a
 * seperate property on the field.
 *
 *  @author Henrik Zenkert, s224281@dtu.dk
 */
public interface IFieldAction {
    /**
     * Executes the field action for a given space. In order to be able to do
     * that the GameController associated with the game is passed to this method.
     *
     * @param gameController the gameController of the respective game
     */
    void doAction(GameController gameController, Space space);
}
