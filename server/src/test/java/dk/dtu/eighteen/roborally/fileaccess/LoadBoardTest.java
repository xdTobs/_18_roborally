package dk.dtu.eighteen.roborally.fileaccess;

import org.junit.jupiter.api.Test;

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
}