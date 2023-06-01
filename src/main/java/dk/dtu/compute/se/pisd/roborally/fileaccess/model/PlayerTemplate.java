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
        this.x = p.;
        this.y = y;
        this.name = name;
        this.color = color;
        this.checkpointCounter = checkpointCounter;
        this.heading = heading;
        this.registerSlots = registerSlots;
        this.availableCardSlots = availableCardSlots;
    }
}
