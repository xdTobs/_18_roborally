package dk.dtu.eighteen.roborally.fileaccess.model;


import dk.dtu.eighteen.roborally.model.Heading;
import dk.dtu.eighteen.roborally.model.IFieldAction;
import dk.dtu.eighteen.roborally.model.Space;

import java.util.List;
import java.util.Set;

public class SpaceTemplate {
    public Set<Heading> walls;
    public List<IFieldAction> actions;
    public int x;
    public int y;

    public SpaceTemplate(Space space) {
        this.actions = space.getActions();
        this.y = space.y;
        this.x = space.x;
        this.walls = space.getWalls();

    }

}
