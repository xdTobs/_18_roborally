package eighteen.view;/*
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

import eighteen.controller.WebAppController;
import eighteen.observer.Subject;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class BoardView extends VBox implements ViewObserver {

//    private Board board;

    private GridPane mainBoardPane;
    private SpaceView[][] spaces;

    private PlayersView playersView;

    private Label statusLabel;

    private SpaceEventHandler spaceEventHandler;

    //    public BoardView(@NotNull GameController gameController) {
    public BoardView(WebAppController webAppController) {
//        board = gameController.board;
//
//        mainBoardPane = new GridPane();
//        playersView = new dk.dtu.compute.se.pisd.roborally.view.PlayersView(gameController);
//        statusLabel = new Label("<no status>");
//
        this.getChildren().add(mainBoardPane);
//        this.getChildren().add(playersView);
//        this.getChildren().add(statusLabel);
//
//        spaces = new dk.dtu.compute.se.pisd.roborally.view.SpaceView[board.width][board.height];
//
//        spaceEventHandler = new SpaceEventHandler(gameController);
//
//        for (int x = 0; x < board.width; x++) {
//            for (int y = 0; y < board.height; y++) {
//                Space space = board.getSpace(x, y);
//                dk.dtu.compute.se.pisd.roborally.view.SpaceView spaceView = new dk.dtu.compute.se.pisd.roborally.view.SpaceView(space);
//                spaces[x][y] = spaceView;
//                mainBoardPane.add(spaceView, x, y);
//                spaceView.setOnMouseClicked(spaceEventHandler);
//            }
//        }
//
//        board.attach(this);
//        update(board);
    }

    @Override
    public void updateView(Subject subject) {
//        if (subject == board) {
//            Phase phase = board.getPhase();
//            statusLabel.setText(board.getStatusMessage());
//        }
    }

    // XXX this handler and its uses should eventually be deleted! This is just to help test the
    //     behaviour of the game by being able to explicitly move the players on the board!
    private class SpaceEventHandler implements EventHandler<MouseEvent> {

//        final public GameController gameController;

        //        public SpaceEventHandler(@NotNull GameController gameController) {
        public SpaceEventHandler() {
//            this.gameController = gameController;
        }

        @Override
        public void handle(MouseEvent event) {
//            public void handle(MouseEvent event) {
//            Object source = event.getSource();
//            if (source instanceof dk.dtu.compute.se.pisd.roborally.view.SpaceView) {
//                dk.dtu.compute.se.pisd.roborally.view.SpaceView spaceView = (dk.dtu.compute.se.pisd.roborally.view.SpaceView) source;
//                Space space = spaceView.space;
//                Board board = space.board;
//
//                if (board == gameController.board) {
//                    gameController.moveCurrentPlayerToSpace(space);
//                    event.consume();
//                }
//            }
//        }
            System.out.println("consuming event");
            event.consume();
        }

    }

}
