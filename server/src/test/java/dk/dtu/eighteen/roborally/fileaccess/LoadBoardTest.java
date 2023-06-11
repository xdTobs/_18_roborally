package dk.dtu.eighteen.roborally.fileaccess;

import dk.dtu.eighteen.roborally.model.Board;
import dk.dtu.eighteen.roborally.model.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;


class LoadBoardTest {


    @Test
    void loadBoardFromJSONString() {
        String json = """
                {
                   "board":{
                      "width":3,
                      "height":3,
                      "spaces":[
                         [
                            {
                               "walls":[
                                 \s
                               ],
                               "actions":[
                                 \s
                               ],
                               "x":0,
                               "y":0
                            },
                            {
                               "walls":[
                                 \s
                               ],
                               "actions":[
                                 \s
                               ],
                               "x":0,
                               "y":1
                            },
                            {
                               "walls":[
                                 \s
                               ],
                               "actions":[
                                 \s
                               ],
                               "x":0,
                               "y":2
                            }
                         ],
                         [
                            {
                               "walls":[
                                 \s
                               ],
                               "actions":[
                                 \s
                               ],
                               "x":1,
                               "y":0
                            },
                            {
                               "walls":[
                                 \s
                               ],
                               "actions":[
                                 \s
                               ],
                               "x":1,
                               "y":1
                            },
                            {
                               "walls":[
                                  "NORTH"
                               ],
                               "actions":[
                                 \s
                               ],
                               "x":1,
                               "y":2
                            }
                         ],
                         [
                            {
                               "walls":[
                                  "EAST"
                               ],
                               "actions":[
                                 \s
                               ],
                               "x":2,
                               "y":0
                            },
                            {
                               "walls":[
                                 \s
                               ],
                               "actions":[
                                  {
                                     "CLASSNAME":"dk.dtu.eighteen.roborally.controller.spaces.ConveyorBelt",
                                     "INSTANCE":{
                                        "heading":"EAST"
                                     }
                                  }
                               ],
                               "x":2,
                               "y":1
                            },
                            {
                               "walls":[
                                 \s
                               ],
                               "actions":[
                                 \s
                               ],
                               "x":2,
                               "y":2
                            }
                         ]
                      ],
                      "players":[
                         {
                            "currentMoves":{
                               "cardIndex":[
                                  0,
                                  0,
                                  0,
                                  0,
                                  0
                               ]
                            },
                            "x":0,
                            "y":0,
                            "name":"henrik",
                            "checkpointCounter":0,
                            "heading":"SOUTH",
                            "availableCardSlots":[
                               {
                                  "visible":true
                               },
                               {
                                  "visible":true
                               },
                               {
                                  "visible":true
                               },
                               {
                                  "visible":true
                               },
                               {
                                  "visible":true
                               },
                               {
                                  "visible":true
                               },
                               {
                                  "visible":true
                               },
                               {
                                  "visible":true
                               }
                            ]
                         }
                      ]
                   },
                   "status":"INIT_NEW_GAME"
                }
                """;
//        try {
//            var b = LoadBoard.loadBoardFromJSONString(json);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        BoardTemplate boardTemplate = BoardTemplate.createBoardTemplate(jsonObject);
//        assertEquals(3, boardTemplate.width);
//        assertEquals(3, boardTemplate.height);

    }


    @Test
    /**
     * This tests the loadNewGameFromFile(), saveBoard(), and loadSavedGameFromFile() methods from
     * the LoadBoard class.
     * @Author Frederik Rolsted, s224299@dtu.dk
     */
    void savingAndLoadingGame() throws IOException {
        Board board = LoadBoard.loadNewGameFromFile("DIZZY_HIGHWAY.json");
        board.createAddPlayerToEmptySpace("PURPLE", "player1");

        try {
            LoadBoard.saveBoard(board, "savedTestBoard.json");
            Board board2 = LoadBoard.loadSavedGameFromFile("savedTestBoard.json");
            Player playerFromOrigBoard = board.getPlayer(0);
            Player playerFromSavedBoard = board2.getPlayer(0);

            // Assert relevant attributes of the boards
            Assertions.assertEquals(board.height, board2.height);
            Assertions.assertEquals(board.width, board2.width);
            Assertions.assertEquals(board.getPhase(), board2.getPhase());
            Assertions.assertEquals(board.getStep(), board2.getStep());
            Assertions.assertEquals(board.isStepMode(), board2.isStepMode());

            // Check if spacetype is the same on both boards with a wall
            Assertions.assertEquals(board.getSpace(1, 2).getWalls(), board2.getSpace(1, 2).getWalls());

            // Assert relevant attributes of the players
            Assertions.assertEquals(playerFromOrigBoard.getName(), playerFromSavedBoard.getName());
            Assertions.assertEquals(playerFromOrigBoard.getColor(), playerFromSavedBoard.getColor());
            Assertions.assertEquals(playerFromOrigBoard.getSpace().x, playerFromSavedBoard.getSpace().x);
            Assertions.assertEquals(playerFromOrigBoard.getSpace().y, playerFromSavedBoard.getSpace().y);
        } finally {
            File savedBoardFile = new File("src/main/resources/savedBoards/savedTestBoard.json");
            if (savedBoardFile.exists()) {
                savedBoardFile.delete();
            }

        }

    }
}