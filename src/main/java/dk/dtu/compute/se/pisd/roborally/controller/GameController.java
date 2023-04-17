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

import dk.dtu.compute.se.pisd.roborally.model.*;
import org.jetbrains.annotations.NotNull;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class GameController {

    final public Board board;

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
        // TODO Assignment V1: method should be implemented by the students:
        //   - the current player should be moved to the given space
        //     (if it is free()
        //   - and the current player should be set to the player
        //     following the current player
        //   - the counter of moves in the game should be increased by one
        //     if the player is moved

        // Retrieve the current player from the game board
        Player currentPlayer = board.getCurrentPlayer();
        // Set the current player's space to the specified space
        currentPlayer.setSpace(space);
        // Calculate the number of the next player
        int playerNumber = (board.getPlayerNumber(currentPlayer) + 1) % board.getPlayersNumber();
        // Set the next player as the current player on the game board
        board.setCurrentPlayer(board.getPlayer(playerNumber));
    }

    // XXX: V2
    public void startProgrammingPhase() {
        // Set the current phase to programming
        board.setPhase(Phase.PROGRAMMING);
        // Set the current player to the first player in the game
        board.setCurrentPlayer(board.getPlayer(0));
        // Set the current step to 0
        board.setStep(0);
        // Loop through each player in the game
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            // Retrieve the player from the game board
            Player player = board.getPlayer(i);
            // If the player exists, reset their program and card fields
            if (player != null) {
                // Reset the command cards in the program field for this player
                for (int j = 0; j < Player.NO_REGISTERS; j++) {
                    CommandCardField field = player.getProgramField(j);
                    field.setCard(null);
                    field.setVisible(true);
                }

                // Generate new random command cards for the player
                for (int j = 0; j < Player.NO_CARDS; j++) {
                    CommandCardField field = player.getCardField(j);
                    field.setCard(generateRandomCommandCard());
                    field.setVisible(true);
                }
            }
        }
    }

    // XXX: V2
    private CommandCard generateRandomCommandCard() {
        // Get all possible command values
        Command[] commands = Command.values();
        // Generate a random integer between 0 and the number of commands
        int random = (int) (Math.random() * commands.length);
        // Create a new CommandCard with a randomly selected command
        return new CommandCard(commands[random]);
    }

    // XXX: V2
    public void finishProgrammingPhase() {
        // Hide all program fields for all players
        makeProgramFieldsInvisible();
        // Make the program field for the first player visible
        makeProgramFieldsVisible(0);
        // Set the current phase to activation
        board.setPhase(Phase.ACTIVATION);
        // Set the current player to the first player in the game
        board.setCurrentPlayer(board.getPlayer(0));
        // Set the current step to 0
        board.setStep(0);
    }

    // XXX: V2
    private void makeProgramFieldsVisible(int register) {
        // Check that the specified register is valid
        if (register >= 0 && register < Player.NO_REGISTERS) {
            // Loop through each player in the game
            for (int i = 0; i < board.getPlayersNumber(); i++) {
                // Retrieve the player from the game board
                Player player = board.getPlayer(i);
                // Retrieve the program field for the specified register for this player
                CommandCardField field = player.getProgramField(register);
                // Make the program field visible for this player
                field.setVisible(true);
            }
        }
    }

    // XXX: V2
    private void makeProgramFieldsInvisible() {
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            for (int j = 0; j < Player.NO_REGISTERS; j++) {
                CommandCardField field = player.getProgramField(j);
                field.setVisible(false);
            }
        }
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
    private void continuePrograms() {
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

        if (nextPlayerNumber < board.getPlayersNumber()) {
            board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
        } else {
            step++;
            if (step < Player.NO_REGISTERS) {
                makeProgramFieldsVisible(step);
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
    private void executeNextStep() {
        Player currentPlayer = board.getCurrentPlayer();
        if (board.getPhase() == Phase.ACTIVATION && currentPlayer != null) {
            int step = board.getStep();
            if (step >= 0 && step < Player.NO_REGISTERS) {
                CommandCard card = currentPlayer.getProgramField(step).getCard();
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
                if (nextPlayerNumber < board.getPlayersNumber()) {
                    board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
                } else {
                    step++;
                    if (step < Player.NO_REGISTERS) {
                        makeProgramFieldsVisible(step);
                        board.setStep(step);
                        board.setCurrentPlayer(board.getPlayer(0));
                    } else {
                        startProgrammingPhase();
                    }
                }
            } else {
                // this should not happen
                assert false;
            }
        } else {
            // this should not happen
            assert false;
        }
    }

    // XXX: V2
    private void executeCommand(@NotNull Player player, Command command) {
        if (player != null && player.board == board && command != null) {
            // XXX This is a very simplistic way of dealing with some basic cards and
            //     their execution. This should eventually be done in a more elegant way
            //     (this concerns the way cards are modelled as well as the way they are executed).

            switch (command) {
                case FORWARD -> this.moveForward(player);
                case RIGHT -> this.turnRight(player);
                case LEFT -> this.turnLeft(player);
                case FAST_FORWARD -> this.fastForward(player);
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
        if (board.getSpace(x + nextCoords[0], y + nextCoords[1]) != null && board.getSpace(x + nextCoords[0], y + nextCoords[1]).getPlayer() != null) {
            push(board.getSpace(x + nextCoords[0], y + nextCoords[1]).getPlayer(), player.getHeading());
        }
        if (board.getSpace(x + nextCoords[0], y + nextCoords[1]) != null && board.getSpace(x + nextCoords[0], y + nextCoords[1]).getPlayer() == null) {
            player.setSpace(board.getSpace(x + nextCoords[0], y + nextCoords[1]));
        }

    }

    // TODO Assignment V2
    public void fastForward(@NotNull Player player) {
        moveForward(player);
        moveForward(player);
    }

    // TODO Assignment V2
    public void turnRight(@NotNull Player player) {
        player.setHeading(player.getHeading().next());
    }

    // TODO Assignment V2
    public void turnLeft(@NotNull Player player) {
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

    public boolean moveCards(@NotNull CommandCardField source, @NotNull CommandCardField target) {
        CommandCard sourceCard = source.getCard();
        CommandCard targetCard = target.getCard();
        if (sourceCard != null && targetCard == null) {
            target.setCard(sourceCard);
            source.setCard(null);
            return true;
        } else {
            return false;
        }
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

}
