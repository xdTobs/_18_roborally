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

import dk.dtu.eighteen.roborally.controller.Actions.IFieldAction;
import dk.dtu.eighteen.roborally.model.*;
import javafx.scene.control.Alert;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class GameController {

    public Board board;

    {
        // this should not happen
        assert false;
    }

    public GameController(@NotNull Board board) {
        this.board = board;
    }

    /**
     * This is just some dummy controller operation to make a simple move to see something
     * happening on the board. This method should eventually be deleted!
     *
     * @param space the space to which the current player should move
     */
    public void moveCurrentPlayerToSpace(@NotNull Space space) {
        Player currentPlayer = board.getCurrentPlayer();
        currentPlayer.setSpace(space);
        int playerNumber = (board.getPlayerNumber(currentPlayer) + 1) % board.getNumberOfPlayers();
        board.setCurrentPlayer(board.getPlayer(playerNumber));
    }

    public void startProgrammingPhase() {
        board.setPhase(Phase.PROGRAMMING);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);

        for (int i = 0; i < board.getNumberOfPlayers(); i++) {
            Player player = board.getPlayer(i);
            if (player != null) {
                for (int j = 0; j < Player.NO_AVAILABLE_CARDS; j++) {
                    CommandCardField field = player.getAvailableCardSlot(j);
                    field.setCard(generateRandomCommandCard());
                    field.setVisible(true);
                }
            }
        }
    }

    public void loadProgrammingPhase() {
        //TODO currently only load in step 0, should be easy to make able to load in all steps
        board.setPhase(Phase.PROGRAMMING);
        board.setStep(0);
        for (int i = 0; i < board.getNumberOfPlayers(); i++) {
            Player player = board.getPlayer(i);
            if (player != null) {
                for (int j = 0; j < Player.NO_AVAILABLE_CARDS; j++) {
                    CommandCardField field = player.getAvailableCardSlot(j);
                    field.setVisible(true);
                }
            }
        }
    }

    public CommandCard generateRandomCommandCard() {
        Command[] commands = Command.values();
        int random = (int) (Math.random() * commands.length);
        return new CommandCard(commands[random]);
    }

    public void finishProgrammingPhase() {
        board.setPhase(Phase.ACTIVATION);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);
    }


    // XXX: V2
    public void executePrograms() {
        board.setStepMode(false);
        continuePrograms();
    }

    // XXX: V2
    public void executeStep() {
        board.setStepMode(true);
        continuePrograms();
    }

    // XXX: V2
    public void continuePrograms() {
        do {
            executeNextStep();
        } while (board.getPhase() == Phase.ACTIVATION && !board.isStepMode());
    }

    public void executeCommandOptionAndContinue(Command command) {
        board.setPhase(Phase.ACTIVATION);
        executeCommand(board.getCurrentPlayer(), command);

        Player currentPlayer = board.getCurrentPlayer();

        int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;

        int step = board.getStep();

        if (nextPlayerNumber < board.getNumberOfPlayers()) {
            board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
        } else {
            step++;
            if (step < Player.NO_REGISTERS) {
                //makeProgramFieldsVisible(step);
                board.setStep(step);
                board.setCurrentPlayer(board.getPlayer(0));
            } else {
                startProgrammingPhase();
            }
        }
        if (!board.isStepMode()) {
            continuePrograms();
        }
    }

    // XXX: V2
    public void executeNextStep() {
        Player currentPlayer = board.getCurrentPlayer();
        if (board.getPhase() != Phase.ACTIVATION || currentPlayer == null || board.getPhase() == Phase.GAMEOVER)
            assert false;

        int step = board.getStep();

        if (step < 0 || step >= Player.NO_REGISTERS) assert false;


        CommandCard card = currentPlayer.getCurrentMove().getCardAtIndex(step, currentPlayer.getCardsOnHand());
        if (card != null) {
            Command command = card.command;
            if (command.isInteractive()) {
                board.setPhase(Phase.PLAYER_INTERACTION);
                return;
            } else {
                executeCommand(currentPlayer, command);
            }
        }

        // We need to change state to get player input if we have a left or right card.
        int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
        if (nextPlayerNumber < board.getNumberOfPlayers()) {
            board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
        } else {
            // New register
            for (Player p : board.getPlayers()) {
                List<IFieldAction> actions = p.getSpace().getActions();
                for (IFieldAction IFieldAction : actions) {
                    IFieldAction.doAction(this, p.getSpace());
                }
            }
            step++;
            if (board.isGameover()) {
                board.setPhase(Phase.GAMEOVER);
                currentPlayer = board.findWinner();
                Alert gameover = new Alert(Alert.AlertType.INFORMATION);
                gameover.setTitle("Winner found!");
                gameover.setHeaderText(null);
                gameover.setContentText(currentPlayer.getName() + " has won the game! Select 'Stop Game' and then 'New Game' to play again.");
                gameover.showAndWait();
            } else if (step < Player.NO_REGISTERS) {
                //makeProgramFieldsVisible(step);
                board.setStep(step);
                board.setCurrentPlayer(board.getPlayer(0));
            } else {
                startProgrammingPhase();
            }
        }


    }

    public void executeCommand(@NotNull Player player, Command command) {
        if (command != null) {
            // XXX This is a very simplistic way of dealing with some basic cards and
            //     their execution. This should eventually be done in a more elegant way
            //     (this concerns the way cards are modelled as well as the way they are executed).

            switch (command) {
                case MOVE_1 -> this.moveForward(player);
                case MOVE_2 -> this.moveForward_2(player);
                case MOVE_3 -> this.moveForward_3(player);
                case RIGHT -> this.turnRight(player);
                case LEFT -> this.turnLeft(player);
                case U_TURN -> this.uTurn(player);
                case MOVE_BACK -> this.moveBackwards(player);
                case AGAIN -> this.again(player);
                default -> {
                    throw new RuntimeException("NOT IMPLEMENTED YET.");
                }
            }
        }
    }

    // TODO Assignment V2
    public void moveForward(@NotNull Player player) {
        int x = player.getSpace().x;
        int y = player.getSpace().y;
        int[] nextCoords = Heading.headingToCoords(player.getHeading());
        Space nextSpace = board.getSpace(x + nextCoords[0], y + nextCoords[1]);
        if (!isPlayerIsBlockedByWall(player, nextSpace)) {
            if (nextSpace.getPlayer() != null) {
                push(board.getSpace(x + nextCoords[0], y + nextCoords[1]).getPlayer(), player.getHeading());
            } else {
                player.setSpace(board.getSpace(x + nextCoords[0], y + nextCoords[1]));
            }
        }

    }

    public void moveBackwards(@NotNull Player player) {
        uTurn(player);
        moveForward(player);
        uTurn(player);
    }

    // TODO Assignment V2
    public void moveForward_2(@NotNull Player player) {
        moveForward(player);
        moveForward(player);
    }

    public void moveForward_3(@NotNull Player player) {
        moveForward(player);
        moveForward(player);
        moveForward(player);
    }

    /**
     * If we are moving south and the next space has a north wall or the current space has a south wall
     * we can't move forward.
     *
     * @param player    the player that is moving
     * @param nextSpace the space the player is moving to
     * @return true if the player can't move forward
     */
    public boolean isPlayerIsBlockedByWall(Player player, Space nextSpace) {
        if (nextSpace == null) return true;
        Heading playerHeading = player.getHeading();
        Heading oppositePlayerHeading = playerHeading.next().next();
        Set<Heading> currentSpaceWalls = player.getSpace().getWalls();
        Set<Heading> nextSpaceWalls = nextSpace.getWalls();
        if (nextSpaceWalls.contains(oppositePlayerHeading) || currentSpaceWalls.contains(playerHeading)) {
            return true;
        }
        return false;

    }


    // TODO Assignment V2
    public void turnRight(@NotNull Player player) {
        player.setHeading(player.getHeading().next());
    }

    // TODO Assignment V2
    public void turnLeft(@NotNull Player player) {
        player.setHeading(player.getHeading().prev());
    }

    public void uTurn(@NotNull Player player) {
        player.setHeading(player.getHeading().prev());
        player.setHeading(player.getHeading().prev());
    }

    public void push(@NotNull Player player, Heading direction) {
        int x = player.getSpace().x;
        int y = player.getSpace().y;
        int[] nextCoords = Heading.headingToCoords(direction);
        if (board.getSpace(x + nextCoords[0], y + nextCoords[1]) != null && board.getSpace(x + nextCoords[0], y + nextCoords[1]).getPlayer() != null) {
            push(board.getSpace(x + nextCoords[0], y + nextCoords[1]).getPlayer(), direction);
        }
        if (board.getSpace(x + nextCoords[0], y + nextCoords[1]) != null && board.getSpace(x + nextCoords[0], y + nextCoords[1]).getPlayer() == null) {
            player.setSpace(board.getSpace(x + nextCoords[0], y + nextCoords[1]));
        }
    }

    public void again(Player player, int step) {
        if (step < 0) {
            return;
        }
        CommandCard card = player.getCurrentMove().getCardAtIndex(step, player.getCardsOnHand());
        if (card.command == Command.AGAIN) {
            again(player, step - 1);
            return;
        } else {
            executeCommand(player, card.command);
        }
    }

    public void again(@NotNull Player player) {
        again(player, board.getStep());
    }


    /**
     * A method called when no corresponding controller operation is implemented yet. This
     * should eventually be removed.
     */
    public void notImplemented() {
        // XXX just for now to indicate that the actual method is not yet implemented
        System.out.println("Not implemented yet");
        assert false;
    }

    public Board getBoard() {
        return this.board;
    }

    public void endProgramming(int playerNo, int x, int y) {
        moveCurrentPlayerToSpace(board.getSpace(x, y));
    }

    @Override
    public String toString() {
        return "GameController{" +
                "board=" + board +
                '}';
    }
}
