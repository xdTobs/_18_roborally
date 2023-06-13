package dk.dtu.eighteen.roborally.fileaccess;


import dk.dtu.eighteen.roborally.model.Board;
import dk.dtu.eighteen.roborally.model.Player;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * This tests all the LoadBoard class.
 *
 * @Author Frederik Rolsted, s224299@dtu.dk
 */
public class LoadBoardTest {

    @Test
    public void savingAndLoadingGame() throws IOException {

        LoadBoard.saveFolder = new File("./savedGames");
        if (!LoadBoard.saveFolder.exists()) {
            LoadBoard.saveFolder.mkdir();
        }
        Board board = LoadBoard.loadNewGameFromFile("DIZZY_HIGHWAY.json");
        board.createAddPlayerToEmptySpace("PURPLE", "player1");

        try {
            LoadBoard.saveBoard(board, "savedTestBoard.json");
            Board board2 = LoadBoard.loadSavedGameFromFile("savedTestBoard.json");
            Player playerFromOrigBoard = board.getPlayer(0);
            Player playerFromSavedBoard = board2.getPlayer(0);

            // Assert relevant attributes of the boards
            assertEquals(board.height, board2.height);
            assertEquals(board.width, board2.width);
            assertEquals(board.getPhase(), board2.getPhase());
            assertEquals(board.getStep(), board2.getStep());

            // Check if spacetypes are the same on both boards with a wall
            assertEquals(board.getSpace(1, 2).getWalls(), board2.getSpace(1, 2).getWalls());

            // Assert relevant attributes of the players
            assertEquals(playerFromOrigBoard.getName(), playerFromSavedBoard.getName());
            assertEquals(playerFromOrigBoard.getColor(), playerFromSavedBoard.getColor());
            assertEquals(playerFromOrigBoard.getSpace().x, playerFromSavedBoard.getSpace().x);
            assertEquals(playerFromOrigBoard.getSpace().y, playerFromSavedBoard.getSpace().y);
        } finally {
            File savedBoardFile = new File(LoadBoard.saveFolder, "savedTestBoard.json");
            if (savedBoardFile.exists()) {
                savedBoardFile.delete();
            }

        }

    }
}