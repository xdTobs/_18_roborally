package dk.dtu.eighteen.roborally.fileaccess;


import org.junit.Test;

import java.io.IOException;

/**
 * This tests the all the methods from the LoadBoard class.
 *
 * @Author Frederik Rolsted, s224299@dtu.dk
 */
public class LoadBoardTest {

    @Test
    public void savingAndLoadingGame() throws IOException {
//        LoadBoard.saveFolder = new File("/home/henrik/Documents/t2/advanced/project/roborally/savedGames");
//        Board board = LoadBoard.loadNewGameFromFile("src/test/resources/playableBoards/DIZZY_HIGHWAY.json");
//        board.createAddPlayerToEmptySpace("PURPLE", "player1");
//
//        try {
//            LoadBoard.saveBoard(board, "savedTestBoard.json");
//            Board board2 = LoadBoard.loadSavedGameFromFile("savedTestBoard.json");
//            Player playerFromOrigBoard = board.getPlayer(0);
//            Player playerFromSavedBoard = board2.getPlayer(0);
//
//            // Assert relevant attributes of the boards
//            Assertions.assertEquals(board.height, board2.height);
//            Assertions.assertEquals(board.width, board2.width);
//            Assertions.assertEquals(board.getPhase(), board2.getPhase());
//            Assertions.assertEquals(board.getStep(), board2.getStep());
//
//            // Check if spacetypes are the same on both boards with a wall
//            Assertions.assertEquals(board.getSpace(1, 2).getWalls(), board2.getSpace(1, 2).getWalls());
//
//            // Assert relevant attributes of the players
//            Assertions.assertEquals(playerFromOrigBoard.getName(), playerFromSavedBoard.getName());
//            Assertions.assertEquals(playerFromOrigBoard.getColor(), playerFromSavedBoard.getColor());
//            Assertions.assertEquals(playerFromOrigBoard.getSpace().x, playerFromSavedBoard.getSpace().x);
//            Assertions.assertEquals(playerFromOrigBoard.getSpace().y, playerFromSavedBoard.getSpace().y);
//        } finally {
//            File savedBoardFile = new File(LoadBoard.saveFolder, "savedTestBoard.json");
//            if (savedBoardFile.exists()) {
//                savedBoardFile.delete();
//            }
//
//        }
//
    }
}