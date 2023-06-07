package dk.dtu.eighteen.roborally.fileaccess.model;

import dk.dtu.eighteen.roborally.model.Heading;
import dk.dtu.eighteen.roborally.model.Moves;
import dk.dtu.eighteen.roborally.model.Player;

public class PlayerTemplate {
    public Moves currentMoves;
    public int x, y;
    public String name;
    public String color;
    public int checkpointCounter;
    public Heading heading;
    public CommandCardFieldTemplate[] availableCardSlots;

    private PlayerTemplate(Player p, CommandCardFieldTemplate[] availableSlotsTemplate) {
        this.x = p.getSpace().x;
        this.y = p.getSpace().y;
        this.name = p.getName();
        this.color = p.getColor();
        this.checkpointCounter = p.getCheckpointCounter();
        this.heading = p.getHeading();
        this.currentMoves = p.getCurrentMove();
        this.availableCardSlots = availableSlotsTemplate;
    }

    public static PlayerTemplate createPlayerTemplate(Player p) {
        CommandCardFieldTemplate[] availableSlotsTemplate = new CommandCardFieldTemplate[p.getAvailableCardSlots().length];
        for (int i = 0; i < p.getAvailableCardSlots().length; i++) {
            availableSlotsTemplate[i] = new CommandCardFieldTemplate(p.getAvailableCardSlot(i));
        }
        return new PlayerTemplate(p, availableSlotsTemplate);
    }

    public static PlayerTemplate createSecretPlayerTemplate(Player p) {
        return new PlayerTemplate(p, null);
    }
}

