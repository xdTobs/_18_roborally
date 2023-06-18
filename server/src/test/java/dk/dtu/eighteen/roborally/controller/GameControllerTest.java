package dk.dtu.eighteen.roborally.controller;

import dk.dtu.eighteen.roborally.fileaccess.LoadBoard;
import dk.dtu.eighteen.roborally.model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static dk.dtu.eighteen.roborally.model.Phase.ACTIVATION;
import static dk.dtu.eighteen.roborally.model.Phase.PROGRAMMING;
import static org.junit.Assert.*;


/**
 * Testclass testing the logic of the GameController
 *
 * @author Frederik Rolsted, s224299@dtu.dk
 * @author Jakob Hansen, s224312@dtu.dk
 */
public class GameControllerTest {
    private GameController gameController;

    @Before
    public void setUp() throws IOException {
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

    @After
    public void tearDown() {
        gameController = null;
    }

    @Test
    public void moveCurrentPlayerToSpace() {
        Board board = gameController.board;
        Player player1 = board.getPlayer(0);
        Player player2 = board.getPlayer(1);

        gameController.moveCurrentPlayerToSpace(board.getSpace(0, 4));

        assertEquals(player1, board.getSpace(0, 4).getPlayer());
        assertNull(board.getSpace(0, 0).getPlayer());
        assertEquals(player2, board.getCurrentPlayer());
    }

    @Test
    public void testStartProgrammingPhase() {
        Board board = gameController.board;
        GameController gamecontroller = new GameController(board);
        board.setPhase(ACTIVATION);
        gamecontroller.startProgrammingPhase();
        assertEquals(Phase.PROGRAMMING, board.getPhase());
    }

    @Test
    public void loadProgrammingPhase() {
        Board board = gameController.board;
        GameController gamecontroller = new GameController(board);
        board.setPhase(ACTIVATION);
        gamecontroller.startProgrammingPhase();
        assertEquals(Phase.PROGRAMMING, board.getPhase());
        assertEquals(0, board.getStep());
    }

    @Test
    public void generateRandomCommandCardTest() {
        Board board = gameController.board;
        GameController gamecontroller = new GameController(board);
        CommandCard card = gamecontroller.generateRandomCommandCard();
        assertNotEquals(null, card);
    }

    @Test
    public void finishProgrammingPhase() {
        Board board = gameController.board;
        GameController gamecontroller = new GameController(board);
        board.setPhase(PROGRAMMING);
        gamecontroller.finishProgrammingPhase();
        assertEquals(Phase.ACTIVATION, board.getPhase());
        assertEquals("Player 0", board.getCurrentPlayer().getName());
    }

    @Test
    public void moveForward() {
        Board board = gameController.board;
        GameController gamecontroller = new GameController(board);
        Player player = board.getCurrentPlayer();
        player.setHeading(Heading.SOUTH);
        assertEquals(0, player.x);
        assertEquals(0, player.y);
        gamecontroller.moveForward(player);
        assertEquals(0, player.x);
        assertEquals(1, player.y);
    }

    @Test
    public void moveForward_2() {
        Board board = gameController.board;
        GameController gamecontroller = new GameController(board);
        Player player = board.getCurrentPlayer();
        player.setHeading(Heading.SOUTH);
        assertEquals(0, player.x);
        assertEquals(0, player.y);
        gamecontroller.moveForward_2(player);
        assertEquals(0, player.x);
        assertEquals(2, player.y);
    }

    @Test
    public void moveForward_3() {
        Board board = gameController.board;
        GameController gamecontroller = new GameController(board);
        Player player = board.getCurrentPlayer();
        player.setHeading(Heading.SOUTH);
        assertEquals(0, player.x);
        assertEquals(0, player.y);
        gamecontroller.moveForward_3(player);
        assertEquals(0, player.x);
        assertEquals(3, player.y);
    }

    @Test
    public void moveBackwards() {
        Board board = gameController.board;
        GameController gamecontroller = new GameController(board);
        Player player = board.getCurrentPlayer();
        player.setHeading(Heading.NORTH);
        assertEquals(0, player.x);
        assertEquals(0, player.y);
        gamecontroller.moveBackwards(player);
        assertEquals(0, player.x);
        assertEquals(1, player.y);
    }

    @Test
    public void turnLeft() {
        Board board = gameController.board;
        GameController gamecontroller = new GameController(board);
        Player player = board.getCurrentPlayer();
        player.setHeading(Heading.NORTH);
        assertEquals(Heading.NORTH, player.getHeading());
        gamecontroller.turnLeft(player);
        assertEquals(Heading.WEST, player.getHeading());
    }

    @Test
    public void turnRight() {
        Board board = gameController.board;
        GameController gamecontroller = new GameController(board);
        Player player = board.getCurrentPlayer();
        player.setHeading(Heading.NORTH);
        assertEquals(Heading.NORTH, player.getHeading());
        gamecontroller.turnRight(player);
        assertEquals(Heading.EAST, player.getHeading());
    }

    @Test
    public void uTurn() {
        Board board = gameController.board;
        GameController gamecontroller = new GameController(board);
        Player player = board.getCurrentPlayer();

        player.setHeading(Heading.NORTH);
        assertEquals(Heading.NORTH, player.getHeading());
        gamecontroller.uTurn(player);
        assertEquals(Heading.SOUTH, player.getHeading());
    }

    @Test
    public void push() {
        Board board = gameController.board;
        GameController gamecontroller = new GameController(board);
        Player player1 = board.getPlayer(0);
        Player player2 = board.getPlayer(1);
        player1.setHeading(Heading.SOUTH);
        assertEquals(0, player1.x);
        assertEquals(0, player1.y);
        player2.setHeading(Heading.WEST);
        gamecontroller.moveForward(player2);
        assertEquals(0, player2.x);
        assertEquals(1, player2.y);
        gamecontroller.moveForward(player1);
        assertEquals(0, player1.x);
        assertEquals(1, player1.y);
        assertEquals(0, player2.x);
        assertEquals(2, player2.y);
    }

    @Test
    public void generateRandomCommandCard() {
        Board board = gameController.board;
        GameController gamecontroller = new GameController(board);
        Player player = board.getPlayer(0);
        CommandCardField field = player.getPlayableCardField(0);
        assertEquals("player={Player{, x=0, y=0, name='Player 0'}card=null, visible=true}", String.valueOf(player.getPlayableCardField(0)));
        field.setCard(gamecontroller.generateRandomCommandCard());
        assertNotEquals("player={Player{, x=0, y=0, name='Player 0'}card=null, visible=true}", String.valueOf(player.getPlayableCardField(0)));
    }


    @Test
    public void currentSpaceHasBlockingWallTest() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        current.setHeading(Heading.SOUTH);
        Space space = board.getSpace(0, 0);
        space.addWalls(Heading.SOUTH);

        gameController.moveForward(current);

        assertEquals(current, board.getSpace(0, 0).getPlayer());
        assertEquals(Heading.SOUTH, current.getHeading());
        assertNull(board.getSpace(0, 1).getPlayer());
    }

    @Test
    public void nextSpaceHasBlockinWallTest() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        Space space = board.getSpace(0, 1);
        space.addWalls(Heading.NORTH);

        gameController.moveForward(current);

        assertEquals(current, board.getSpace(0, 0).getPlayer());
        assertNull(board.getSpace(0, 1).getPlayer());
    }
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