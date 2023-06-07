package dk.dtu.eighteen.roborally.fileaccess.model;

import dk.dtu.eighteen.roborally.model.Board;
import dk.dtu.eighteen.roborally.model.Player;

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
    public BoardTemplate(Board board, int playerId){
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
        for (int i = 0; i < board.getNumberOfPlayers(); i++) {
            if(playerId==i)
                players.add(new PlayerTemplate(board.getPlayers().get(i)));
            else
                players.add(new PlayerTemplate(board.getPlayers().get(i),true));

        }
    }

    public int width;
    public int height;
    public SpaceTemplate[][]spaces;
    public List<PlayerTemplate> players = new ArrayList<>();



}

