package dk.dtu.compute.se.pisd.roborally.fileaccess.model;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

import java.util.ArrayList;
import java.util.List;

public class BoardTemplate {
    public BoardTemplate(Board board){
        this.height = board.height;
        this.width = board.width;
        SpaceTemplate[][] spaceTemplates = new SpaceTemplate[board.getSpaces().length][board.getSpaces()[0].length];
        for (int i = 0; i < board.getSpaces().length; i++) {
            for (int j = 0; j < board.getSpaces()[0].length; j++) {
                spaceTemplates[i][j] = new SpaceTemplate(board.getSpace(i, j));
            }
        }
        this.spaces = spaceTemplates;

        for (Player p : board.getPlayers()){
            players.add(new PlayerTemplate(p));
        }
    }
    public int width;
    public int height;
    public SpaceTemplate[][]spaces;
    public List<PlayerTemplate> players = new ArrayList<>();



}

