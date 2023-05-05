package dk.dtu.compute.se.pisd.roborally.fileaccess.model;

import dk.dtu.compute.se.pisd.roborally.controller.IFieldAction;
import dk.dtu.compute.se.pisd.roborally.model.Heading;

import java.util.ArrayList;
import java.util.List;

public class SpaceTemplate {

    public List<Heading> walls = new ArrayList<>();
    public List<IFieldAction> actions = new ArrayList<>();

    public int x;
    public int y;

}
