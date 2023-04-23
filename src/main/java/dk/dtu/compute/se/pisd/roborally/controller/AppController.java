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
package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.designpatterns.observer.Observer;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.RoboRally;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.stage.FileChooser;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class AppController implements Observer {

    final private List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(2, 3, 4, 5, 6);
    final private List<String> PLAYER_COLORS = Arrays.asList("red", "green", "blue", "orange", "grey", "magenta");

    final private RoboRally roboRally;

    private GameController gameController;

    public AppController(@NotNull RoboRally roboRally) {
        this.roboRally = roboRally;
    }

    public void newGame(Board board, boolean isLoaded) {
        if (isLoaded) {
            gameController = new GameController(board);
            roboRally.createBoardView(gameController);
            switch (gameController.board.getPhase()) {

                case INITIALISATION -> {
                    // TODO Look up how to load if in this phase, or disable saving in these phases.
                    // I don't know if it is okay to save and load here, so I only make it possible in programming phase.

                }
                case PROGRAMMING -> {
                    // TODO make sure which part of the programming phase it is in is synced or only make saving possible at start of phase.
                    roboRally.createBoardView(gameController);
                }
                case ACTIVATION -> {
                    // TODO Look up how to load if in this phase, or disable saving in these phases.
                }
                case PLAYER_INTERACTION -> {
                    // TODO Look up how to load if in this phase, or disable saving in these phases.
                }
            }
        } else {
            ChoiceDialog<Integer> dialog = new ChoiceDialog<>(PLAYER_NUMBER_OPTIONS.get(0), PLAYER_NUMBER_OPTIONS);
            dialog.setTitle("Player number.");
            dialog.setHeaderText("Select number of players.");
            Optional<Integer> result = dialog.showAndWait();
            if (result.isPresent()) {
                if (gameController != null) {
                    // The UI should not allow this, but in case this happens anyway.
                    // give the user the option to save the game or abort this operation!
                    if (!stopGame()) {
                        return;
                    }
                }

//            // XXX the board should eventually be created programmatically or loaded from a file
//            //     here we just create an empty board with the required number of players.
//            Board board = new Board(8, 8);

                gameController = new GameController(board);
                int no = result.get();
                for (int i = 0; i < no; i++) {
                    Player player = new Player(board, PLAYER_COLORS.get(i), "Player " + (i + 1));
                    board.addPlayer(player);
                    player.setSpace(board.getSpace(i % board.width, i));
                }
                // XXX: V2
                // board.setCurrentPlayer(board.getPlayer(0));
                gameController.startProgrammingPhase();
                roboRally.createBoardView(gameController);
            }
        }
    }

    public void saveGame(File file) {
        // TODO make it possible to save in all phases or disable saving when not in programming phase.
        try (FileOutputStream fileOutputStream = new FileOutputStream(file); ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(gameController.board);
            objectOutputStream.flush();
            System.out.printf("Saved to %s\n", file.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadGame(File file) {
        // XXX needs to be implemented eventually
        // for now, we just create a new game

        try (FileInputStream fileInputStream = new FileInputStream(file); ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            Board board = (Board) objectInputStream.readObject();
            newGame(board, true);
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Stop playing the current game, giving the user the option to save
     * the game or to cancel stopping the game. The method returns true
     * if the game was successfully stopped (with or without saving the
     * game); returns false, if the current game was not stopped. In case
     * there is no current game, false is returned.
     *
     * @return true if the current game was stopped, false otherwise
     */
    public boolean stopGame() {
        if (gameController != null) {

            // here we save the game (without asking the user).
            File f = new File("./saves");
            f.mkdir();
            f = new File("./saves/autosave.txt");
            saveGame(f);

            gameController = null;
            roboRally.createBoardView(null);
            return true;
        }
        return false;
    }

    public void exit() {
        if (gameController != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Exit RoboRally?");
            alert.setContentText("Are you sure you want to exit RoboRally?");
            Optional<ButtonType> result = alert.showAndWait();

            if (!result.isPresent() || result.get() != ButtonType.OK) {
                return; // return without exiting the application
            }
        }

        // If the user did not cancel, the RoboRally application will exit
        // after the option to save the game
        if (gameController == null || stopGame()) {
            Platform.exit();
        }
    }

    public boolean isGameRunning() {
        return gameController != null;
    }


    @Override
    public void update(Subject subject) {
        // XXX do nothing for now
    }

    public File getFile() {
        FileChooser fileChooser = createFileChooser("Open Save File");

        // Show file chooser dialog
        File selectedFile = fileChooser.showOpenDialog(roboRally.getStage());
        if (selectedFile != null) System.out.println(selectedFile.getName());
        return selectedFile;
    }

    public File createFile() {
        FileChooser fileChooser = createFileChooser("Create Save File");

        // Show file chooser dialog
        fileChooser.setInitialFileName("roborally-game.txt");
        File selectedFile = fileChooser.showSaveDialog(roboRally.getStage());
        if (selectedFile != null) System.out.println(selectedFile.getName());
        return selectedFile;
    }

    private FileChooser createFileChooser(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);

        String currentWorkingDir = Paths.get("").toAbsolutePath().toString();
        File initialDirectory = new File(currentWorkingDir);
        fileChooser.setInitialDirectory(initialDirectory);

        // Add extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text Files (*.txt), JSON Files (*.json)", "*.txt", "*.json");
        FileChooser.ExtensionFilter showAllFilter = new FileChooser.ExtensionFilter("All (*)", "*");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.getExtensionFilters().add(showAllFilter);


        return fileChooser;
    }

    public Optional<Board> getBoardFromFile() {
        Alert gameboardSelectorAlert = new Alert(AlertType.INFORMATION);
        gameboardSelectorAlert.setTitle("Gameboard Selection");
        gameboardSelectorAlert.setHeaderText("Please select a gameboard file.");
        gameboardSelectorAlert.setContentText("Click 'OK' to continue.");

        Optional<ButtonType> buttonClick = gameboardSelectorAlert.showAndWait();

        if (buttonClick.isPresent()) {
            FileChooser fileChooser = createFileChooser("File Explorer");
            File boardFile = fileChooser.showOpenDialog(null);
            return Board.createBoardFromFile(boardFile);
        }

        return Optional.empty();
    }
}
