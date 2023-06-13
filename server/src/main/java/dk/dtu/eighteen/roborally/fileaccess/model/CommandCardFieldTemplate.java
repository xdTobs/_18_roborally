package dk.dtu.eighteen.roborally.fileaccess.model;

import dk.dtu.eighteen.roborally.model.CommandCardField;
/**
 * @author Tobias Schønau s224327
 */
public class CommandCardFieldTemplate {

    public CommandCardTemplate card = null;
    public boolean visible;

    /***
     * Constructor used by BoardTemplate to create CommandCardFieldTemplate
     * @param commandCardField Field being converted to Template
     */
    public CommandCardFieldTemplate(CommandCardField commandCardField) {
        if (!(commandCardField.getCard() == null))
            this.card = new CommandCardTemplate(commandCardField.getCard());
        this.visible = commandCardField.isVisible();
    }
}