package dk.dtu.compute.se.pisd.roborally.fileaccess.model;

import java.util.ArrayList;
import java.util.List;

public class BoardTemplate {

    public int width;
    public int height;

    public List<SpaceTemplate> spaces = new ArrayList<>();
    public List<PlayerTemplate> players = new ArrayList<>();

}

