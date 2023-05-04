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

import java.util.Set;

/**
 * The GameController controls everything inside the game. Contain the methods for the game-flow.
 * (AppController controls everything around the game)
 * Has a constructor - 'board'. Assigns value of the argument 'board' to the variable 'this.board'
 * Methods:
 * moveCurrentPlayerToSpace
 * startProgrammingPhase
 * resumeProgrammingPhase
 * generateRandomCommandCard
 * finishProgrammingPhase
 * makeProgramFieldsVisible
 * makeProgramFieldsInvisible
 * executePrograms
 * executeSteps
 * continueProgram
 * executeCommandOptionAndContinue
 * executeNextStep
 * executeCommand
 * moveForwards
 * moveBackwards
 * moveForwards_2
 * moveForwards_3
 * isPlayerBlockedByWall
 * turnRight
 * turnLeft
 * push
 * again
 * moveCard
 * getBoard
 *
 * @author all
 */
public class GameController {

    public Board board;

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
        int playerNumber = (board.getPlayerNumber(currentPlayer) + 1) % board.getPlayersNumber();
        board.setCurrentPlayer(board.getPlayer(playerNumber));
    }

    /**
     * Method startProgrammingPhase. Prepare board object and all players for the programming phase.
     * Sets the Phase of the board object to programming. 'Board' class has a variable that keeps track of phases.
     * In loop: iterates over every player. resets commandCardField in playerRegisterSlot and sets visibility.
     * Sets all CommandCardField's in players AvailableCardSlot to a random command card, and sets visibility.
     */
    public void startProgrammingPhase() {
        board.setPhase(Phase.PROGRAMMING);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);

        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            if (player != null) {
                for (int j = 0; j < Player.NO_REGISTERS; j++) {
                    CommandCardField field = player.getRegisterSlot(j);
                    field.setCard(null);
                    field.setVisible(true);
                }
                for (int j = 0; j < Player.NO_CARDS; j++) {
                    CommandCardField field = player.getAvailableCardSlot(j);
                    field.setCard(generateRandomCommandCard());
                    field.setVisible(true);
                }
            }
        }
    }

    /**
     * Method resumeProgrammingPhase:
     * ?
     */
    public void resumeProgrammingPhase() {
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            if (player != null) {
                for (int j = 0; j < Player.NO_REGISTERS; j++) {
                    CommandCardField field = player.getRegisterSlot(j);
                    field.setCard(null);
                    field.setVisible(true);
                }
                for (int j = 0; j < Player.NO_CARDS; j++) {
                    CommandCardField field = player.getAvailableCardSlot(j);
                    field.setCard(generateRandomCommandCard());
                    field.setVisible(true);
                }
            }
        }
    }


    private CommandCard generateRandomCommandCard() {
        Command[] commands = Command.values();
        int random = (int) (Math.random() * commands.length);
        return new CommandCard(commands[random]);
    }

    /**
     * Method finishProgrammingPhase:
     * Hides programmingField when called.
     */
    public void finishProgrammingPhase() {
        makeProgramFieldsInvisible();
        makeProgramFieldsVisible(0);
        board.setPhase(Phase.ACTIVATION);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);
    }


    private void makeProgramFieldsVisible(int register) {
        if (register >= 0 && register < Player.NO_REGISTERS) {
            for (int i = 0; i < board.getPlayersNumber(); i++) {
                Player player = board.getPlayer(i);
                CommandCardField field = player.getRegisterSlot(register);
                field.setVisible(true);
            }
        }
    }

    private void makeProgramFieldsInvisible() {
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            for (int j = 0; j < Player.NO_REGISTERS; j++) {
                CommandCardField field = player.getRegisterSlot(j);
                field.setVisible(false);
            }
        }
    }

    /**
     * Method executeProgram: turning off stepMode, so that the player can't type while program is running.
     */
    // XXX: V2
    public void executePrograms() {
        board.setStepMode(false);
        continuePrograms();
    }

    /**
     * Method executeStep is turning stepmode on, for the player(s) to interact.
     */
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

    /**
     *Method executeCommandOptionAndContinue: Sets the phase of board to 'ACTIVATION'.
     * execute commands on current player and iterate to next player. After last player 'step' variable advances.
     * Sets current player to the "first player" again. Goes out of stepmode and continue program.
     * @param command
     */
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
        if (board.getPhase() == Phase.ACTIVATION && currentPlayer != null && board.getPhase() != Phase.GAMEOVER) {
            int step = board.getStep();
            System.out.println(Board.toJson(board));
            if (step >= 0 && step < Player.NO_REGISTERS) {
                CommandCard card = currentPlayer.getRegisterSlot(step).getCard();
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
                    // New register
                    for (Player p : board.getPlayers()) {
                        IFieldAction iFieldAction = p.getSpace();
                        iFieldAction.doAction(this);
                    }
                    step++;
                    if (board.isGameover()) {
                        board.setPhase(Phase.GAMEOVER);
                    } else if (step < Player.NO_REGISTERS) {
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

//    private Optional<Player> findWinner() {
//        List<Checkpoint> checkpoints = board.getCheckpoints();
//        HashMap<Player, Integer> timesLandedPerPlayer = new HashMap();
//        for (Checkpoint c : checkpoints) {
//            Set<Player> players = c.getPlayersLanded();
//            for (Player p : players) {
//                if (timesLandedPerPlayer.containsKey(p)) {
//                    int val = timesLandedPerPlayer.get(p);
//                    timesLandedPerPlayer.put(p, val + 1);
//                } else {
//                    timesLandedPerPlayer.put(p, 1);
//                }
//            }
//        }
//        for (Player p : timesLandedPerPlayer.keySet()) {
//            int val = timesLandedPerPlayer.get(p);
//            if (val == checkpoints.size()) {
//                return Optional.of(p);
//            }
//        }
//        return Optional.empty();
//
//    }

    // XXX: V2
    private void executeCommand(@NotNull Player player, Command command) {
        if (player != null && command != null) {
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

    /**
     * Takes a player object. Gets the position, and the way the player is facing and calculates the next space forward.
     * Checks if player is blocked by a wall. If not, nextSpace is called.
     * @param player
     */
    // TODO Assignment V2
    public void moveForward(@NotNull Player player) {
        int x = player.getSpace().x;
        int y = player.getSpace().y;
        int[] nextCoords = Heading.headingToCoords(player.getHeading());
        Space nextSpace = board.getSpace(x + nextCoords[0], y + nextCoords[1]);
        if (!isPlayerBlockedByWall(player, nextSpace)) {
            if (nextSpace.getPlayer() != null) {
                push(board.getSpace(x + nextCoords[0], y + nextCoords[1]).getPlayer(), player.getHeading());
            } else {
                player.setSpace(board.getSpace(x + nextCoords[0], y + nextCoords[1]));
            }
        }
    }

    /**
     *
     * @param player
     */
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
    public boolean isPlayerBlockedByWall(Player player, Space nextSpace) {
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

    /**
     *
     * @param player
     */
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

    /**
     * Method push:
     * Pushes a player in the same direction till they reach an empty space or edge.
     *
     * @param player
     * @param direction
     */
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

    /**
     * Method again: Check for commandCard 'AGAIN'.
     * Takes the action of the previous played step.
     * @param player
     * @param step
     */
    public void again(Player player, int step) {
        if (step < 0) {
            return;
        }
        CommandCard card = player.getRegisterSlot(step).getCard();
        if (card.command == Command.AGAIN) {
            again(player, step - 1);
            return;
        } else {
            executeCommand(player, card.command);
        }
    }

    /**
     * Method again:
     * @param player
     */
    public void again(@NotNull Player player) {
        again(player, board.getStep());
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

    /**
     * returns the value of the board.
     * @return
     */
    public Board getBoard() {
        return this.board;
    }

}
