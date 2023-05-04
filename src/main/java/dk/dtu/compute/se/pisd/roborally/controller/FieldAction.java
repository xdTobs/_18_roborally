package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Space;

public abstract class FieldAction {
    /**
     * Executes the field action for a given space. In order to be able to do
     * that the GameController associated with the game is passed to this method.
     *
     * @param gameController the gameController of the respective game
     * @return whether the action was successfully executed
     */
    public abstract boolean doAction(GameController gameController, Space space);
}
