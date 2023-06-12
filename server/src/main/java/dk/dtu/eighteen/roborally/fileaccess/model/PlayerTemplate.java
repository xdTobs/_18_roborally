package dk.dtu.eighteen.roborally.fileaccess.model;

import dk.dtu.eighteen.roborally.model.Heading;
import dk.dtu.eighteen.roborally.model.Player;

public class PlayerTemplate {

    public int x, y;
    public String name;
    public String color;
    public int checkpointCounter;
    public Heading heading;
    public CommandCardFieldTemplate[] playableCards;


    private PlayerTemplate(Player p, CommandCardFieldTemplate[] availableSlotsTemplate) {
        this.x = p.getSpace().x;
        this.y = p.getSpace().y;
        this.name = p.getName();
        this.color = p.getColor();
        this.checkpointCounter = p.getCheckpointCounter();
        this.heading = p.getHeading();
        this.playableCards = availableSlotsTemplate;
    }
    /***
     * Function used by BoardTemplate constructor, to create PlayerTemplates in BoardTemplate
     *
     */
    public static PlayerTemplate createPlayerTemplate(Player p) {
        CommandCardFieldTemplate[] availableSlotsTemplate = new CommandCardFieldTemplate[p.getPlayableCards().length];
        for (int i = 0; i < p.getPlayableCards().length; i++) {
            availableSlotsTemplate[i] = new CommandCardFieldTemplate(p.getPlayableCardField(i));
        }
        return new PlayerTemplate(p, availableSlotsTemplate);
    }

    public static PlayerTemplate createSecretPlayerTemplate(Player p) {
        return new PlayerTemplate(p, null);
    }
}

