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
public class PlayerView extends Pane {

    private final CardFieldView[] programCardViews;


    public PlayerView(@NotNull WebAppController webAppController, Board board) {
        super();
        Player player = board.getPlayer(webAppController.playerName);

        this.setStyle("-fx-text-base-color: " + player.getColor() + ";");

        VBox top = new VBox();
        this.getChildren().add(top);

        Label programLabel = new Label("Program");

        GridPane programPane = new GridPane();
        programPane.setVgap(2.0);
        programPane.setHgap(2.0);
        programCardViews = new CardFieldView[Player.NO_REGISTER_CARDS];
        for (int i = 0; i < Player.NO_REGISTER_CARDS; i++) {
            CommandCardField cardField = player.getRegisterCardField(i);
            if (cardField != null) {
                programCardViews[i] = new CardFieldView(board, cardField);
                programPane.add(programCardViews[i], i, 0);
            }
        }

        // XXX the following buttons should actually not be on the tabs of the individual
        //      players, but on the PlayersView (view for all players). This should be
        //      refactored.

        Button finishButton = new Button("Finish Programming");
        finishButton.setOnAction(e -> {
            List<String> moveNames = new ArrayList<>();
            for (CardFieldView programCardView : this.programCardViews) {
                try {
                    String s = programCardView.getCommandCardField().getCard().command.toString();
                    moveNames.add(s);
                } catch (NullPointerException exception) {
                    moveNames.add("empty");
                }
            }
            webAppController.finishProgrammingPhase(moveNames);
        });

        VBox buttonPanel = new VBox(finishButton);
        buttonPanel.setAlignment(Pos.CENTER_LEFT);
        buttonPanel.setSpacing(3.0);
        programPane.add(buttonPanel, Player.NO_REGISTER_CARDS, 0);

        VBox playerInteractionPanel = new VBox();
        playerInteractionPanel.setAlignment(Pos.CENTER_LEFT);
        playerInteractionPanel.setSpacing(3.0);

        Label cardsLabel = new Label("Command Cards");
        GridPane cardsPane = new GridPane();
        cardsPane.setVgap(2.0);
        cardsPane.setHgap(2.0);
        CardFieldView[] cardViews = new CardFieldView[Player.NO_PLAYABLE_CARDS];
        for (int i = 0; i < Player.NO_PLAYABLE_CARDS; i++) {
            CommandCardField cardField = player.getPlayableCardField(i);
            if (cardField != null) {
                cardViews[i] = new CardFieldView(board, cardField);
                cardsPane.add(cardViews[i], i, 0);
            }
        }

        top.getChildren().add(programLabel);
        top.getChildren().add(programPane);
        top.getChildren().add(cardsLabel);
        top.getChildren().add(cardsPane);

    }
}
