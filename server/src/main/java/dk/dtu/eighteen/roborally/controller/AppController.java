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
package dk.dtu.eighteen.roborally.controller;
//import dtu.dk.eighteen.Status;

import dk.dtu.eighteen.designpatterns.observer.Observer;
import dk.dtu.eighteen.designpatterns.observer.Subject;
import dk.dtu.eighteen.roborally.fileaccess.LoadBoard;
import dk.dtu.eighteen.roborally.fileaccess.model.BoardTemplate;
import dk.dtu.eighteen.roborally.model.Board;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class AppController implements Observer {

    // Use this to check if all players have joined/moved.
    private static AtomicInteger playersTakenAction = new AtomicInteger(1);
    final private List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(2, 3, 4, 5, 6);
    final private List<String> PLAYER_COLORS = Arrays.asList("red", "green", "blue", "orange", "grey", "magenta");
    private final int playerCapacity;
    //    List<User> users = new ArrayList<>(10);
    public Status status;
    private GameController gameController;

    public AppController(Board board, int playerCapacity, Status status) {
        this.playerCapacity = playerCapacity;
        this.status = status;
        gameController = new GameController(board);
        board.setCurrentPlayer(board.getPlayer(0));
    }

    public static int incrementTakenAction() {
        return playersTakenAction.incrementAndGet();
    }

    public int getPlayerCapacity() {
        return playerCapacity;
    }

    public void loadGame(Board board) {
        gameController = new GameController(board);
        switch (gameController.board.getPhase()) {
            case PROGRAMMING -> {
            }
            case INITIALISATION -> {
            }
            default -> {
                throw new RuntimeException("Invalid save state.");
            }
        }

    }

    public void startDebugGame(Board board) {
        loadGame(board);
    }


    public void saveGame(File file) {
        // TODO make it possible to save in all phases or disable saving when not in programming phase.
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(Board.toJson(gameController.board).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public BoardTemplate getBoardTemplate() {
        return new BoardTemplate(gameController.board);
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
    public boolean saveGame(int gameId) {
        if (gameController != null) {
            // here we save the game (without asking the user).
            saveState(String.valueOf(gameId));
            gameController = null;
            return true;
        }
        return false;
    }

    public boolean isGameRunning() {
        return gameController != null;
    }


    @Override
    public void update(Subject subject) {
        // XXX do nothing for now
    }


//    public Board getStandardBoard() {
//        return Board.createBoardFromResource("boards/dizzy_highway.json");
//    }

    public void saveState(String name) {
        LoadBoard.saveBoard(gameController.board, name);
    }

//    public void loadSaveState(String name) {
//        Board board = LoadBoard.loadSaveState(name);
//        gameController = new GameController(board);
//        gameController.loadProgrammingPhase();
//    }


    //TODO used to pass to Server, maybe not the best way
    public GameController getGameController() {
        return gameController;
    }


    @Override
    public String toString() {
        return "AppController{" +
                "playerCapacity=" + playerCapacity +
                ", gameController=" + gameController +
                '}';
    }

    public void resetTakenAction() {
        playersTakenAction.set(1);
    }
}
