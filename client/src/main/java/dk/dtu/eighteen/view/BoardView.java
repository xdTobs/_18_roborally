/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.eighteen.view;

import dk.dtu.eighteen.controller.WebAppController;
import dk.dtu.eighteen.roborally.controller.Actions.Checkpoint;
import dk.dtu.eighteen.roborally.controller.Actions.IFieldAction;
import dk.dtu.eighteen.roborally.controller.spaces.ConveyorBelt;
import dk.dtu.eighteen.roborally.controller.spaces.FastConveyorBelt;
import dk.dtu.eighteen.roborally.controller.spaces.RotateLeft;
import dk.dtu.eighteen.roborally.controller.spaces.RotateRight;
import dk.dtu.eighteen.roborally.designpatterns.observer.Subject;
import dk.dtu.eighteen.roborally.model.Board;
import dk.dtu.eighteen.roborally.model.Space;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class BoardView extends VBox implements ViewObserver {

    private final WebAppController webAppController;
    private Board board;

    private GridPane mainBoardPane;
    private SpaceView[][] spaces;

    private PlayerView playerView;

    private Label statusLabel;

    /**
     * Constructor to build the boardview from a Board, by iterating over spaces in the board
     * and checking which action is associated with the given space.
     * @param webAppController
     * @param board
     * @Author Frederik Rolsted, s224299@dtu.dk
     */
    public BoardView(WebAppController webAppController, @NotNull Board board) {
        this.webAppController = webAppController;
        this.board = board;

        mainBoardPane = new GridPane();
        playerView = new PlayerView(webAppController, board);
        statusLabel = new Label("<no status>");

        this.getChildren().add(mainBoardPane);
        this.getChildren().add(playerView);
        this.getChildren().add(statusLabel);

        spaces = new SpaceView[board.width][board.height];

        for (int x = 0; x < board.width; x++) {
            for (int y = 0; y < board.height; y++) {
                Space space = board.getSpace(x, y);
                List<IFieldAction> actions = space.getActions();
                SpaceView spaceView = new SpaceView(space);
                for (IFieldAction action : actions) {
                    if (action instanceof ConveyorBelt conveyorBelt) {
                        spaceView = new ConveyorBeltView(conveyorBelt, space);
                    }
                    if (action instanceof FastConveyorBelt fastconveyorBelt) {
                        spaceView = new FastConveyorBeltView(fastconveyorBelt, space);
                    }
                    if (action instanceof RotateLeft rotateleft) {
                        spaceView = new RotateLeftView(rotateleft, space);
                    }
                    if (action instanceof RotateRight rotateright) {
                        spaceView = new RotateRightView(rotateright, space);
                    }
                    if (action instanceof Checkpoint checkpoint) {
                        spaceView = new CheckpointView(space, checkpoint.getCheckpointNumber());
                    }
                }
                spaces[x][y] = spaceView;
                mainBoardPane.add(spaceView, x, y);

            }
        }

        board.attach(this);
        update(board);

    }

    @Override
    public void updateView(Subject subject) {
        if (subject == board) {
            statusLabel.setText(board.getStatusMessage(webAppController.playerName));
        }
    }

}
