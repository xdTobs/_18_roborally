package dk.dtu.eighteen.roborally.fileaccess.model;


import dk.dtu.eighteen.roborally.model.Board;
import dk.dtu.eighteen.roborally.model.Phase;
import dk.dtu.eighteen.roborally.model.Player;

import java.util.ArrayList;
import java.util.List;

public class BoardTemplate {
    public Phase phase;
    public int width;
    public int height;
    public SpaceTemplate[][] spaces;
    public List<PlayerTemplate> players = new ArrayList<>();


    private BoardTemplate() {

    }

    public BoardTemplate(Board board) {
        this.height = board.height;
        this.width = board.width;
        this.phase = board.getPhase();
        SpaceTemplate[][] spaceTemplates = new SpaceTemplate[board.getSpaces().length][board.getSpaces()[0].length];
        for (int i = 0; i < board.getSpaces().length; i++) {
            for (int j = 0; j < board.getSpaces()[0].length; j++) {
                spaceTemplates[i][j] = new SpaceTemplate(board.getSpace(i, j));
            }
        }
        this.spaces = spaceTemplates;

        for (Player p : board.getPlayers()) {
            players.add(PlayerTemplate.createPlayerTemplate(p));
        }
    }

    public BoardTemplate(Board board, Player currentPlayer) {
        this.height = board.height;
        this.width = board.width;
        this.phase = board.getPhase();
        SpaceTemplate[][] spaceTemplates = new SpaceTemplate[board.getSpaces().length][board.getSpaces()[0].length];
        for (int i = 0; i < board.getSpaces().length; i++) {
            for (int j = 0; j < board.getSpaces()[0].length; j++) {
                spaceTemplates[i][j] = new SpaceTemplate(board.getSpace(i, j));
            }
        }
        this.spaces = spaceTemplates;

        for (Player player : board.getPlayers()) {
            if (player.equals(currentPlayer)) {
                players.add(PlayerTemplate.createPlayerTemplate(player));
            } else {
                players.add(PlayerTemplate.createSecretPlayerTemplate(player));
            }
        }
    }


//    public static BoardTemplate createBoardTemplate(JSONObject jsonObject) {
//        BoardTemplate bt = new BoardTemplate(null, null);
//        bt.width = (int) jsonObject.get("width");
//        bt.height = (int) jsonObject.get("height");
//        return bt;
//    }
}

