package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoardTest {


    Board board;
    GameController gameController;

    @BeforeEach
    void setUp() {
        InputStream in = BoardTest.class.getResourceAsStream("/TestBoardCheckpoint.json");
        board = Board.createBoardFromInputStream(in).get();
        gameController = new GameController(board);

        Player p1 = new Player(null, "Player 1");
        board.addPlayer(p1);
        p1.setSpace(board.getSpace(0, 0));
        p1.setHeading(Heading.SOUTH);

        Player p2 = new Player(null, "Player 2");
        board.addPlayer(p2);
        p2.setSpace(board.getSpace(1, 0));
        p2.setHeading(Heading.SOUTH);
    }

    @Test
    void getCheckpointsTest() {
        int numberOfCheckpoints = board.getCheckpoints().size();
        assertEquals(2, numberOfCheckpoints);
    }

    @Test
    void winGameTest() {
        GameController gc = new GameController(board);
        Player p1 = board.getPlayer(0);
        Player p2 = board.getPlayer(1);
        CommandCard c1 = new CommandCard(Command.FORWARD);
        CommandCard c2 = new CommandCard(Command.FORWARD);
        p2.getProgramField(0).setCard(c1);
        p2.getProgramField(1).setCard(c2);
        gameController.finishProgrammingPhase();
        gc.executePrograms();
        assertTrue(board.getPhase() == Phase.GAMEOVER);
    }

    @Test
    void saveTest() {
        String s = Board.toJson(board);
        System.out.println(s);
        Board b = Board.fromJson(s);
        System.out.println("done");
    }
}