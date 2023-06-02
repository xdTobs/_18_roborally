package dk.dtu.compute.se.pisd.roborally.fileaccess.model;

import dk.dtu.compute.se.pisd.roborally.model.Command;
import dk.dtu.compute.se.pisd.roborally.model.CommandCard;

public class CommandCardTemplate {
    public CommandCardTemplate(CommandCard card){
        this.command =card.command;
    }
    public Command command;
}
