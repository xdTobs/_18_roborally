package dk.dtu.compute.se.pisd.roborally.fileaccess.model;

import dk.dtu.compute.se.pisd.roborally.model.CommandCardField;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.SOUTH;

public class PlayerTemplate {
    public int x,y;
    public String name;
    public String color;
    public int checkpointCounter;
    public Heading heading;
    public CommandCardFieldTemplate[] registerSlots;
    public CommandCardFieldTemplate[] availableCardSlots;

    public PlayerTemplate(Player p) {
        this.x = p.getSpace().x;
        this.y = p.getSpace().y;
        this.name = p.getName();
        this.color = p.getColor();
        this.checkpointCounter = p.getCheckpointCounter();
        this.heading = p.getHeading();
        CommandCardFieldTemplate[] registerSlotsTemplate = new CommandCardFieldTemplate[p.getRegisterSlots().length];
        for (int i = 0; i < p.getRegisterSlots().length; i++) {
            registerSlotsTemplate[i] = new CommandCardFieldTemplate(p.getRegisterSlot(i));
        }
        this.registerSlots = registerSlotsTemplate;

        CommandCardFieldTemplate[] availableSlotsTemplate = new CommandCardFieldTemplate[p.getAvailableCardSlots().length];
        for (int i = 0; i < p.getAvailableCardSlots().length; i++) {
            registerSlotsTemplate[i] = new CommandCardFieldTemplate(p.getAvailableCardSlot(i));
        }
        this.availableCardSlots = availableSlotsTemplate;
    }
}

