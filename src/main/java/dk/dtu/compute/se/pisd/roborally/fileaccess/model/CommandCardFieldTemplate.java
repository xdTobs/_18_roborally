package dk.dtu.compute.se.pisd.roborally.fileaccess.model;

import dk.dtu.compute.se.pisd.roborally.model.CommandCard;
import dk.dtu.compute.se.pisd.roborally.model.CommandCardField;
import dk.dtu.compute.se.pisd.roborally.model.Player;

public class CommandCardFieldTemplate {

    public CommandCardFieldTemplate(CommandCardField commandCardField) {
        this.card = new CommandCardTemplate(commandCardField.getCard());
        this.visible = commandCardField.isVisible();
    }

    public CommandCardTemplate card;

    public boolean visible;
}
