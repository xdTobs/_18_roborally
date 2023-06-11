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
import dk.dtu.eighteen.roborally.designpatterns.observer.Subject;
import dk.dtu.eighteen.roborally.model.Board;
import dk.dtu.eighteen.roborally.model.CommandCardField;
import dk.dtu.eighteen.roborally.model.Player;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class PlayerView extends Pane implements ViewObserver {

    private final WebAppController webAppController;
    private Player player;

    private VBox top;

    private Label programLabel;
    private GridPane programPane;
    private Label cardsLabel;
    private GridPane cardsPane;

    private CardFieldView[] programCardViews;
    private CardFieldView[] cardViews;

    private VBox buttonPanel;

    private Button finishButton;

    private VBox playerInteractionPanel;

//    private GameController gameController;

    public PlayerView(@NotNull WebAppController webAppController, Board board) {
        super();
        Player player = board.getPlayer(webAppController.playerName);

        this.setStyle("-fx-text-base-color: " + player.getColor() + ";");

        top = new VBox();
        this.getChildren().add(top);

        this.webAppController = webAppController;
        this.player = player;

        programLabel = new Label("Program");

        programPane = new GridPane();
        programPane.setVgap(2.0);
        programPane.setHgap(2.0);
        programCardViews = new CardFieldView[Player.NO_REGISTER_CARDS];
        for (int i = 0; i < Player.NO_REGISTER_CARDS; i++) {
            CommandCardField cardField = player.getRegisterCardField(i);
            if (cardField != null) {
                programCardViews[i] = new CardFieldView(webAppController, board, cardField);
                programPane.add(programCardViews[i], i, 0);
            }
        }

        // XXX  the following buttons should actually not be on the tabs of the individual
        //      players, but on the PlayersView (view for all players). This should be
        //      refactored.

        finishButton = new Button("Finish Programming");
        finishButton.setOnAction(e -> {
            List<String> moveNames = new ArrayList<>();
            for (CardFieldView programCardView : this.programCardViews) {
                try {
                    String s = programCardView.getCommandCardField().getCard().command.toString();
                    moveNames.add(s);
                } catch (NullPointerException exeption) {
                    moveNames.add("empty");
                }
            }
            webAppController.finishProgrammingPhase(moveNames);
        });

        buttonPanel = new VBox(finishButton);
        buttonPanel.setAlignment(Pos.CENTER_LEFT);
        buttonPanel.setSpacing(3.0);
        programPane.add(buttonPanel, Player.NO_REGISTER_CARDS, 0);

        playerInteractionPanel = new VBox();
        playerInteractionPanel.setAlignment(Pos.CENTER_LEFT);
        playerInteractionPanel.setSpacing(3.0);

        cardsLabel = new Label("Command Cards");
        cardsPane = new GridPane();
        cardsPane.setVgap(2.0);
        cardsPane.setHgap(2.0);
        cardViews = new CardFieldView[Player.NO_PLAYABLE_CARDS];
        for (int i = 0; i < Player.NO_PLAYABLE_CARDS; i++) {
            CommandCardField cardField = player.getPlayableCard(i);
            if (cardField != null) {
                cardViews[i] = new CardFieldView(webAppController, board, cardField);
                cardsPane.add(cardViews[i], i, 0);
            }
        }

        top.getChildren().add(programLabel);
        top.getChildren().add(programPane);
        top.getChildren().add(cardsLabel);
        top.getChildren().add(cardsPane);

        if (board != null) {
            board.attach(this);
            update(board);
        }
    }

    @Override
    public void updateView(Subject subject) {
//        if (subject == gameController.board) {
//            for (int i = 0; i < Player.NO_REGISTERS; i++) {
//                CardFieldView cardFieldView = programCardViews[i];
//                if (cardFieldView != null) {
//                    if (gameController.board.getPhase() == Phase.PROGRAMMING) {
//                        cardFieldView.setBackground(CardFieldView.BG_DEFAULT);
//                    } else {
//                        if (i < gameController.board.getStep()) {
//                            cardFieldView.setBackground(CardFieldView.BG_DONE);
//                        } else if (i == gameController.board.getStep()) {
//                            if (gameController.board.getCurrentPlayer() == player) {
//                                cardFieldView.setBackground(CardFieldView.BG_ACTIVE);
//                            } else if (gameController.board.getPlayerNumber(gameController.board.getCurrentPlayer()) > gameController.board.getPlayerNumber(player)) {
//                                cardFieldView.setBackground(CardFieldView.BG_DONE);
//                            } else {
//                                cardFieldView.setBackground(CardFieldView.BG_DEFAULT);
//                            }
//                        } else {
//                            cardFieldView.setBackground(CardFieldView.BG_DEFAULT);
//                        }
//                    }
//                }
//            }
//
//            if (gameController.board.getPhase() != Phase.PLAYER_INTERACTION) {
//                if (!programPane.getChildren().contains(buttonPanel)) {
//                    programPane.getChildren().remove(playerInteractionPanel);
//                    programPane.add(buttonPanel, Player.NO_REGISTERS, 0);
//                }
//                switch (gameController.board.getPhase()) {
//                    case INITIALISATION:
//                        finishButton.setDisable(true);
//                        // XXX just to make sure that there is a way for the player to get
//                        //     from the initialization phase to the programming phase somehow!
//                        executeButton.setDisable(false);
//                        stepButton.setDisable(true);
//                        break;
//
//                    case PROGRAMMING:
//                        finishButton.setDisable(false);
//                        executeButton.setDisable(true);
//                        stepButton.setDisable(true);
//                        break;
//
//                    case ACTIVATION:
//                        finishButton.setDisable(true);
//                        executeButton.setDisable(false);
//                        stepButton.setDisable(false);
//                        break;
//
//                    default:
//                        finishButton.setDisable(true);
//                        executeButton.setDisable(true);
//                        stepButton.setDisable(true);
//                }
//
//
//            } else {
//                if (!programPane.getChildren().contains(playerInteractionPanel)) {
//                    programPane.getChildren().remove(buttonPanel);
//                    programPane.add(playerInteractionPanel, Player.NO_REGISTERS, 0);
//                }
//                playerInteractionPanel.getChildren().clear();
//
//                if (gameController.board.getCurrentPlayer() == player) {
//                    // TODO Assignment V3: these buttons should be shown only when there is
//                    //      an interactive command card, and the buttons should represent
//                    //      the player's choices of the interactive command card. The
//                    //      following is just a mockup showing two options
//                    int step = gameController.board.getStep();
////                    CommandCard card = player.getRegisterSlot(step).getCard();
////                    List<Command> options = card.command.getOptions();
//
////                    for (Command option : options) {
////                        Button optionButton = new Button(option.displayName);
////                        optionButton.setOnAction(e -> gameController.executeCommandOptionAndContinue(option));
////                        optionButton.setDisable(false);
////                        playerInteractionPanel.getChildren().add(optionButton);
////                    }
//
//                }
//            }
//        }
    }

}
