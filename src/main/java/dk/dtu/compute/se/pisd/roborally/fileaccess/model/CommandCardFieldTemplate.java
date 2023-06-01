package dk.dtu.compute.se.pisd.roborally.fileaccess.model;

import dk.dtu.compute.se.pisd.roborally.model.CommandCard;
import dk.dtu.compute.se.pisd.roborally.model.Player;

public class CommandCardFieldTemplate {

    private CommandCard card;

    private boolean visible;

    public CommandCardFieldTemplate(CommandCard card, boolean visible) {
        this.card = card;
        this.visible = visible;
    }
}
