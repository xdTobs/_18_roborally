package dk.dtu.eighteen.roborally.fileaccess.model;

import dk.dtu.eighteen.roborally.model.CommandCardField;

public class CommandCardFieldTemplate {

    public CommandCardFieldTemplate(CommandCardField commandCardField) {
        if(!(commandCardField.getCard()==null))
            this.card = new CommandCardTemplate(commandCardField.getCard());
        this.visible = commandCardField.isVisible();
    }
    public CommandCardTemplate card = null;

    public boolean visible;
}
