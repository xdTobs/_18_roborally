package dk.dtu.eighteen.roborally.fileaccess.model;

import dk.dtu.eighteen.roborally.model.Command;
import dk.dtu.eighteen.roborally.model.CommandCard;
/**
 * @author Tobias Schønau s224327
 */
public class CommandCardTemplate {
    public Command command;

    public CommandCardTemplate(CommandCard card) {
        this.command = card.command;
    }
}
