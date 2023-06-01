package dk.dtu.compute.se.pisd.roborally.fileaccess.model;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;

import java.util.ArrayList;
import java.util.List;

public class SpaceTemplate {
    public SpaceTemplate(Space space){
        this.playerOnField = space.getPlayer();
        //TODO make get PlayerTemplate instead
        this.actions = space.getActions();
        this.y = space.y;
        this.x = space.x;
        this.walls = space.getWalls();
        //TODO figure out set or walls

    }
    public List<Heading> walls = new ArrayList<>();
    public List<FieldAction> actions = new ArrayList<>();
    public PlayerTemplate playerOnField;
    public int x;
    public int y;

}
