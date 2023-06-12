package dk.dtu.eighteen.view;/*
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

import dk.dtu.eighteen.controller.WebAppController;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class RoboRallyMenuBar extends MenuBar {

    private WebAppController webAppController;

    private Menu controlMenu;

    private MenuItem saveGame;

    private MenuItem newGame;
    private MenuItem joinGame;

    private MenuItem loadGame;

    private MenuItem stopGame;

    private MenuItem exitApp;


    public RoboRallyMenuBar(WebAppController webAppController) {
        Button requestButton = new Button("Make Request");

        requestButton.setOnAction(event -> {
        });

        this.webAppController = webAppController;

        controlMenu = new Menu("File");
        this.getMenus().add(controlMenu);

//        ScheduledService<String> scheduledService = new ScheduledService<>() {
//            @Override
//            protected Task<String> createTask() {
//                return new Task<>() {
//                    @Override
//                    protected String call() throws Exception {
//
//                        HttpRequest request = HttpRequest.newBuilder()
//                                .uri(new URI("http://localhost:8080/board"))
//                                .header("roborally-player-name", webAppController.playerName)
//                                .GET()
//                                .build();
//                        HttpResponse<String> response = HttpClient.newBuilder()
//                                .build()
//                                .send(request, HttpResponse.BodyHandlers.ofString());
//                        return response.body().toString();
//                    }
//                };
//            }
//        };
//
//        scheduledService.setPeriod(Duration.seconds(1));
        newGame = new MenuItem("New Game");
        newGame.setOnAction(e -> {
            try {
                this.webAppController.newGame();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        controlMenu.getItems().add(newGame);

        joinGame = new MenuItem("Join Game");
        joinGame.setOnAction(e -> {
            try {
                this.webAppController.joinGame();
            } catch (NullPointerException ex) {
                throw new RuntimeException(ex);
            }
        });
        controlMenu.getItems().add(joinGame);

        stopGame = new MenuItem("Stop Game");
        stopGame.setOnAction(e -> this.webAppController.stopGame());
        controlMenu.getItems().add(stopGame);

        saveGame = new MenuItem("Save Game");
        saveGame.setOnAction(e -> this.webAppController.saveGame());
        controlMenu.getItems().add(saveGame);

        loadGame = new MenuItem("Load Game");
        loadGame.setOnAction(e -> this.webAppController.loadGame());
        controlMenu.getItems().add(loadGame);

        exitApp = new MenuItem("Exit");
        exitApp.setOnAction(e -> this.webAppController.exit());
        controlMenu.getItems().add(exitApp);

        controlMenu.setOnShowing(e -> update());
        controlMenu.setOnShown(e -> this.updateBounds());
        update();
    }


    public void update() {
        if (webAppController.isGameRunning()) {
            newGame.setVisible(false);
            stopGame.setVisible(true);
            saveGame.setVisible(true);
            loadGame.setVisible(false);
        } else {
            newGame.setVisible(true);
            stopGame.setVisible(false);
            saveGame.setVisible(false);
            loadGame.setVisible(true);
        }
    }

}
