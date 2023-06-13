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
import dk.dtu.eighteen.roborally.model.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * ...
 * @author Hansen
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class AppController implements Observer {

    private final int playerCapacity;
    public List<Player> joinedPlayers = new ArrayList<>();


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

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
            saveState(String.valueOf(gameId));
            gameController = null;
            return true;
        }
        return false;
    }


    @Override
    public void update(Subject subject) {
    }



    public void saveState(String name) {
        LoadBoard.saveBoard(gameController.board, name);
    }


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

    /***
     * Called by server when activation phase starts or resumes
     * @author Tobias Schønau s224327, Henrik Zenkert
     */
    public void runActivationPhase() {
        gameController.board.setPhase(Phase.ACTIVATION);
        gameController.continuePrograms();
        if (gameController.board.getPhase() == Phase.PLAYER_INTERACTION) {
            status = Status.INTERACTIVE;
        } else if (gameController.board.getPhase() == Phase.GAMEOVER) {
            status = Status.GAMEOVER;
        } else {
            gameController.loadProgrammingPhase();
            setStatus(Status.RUNNING);
        }
    }
}
