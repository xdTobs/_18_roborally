package dk.dtu.eighteen.roborally.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dk.dtu.eighteen.roborally.controller.GameController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoardTest {
//
//
//    Board board;
//    GameController gameController;
//
//    @BeforeEach
//    void setUp() {
////        board = Board.createBoardFromResource(getClass().getResource("/TestBoardCheckpoint.json").getFile());
////        gameController = new GameController(board);
////
////        Player p1 = new Player(board, null, "Player 1");
////        board.addPlayer(p1);
////        p1.setSpace(board.getSpace(0, 0));
////        p1.setHeading(Heading.SOUTH);
////
////        Player p2 = new Player(board, null, "Player 1");
////        board.addPlayer(p2);
////        p2.setSpace(board.getSpace(1, 0));
////        p2.setHeading(Heading.SOUTH);
//    }
//
//    @Test
//    void getCheckpointsTest() {
//        int numberOfCheckpoints = board.getCheckpoints().size();
//        assertEquals(2, numberOfCheckpoints);
//    }
//
//    @Test
//    void winGameTest() {
//        GameController gc = new GameController(board);
//        Player p1 = board.getPlayer(0);
//        Player p2 = board.getPlayer(1);
//        CommandCard c1 = new CommandCard(Command.MOVE_1);
//        CommandCard c2 = new CommandCard(Command.MOVE_1);
//        //Tobs killed this one, since getRegisterSlot no longer exists
//        //Instead set move to point to the correct card in AvailableCardSlot
//        /*p2.getRegisterSlot(0).setCard(c1);
//        p2.getRegisterSlot(1).setCard(c2);*/
//        gameController.finishProgrammingPhase();
//        gc.executePrograms();
//        assertTrue(board.getPhase() == Phase.GAMEOVER);
//    }
//
//    @Test
//    void serializeBoard() {
//        GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
//        Gson gson = gsonBuilder.create();
//        String jsonString = gson.toJson(board);
//        Board board2 = gson.fromJson(jsonString, Board.class);
////        assertEquals();
//    }
//
//    @Test
//    void serializeSpace() {
//        Space[][] spaces = board.getSpaces();
//        GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
//        Gson gson = gsonBuilder.create();
//        String jsonString = gson.toJson(spaces);
//        Space[][] spaces2 = gson.fromJson(jsonString, Space[][].class);
//        assertEquals(spaces[0][0].getWalls().size(), spaces2[0][0].getWalls().size());
//    }
//
//    @Test
//    void serializePlayer() {
//        Player player = board.getPlayer(0);
//        GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
//        Gson gson = gsonBuilder.create();
//        String jsonString = gson.toJson(player);
//        Player player2 = new Gson().fromJson(jsonString, Player.class);
//        assertEquals(player.getName(), player2.getName());
//    }
//
//    @Test
//    void boardFromJsonSaveGameState() {
//        InputStream in = BoardTest.class.getResourceAsStream("/saveGame.json");
//        InputStreamReader inputStreamReader = new InputStreamReader(in);
//        BufferedReader bufferedReader = null;
//        if (in != null) {
//            bufferedReader = new BufferedReader(inputStreamReader);
//        }
//        Board board = Board.fromJson(bufferedReader);
//        
//
////        Board board = Board.fromJson()
////        assertEquals(board.getCheckpoints().size(), 2);
//    }
}