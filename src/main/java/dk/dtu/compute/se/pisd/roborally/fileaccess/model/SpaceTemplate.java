package dk.dtu.compute.se.pisd.roborally.fileaccess.model;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.IFieldAction;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SpaceTemplate {
    public SpaceTemplate(Space space){
        this.actions = space.getActions();
        this.y = space.y;
        this.x = space.x;
        this.walls = space.getWalls();

    }
    public Set<Heading> walls;
    public List<IFieldAction> actions;
    public int x;
    public int y;

}
