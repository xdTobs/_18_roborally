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

import dk.dtu.eighteen.roborally.designpatterns.observer.Observer;
import dk.dtu.eighteen.roborally.designpatterns.observer.Subject;
import dk.dtu.eighteen.roborally.fileaccess.LoadBoard;
import dk.dtu.eighteen.roborally.model.Board;
import dk.dtu.eighteen.roborally.model.Phase;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class AppController implements Observer {

    //    final private List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(2, 3, 4, 5, 6);
//    final private List<String> PLAYER_COLORS = Arrays.asList("red", "green", "blue", "orange", "grey", "magenta");
    private final int playerCapacity;


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    //    List<User> users = new ArrayList<>(10);
    private Status status;

    public int getActionCounter() {
        return actionCounter;
    }

    // Use this to check if all players have joined/moved.
    private int actionCounter = 0;
    private GameController gameController;

    public AppController(Board board, int playerCapacity, Status status) {
        this.playerCapacity = playerCapacity;
        this.status = status;
        this.gameController = new GameController(board);
        board.setCurrentPlayer(board.getPlayer(0));
    }

    public int incActionCounter() {
        return ++actionCounter;
    }

    public int getPlayerCapacity() {
        return playerCapacity;
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
        return "AppController{" + "playerCapacity=" + playerCapacity + ", gameController=" + gameController + '}';
    }

    public void resetTakenAction() {
        actionCounter = 0;
    }


    public void runActivationPhase() {
        gameController.board.setPhase(Phase.ACTIVATION);
        gameController.continuePrograms();
        if (gameController.board.getPhase() == Phase.PLAYER_INTERACTION) {
            status = Status.INTERACTIVE;
        } else {
            gameController.loadProgrammingPhase();
            setStatus(Status.RUNNING);
        }
    }
}
