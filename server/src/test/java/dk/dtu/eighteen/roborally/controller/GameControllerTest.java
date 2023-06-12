package dk.dtu.eighteen.roborally.controller;

import dk.dtu.eighteen.roborally.fileaccess.LoadBoard;
import dk.dtu.eighteen.roborally.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.io.IOException;

import static dk.dtu.eighteen.roborally.model.Phase.ACTIVATION;
import static dk.dtu.eighteen.roborally.model.Phase.PROGRAMMING;
import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {
    private GameController gameController;

    @BeforeEach
    void setUp() throws IOException {
        Board board = LoadBoard.loadNewGameFromFile("a-test-board.json");
        gameController = new GameController(board);
        for (int i = 0; i < 6; i++) {
            Player player = new Player(board, null, "Player " + i);
            board.addPlayer(player);
            player.setSpace(board.getSpace(i, i));
            player.setHeading(Heading.values()[i % Heading.values().length]);
        }
        board.setCurrentPlayer(board.getPlayer(0));
    }

    @AfterEach
    void tearDown() {
        gameController = null;
    }

    @Test
    void moveCurrentPlayerToSpace() {
        Board board = gameController.board;
        Player player1 = board.getPlayer(0);
        Player player2 = board.getPlayer(1);

        gameController.moveCurrentPlayerToSpace(board.getSpace(0, 4));

        assertEquals(player1, board.getSpace(0, 4).getPlayer(), "Player " + player1.getName() + " should beSpace (0,4)!");
        assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
        assertEquals(player2, board.getCurrentPlayer(), "Current player should be " + player2.getName() + "!");
    }
    @Test
    void testStartProgrammingPhase() {
        Board board = gameController.board;
        GameController gamecontroller = new GameController(board);
        board.setPhase(ACTIVATION);
        gamecontroller.startProgrammingPhase();
        assertEquals(Phase.PROGRAMMING, board.getPhase());
    }

    @Test
    void loadProgrammingPhase() {
        Board board = gameController.board;
        GameController gamecontroller = new GameController(board);
        board.setPhase(ACTIVATION);
        gamecontroller.startProgrammingPhase();
        assertEquals(Phase.PROGRAMMING, board.getPhase());
        assertEquals(0, board.getStep());
    }
    @Test
    void generateRandomCommandCardTest() {
        Board board = gameController.board;
        GameController gamecontroller = new GameController(board);
        CommandCard card = gamecontroller.generateRandomCommandCard();
        assertNotEquals(null, card);
    }

    @Test
    void finishProgrammingPhase(){
        Board board = gameController.board;
        GameController gamecontroller = new GameController(board);
        board.setPhase(PROGRAMMING);
        gamecontroller.finishProgrammingPhase();
        assertEquals(Phase.ACTIVATION, board.getPhase());
        assertEquals("Player 0", board.getCurrentPlayer().getName());
    }
    @Test
    void moveForward() {
        Board board = gameController.board;
        GameController gamecontroller = new GameController(board);
        Player player = board.getCurrentPlayer();
        player.setHeading(Heading.SOUTH);
        assertEquals(0,player.x);
        assertEquals(0,player.y);
        gamecontroller.moveForward(player);
        assertEquals(0, player.x);
        assertEquals(1, player.y);
    }
    @Test
    void moveForward_2() {
        Board board = gameController.board;
        GameController gamecontroller = new GameController(board);
        Player player = board.getCurrentPlayer();
        player.setHeading(Heading.SOUTH);
        assertEquals(0,player.x);
        assertEquals(0,player.y);
        gamecontroller.moveForward_2(player);
        assertEquals(0, player.x);
        assertEquals(2, player.y);
    }
    @Test
    void moveForward_3() {
        Board board = gameController.board;
        GameController gamecontroller = new GameController(board);
        Player player = board.getCurrentPlayer();
        player.setHeading(Heading.SOUTH);
        assertEquals(0,player.x);
        assertEquals(0,player.y);
        gamecontroller.moveForward_3(player);
        assertEquals(0, player.x);
        assertEquals(3, player.y);
    }
    @Test
    void moveBackwards() {
        Board board = gameController.board;
        GameController gamecontroller = new GameController(board);
        Player player = board.getCurrentPlayer();
        player.setHeading(Heading.NORTH);
        assertEquals(0,player.x);
        assertEquals(0,player.y);
        gamecontroller.moveBackwards(player);
        assertEquals(0, player.x);
        assertEquals(1, player.y);
    }
    @Test
    void turnLeft() {
        Board board = gameController.board;
        GameController gamecontroller = new GameController(board);
        Player player = board.getCurrentPlayer();
        player.setHeading(Heading.NORTH);
        assertEquals(Heading.NORTH, player.getHeading());
        gamecontroller.turnLeft(player);
        assertEquals(Heading.WEST, player.getHeading());
    }
    @Test
    void turnRight() {
        Board board = gameController.board;
        GameController gamecontroller = new GameController(board);
        Player player = board.getCurrentPlayer();
        player.setHeading(Heading.NORTH);
        assertEquals(Heading.NORTH, player.getHeading());
        gamecontroller.turnRight(player);
        assertEquals(Heading.EAST, player.getHeading());
    }
    @Test
    void uTurn() {
        Board board = gameController.board;
        GameController gamecontroller = new GameController(board);
        Player player = board.getCurrentPlayer();

        player.setHeading(Heading.NORTH);
        assertEquals(Heading.NORTH, player.getHeading());
        gamecontroller.uTurn(player);
        assertEquals(Heading.SOUTH, player.getHeading());
    }

    @Test
    void push() {
        Board board = gameController.board;
        GameController gamecontroller = new GameController(board);
        Player player1 = board.getPlayer(0);
        Player player2 = board.getPlayer(1);
        player1.setHeading(Heading.SOUTH);
        assertEquals(0,player1.x);
        assertEquals(0,player1.y);
        player2.setHeading(Heading.WEST);
        gamecontroller.moveForward(player2);
        assertEquals(0,player2.x);
        assertEquals(1,player2.y);
        gamecontroller.moveForward(player1);
        assertEquals(0,player1.x);
        assertEquals(1,player1.y);
        assertEquals(0,player2.x);
        assertEquals(2,player2.y);
    }

    @Test
    void generateRandomCommandCard(){
        Board board = gameController.board;
        GameController gamecontroller = new GameController(board);
        Player player = board.getPlayer(0);
        CommandCardField field = player.getPlayableCard(0);
        assertEquals("player={Player{, x=0, y=0, name='Player 0'}card=null, visible=true}",String.valueOf(player.getPlayableCard(0)));
        field.setCard(gamecontroller.generateRandomCommandCard());
        assertNotEquals("player={Player{, x=0, y=0, name='Player 0'}card=null, visible=true}",String.valueOf(player.getPlayableCard(0)));
    }






//
//    @Test
//    void moveForward() {
//        Board board = gameController.board;
//        Player current = board.getCurrentPlayer();
//
//        gameController.moveForward(current);
//
//        assertEquals(current, board.getSpace(0, 1).getPlayer(), "Player " + current.getName() + " should beSpace (0,1)!");
//        assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
//        assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
//    }
//
//    @Test
//    void currentSpaceHasBlockinWallTest() {
//        Board board = gameController.board;
//        Player current = board.getCurrentPlayer();
//
//        Space space = board.getSpace(0, 0);
//        space.addWalls(Heading.SOUTH);
//
//        gameController.moveForward(current);
//
//        assertEquals(current, board.getSpace(0, 0).getPlayer(), "Player " + current.getName() + " should beSpace (0,0)!");
//        assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
//        assertNull(board.getSpace(0, 1).getPlayer(), "Space (0,1) should be empty!");
//    }
//
//    @Test
//    void nextSpaceHasBlockinWallTest() {
//        Board board = gameController.board;
//        Player current = board.getCurrentPlayer();
//
//        Space space = board.getSpace(0, 1);
//        space.addWalls(Heading.NORTH);
//
//        gameController.moveForward(current);
//
//        assertEquals(current, board.getSpace(0, 0).getPlayer(), "Player " + current.getName() + " should beSpace (0,0)!");
//        assertNull(board.getSpace(0, 1).getPlayer(), "Space (0,1) should be empty!");
//    }
//
//    @Test
//    void blockingWallFastFwdTest() {
//        Board board = gameController.board;
//        Player current = board.getCurrentPlayer();
//
//        Space space = board.getSpace(0, 1);
//        space.addWalls(Heading.SOUTH);
//
////        gameController.fastForward(current);
//
//        assertEquals(current, board.getSpace(0, 1).getPlayer(), "Player " + current.getName() + " should beSpace (0,1)!");
//        assertNull(board.getSpace(0, 2).getPlayer(), "Space (0,2) should be empty!");
//    }
}