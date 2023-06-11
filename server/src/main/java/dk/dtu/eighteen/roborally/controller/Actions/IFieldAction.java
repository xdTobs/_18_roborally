package dk.dtu.eighteen.roborally.controller.Actions;

import dk.dtu.eighteen.roborally.controller.GameController;
import dk.dtu.eighteen.roborally.model.Space;

public interface IFieldAction {
    /**
     * Executes the field action for a given space. In order to be able to do
     * that the GameController associated with the game is passed to this method.
     *
     * @param gameController the gameController of the respective game
     * @return whether the action was successfully executed
     */
   boolean doAction(GameController gameController, Space space);
}
