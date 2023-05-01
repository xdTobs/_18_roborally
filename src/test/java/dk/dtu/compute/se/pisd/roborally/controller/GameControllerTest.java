package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GameControllerTest {

    private final int TEST_WIDTH = 8;
    private final int TEST_HEIGHT = 8;

    private GameController gameController;

    @BeforeEach
    void setUp() {
        Board board = new Board(TEST_WIDTH, TEST_HEIGHT, "defaultTestBoard");
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
    void moveForward() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.moveForward(current);

        assertEquals(current, board.getSpace(0, 1).getPlayer(), "Player " + current.getName() + " should beSpace (0,1)!");
        assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
    }

    @Test
    void currentSpaceHasBlockinWallTest() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        Space space = board.getSpace(0, 0);
        space.setWalls(Heading.SOUTH);

        gameController.moveForward(current);

        assertEquals(current, board.getSpace(0, 0).getPlayer(), "Player " + current.getName() + " should beSpace (0,0)!");
        assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        assertNull(board.getSpace(0, 1).getPlayer(), "Space (0,1) should be empty!");
    }

    @Test
    void nextSpaceHasBlockinWallTest() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        Space space = board.getSpace(0, 1);
        space.setWalls(Heading.NORTH);

        gameController.moveForward(current);

        assertEquals(current, board.getSpace(0, 0).getPlayer(), "Player " + current.getName() + " should beSpace (0,0)!");
        assertNull(board.getSpace(0, 1).getPlayer(), "Space (0,1) should be empty!");
    }

    @Test
    void blockingWallFastFwdTest() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        Space space = board.getSpace(0, 1);
        space.setWalls(Heading.SOUTH);

        gameController.fastForward(current);

        assertEquals(current, board.getSpace(0, 1).getPlayer(), "Player " + current.getName() + " should beSpace (0,1)!");
        assertNull(board.getSpace(0, 2).getPlayer(), "Space (0,2) should be empty!");
    }
}