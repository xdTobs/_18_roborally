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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Class AppController implements the observer interface.
 * It has the core attributes of players playing the game.
 * Method: newGame takes an instance of the board class as a parameter.
 * Creates a new instance of the GameController class with the specified
 * board object and assigns it to the gameController field of
 * the AppController object. The rest is for the user interface to fill in information,
 * about what the game should include. Then it creates the boardview.

 * Methods:
 * newGame
 * newGameFromBoardfile
 * loadGame
 * startDebugGame?
 * isGameRunning
 * update
 * getFile
 * createFile
 * createFileChooser
 * getBoardFromFile

 * @author Frederik R, Frederik A, Henrik & Tobias.
 */

public class AppController implements Observer {

    final private List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(2, 3, 4, 5, 6);
    final private List<String> PLAYER_COLORS = Arrays.asList("red", "green", "blue", "orange", "grey", "magenta");

    final private RoboRally roboRally;

    private GameController gameController;

    public AppController(@NotNull RoboRally roboRally) {
        this.roboRally = roboRally;
    }

    /**
     * Method: newGame
     * takes an instance of the board class as a parameter.
     * Creates a new instance of the GameController class with the specified
     * board object and assigns it to the gameController field of
     * the AppController object. The rest is for the user interface to fill in information,
     * about what the game should include, so that the class can create the boardView later.
     *
     * @param board
     */

    public void newGame(Board board) {
        gameController = new GameController(board);
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(PLAYER_NUMBER_OPTIONS.get(0), PLAYER_NUMBER_OPTIONS);
        dialog.setTitle("Player number.");
        dialog.setHeaderText("Select number of players.");
        Optional<Integer> result = dialog.showAndWait();
        if (result.isPresent()) {
            newGameFromBoardfile(board, result.get());
        }
        roboRally.createBoardView(gameController);

    }

    /**
     * Method: newGameFromBoardFile: The UI should not allow this, but in case this happens anyway.
     * give the user the option to save the game or abort this operation!
     * @param board
     * @param players
     */
    private void newGameFromBoardfile(Board board, Integer players) {
        if (gameController != null) {
            if (!stopGame()) {
                return;
            }
        }

//            // XXX the board should eventually be created programmatically or loaded from a file
//            //     here we just create an empty board with the required number of players.
//            Board board = new Board(8, 8);

        gameController = new GameController(board);
        int no = players;
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

    /**
     * Method loadGame: creates a new instance of the gameController with
     * the specified board object and assigns it to the gamecontroller field of the AppController object.
     * Switch begins a switch statement that switches on the current phase of the game.
     * In the case of PROGRAMMING, nothing happens in that state yet.
     * roboRally.createBoardView(gameController) creates a new boardView object and adds it to RoboRally.
     * The BoardView object is created with the gameController object that was previous created with
     * the gameController that was previously created, so that it can interact with the game
     * and display the game state to the user.
     * @param board
     */
    public void loadGame(Board board) {
        gameController = new GameController(board);
        switch (gameController.board.getPhase()) {
            case PROGRAMMING -> {
            }
            default -> {
                throw new RuntimeException("Invalid save state.");
            }
        }
        roboRally.createBoardView(gameController);
    }

    /**
     * Method: startDebugGame?
     * @param board
     */
    public void startDebugGame(Board board) {
        loadGame(board);
    }

    /**
     * Method: saveGame try to output the Board-state to a Json file, so that the game-state can be saved.
     * @param file
     */

    public void saveGame(File file) {
        // TODO make it possible to save in all phases or disable saving when not in programming phase.
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(Board.toJson(gameController.board).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method stopGame: Stop playing the current game, giving the user the option to save
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
            f = new File("./saves/autosave.json");
            saveGame(f);

            gameController = null;
            roboRally.createBoardView(null);
            return true;
        }
        return false;
    }

    /**
     * Method exit: checks if user has pressed the button 'exit' and ask the user if they are sure.
     */
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

    /**
     * Method isGameRunning:  is not null if game is running.
     * @return
     */
    public boolean isGameRunning() {
        return gameController != null;
    }

    /**
     * Method update: @todo make it update after every turn (new board-state) or move?
     * @param subject the subject which changed
     */
    @Override
    public void update(Subject subject) {
        // XXX do nothing for now
    }

    /**
     * Method getFile: creates a fileschooser and specify the pathname to the Json files.
     * makes it possible for the user to select a saved file.
     * @return
     */
    public File getFile() {
        FileChooser fileChooser = createFileChooser("Open Save File");

        fileChooser.setInitialDirectory(new File("./saves"));
        // Show file chooser dialog
        File selectedFile = fileChooser.showOpenDialog(roboRally.getStage());

        if (selectedFile != null) System.out.println(selectedFile.getName());
        return selectedFile;
    }

    /**
     * Method createFile:
     * @return
     */
    public File createFile() {
        FileChooser fileChooser = createFileChooser("Create Save File");
        // Show file chooser dialog
        fileChooser.setInitialFileName("roborally-game.json");
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
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON Files (*.json)", "*.json");
        FileChooser.ExtensionFilter showAllFilter = new FileChooser.ExtensionFilter("All (*)", "*");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.getExtensionFilters().add(showAllFilter);


        return fileChooser;
    }

    /**
     * Method getBoardFromFile: Board is the return type.
     * creates text for the user when selecting a saved file.
     * creates a button and checks if it gets pressed.
     * Do not return anything if the user isn't selecting anything.
     * @return
     */
    public Board getBoardFromFile() {
        Alert gameboardSelectorAlert = new Alert(AlertType.INFORMATION);
        gameboardSelectorAlert.setTitle("Gameboard Selection");
        gameboardSelectorAlert.setHeaderText("Please select a gameboard file.");
        gameboardSelectorAlert.setContentText("Click 'OK' to continue.");

        Optional<ButtonType> buttonClick = gameboardSelectorAlert.showAndWait();

        if (buttonClick.isPresent()) {
            FileChooser fileChooser = createFileChooser("File Explorer");
            fileChooser.initialDirectoryProperty().set(new File("./Boards"));
            File boardFile = fileChooser.showOpenDialog(null);
            return Board.createBoardFromBoardFile(boardFile);
        }

        return null;
    }

    /**
     * Method getStandardBoard: Board is the return type
     * gets the standard board-files.
     * @return
     */
    public Board getStandardBoard() {
        return Board.createBoardFromBoardFile(new File("Boards/DIZZY_HIGHWAY.json"));
    }
}
