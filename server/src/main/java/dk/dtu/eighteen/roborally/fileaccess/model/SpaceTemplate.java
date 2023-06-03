package dk.dtu.eighteen.roborally.fileaccess.model;


import dk.dtu.eighteen.roborally.controller.IFieldAction;
import dk.dtu.eighteen.roborally.model.Heading;
import dk.dtu.eighteen.roborally.model.Space;

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
