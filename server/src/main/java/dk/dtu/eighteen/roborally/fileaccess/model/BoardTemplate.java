package dk.dtu.eighteen.roborally.fileaccess.model;


import dk.dtu.eighteen.roborally.model.Board;
import dk.dtu.eighteen.roborally.model.Phase;
import dk.dtu.eighteen.roborally.model.Player;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Tobias Schønau s224327
 */
public class BoardTemplate {
    public Phase phase;
    public int width;
    public int height;
    public SpaceTemplate[][] spaces;
    public int turn;
    public List<PlayerTemplate> players = new ArrayList<>();


    /***
     * Template for saving Board as JSON.
     * Normal Board has circular dependencies. BoardTemplate and all subclasses don't
     * @param board Board being saved
     */
    public BoardTemplate(Board board) {
        this.height = board.height;
        this.width = board.width;
        this.phase = board.getPhase();
        this.turn = board.turn;
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

    /***
     * Creates BoardTemplate suitable for sending to players.
     * Only contains cards for the player given in parameter
     * @param board Board being converted to BoardTemplate
     * @param currentPlayer Player, whose cards are going into BoardTemplate
     */
    public BoardTemplate(Board board, Player currentPlayer) {
        this(board);
        this.players = new ArrayList<>();

        for (Player player : board.getPlayers()) {
            if (player.equals(currentPlayer)) {
                players.add(PlayerTemplate.createPlayerTemplate(player));
            } else {
                players.add(PlayerTemplate.createSecretPlayerTemplate(player));
            }
        }
    }


}

