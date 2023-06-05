package dk.dtu.eighteen.roborally.fileaccess.model;

import dk.dtu.eighteen.roborally.model.Heading;
import dk.dtu.eighteen.roborally.model.Move;
import dk.dtu.eighteen.roborally.model.Player;

public class PlayerTemplate {
    public Move currentMove;
    public int x,y;
    public String name;
    public String color;
    public int checkpointCounter;
    public Heading heading;
    public CommandCardFieldTemplate[] availableCardSlots;

    public PlayerTemplate(Player p) {
        this.x = p.getSpace().x;
        this.y = p.getSpace().y;
        this.name = p.getName();
        this.color = p.getColor();
        this.checkpointCounter = p.getCheckpointCounter();
        this.heading = p.getHeading();
        this.currentMove = p.getCurrentMove();

        CommandCardFieldTemplate[] availableSlotsTemplate = new CommandCardFieldTemplate[p.getAvailableCardSlots().length];
        for (int i = 0; i < p.getAvailableCardSlots().length; i++) {
            availableSlotsTemplate[i] = new CommandCardFieldTemplate(p.getAvailableCardSlot(i));
        }
        this.availableCardSlots = availableSlotsTemplate;
    }
}

