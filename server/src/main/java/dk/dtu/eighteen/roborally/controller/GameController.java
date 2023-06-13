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
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

/**
 * @author Tobias Schønau, s224327
 * @author Henrik Zenkert, s224281
 */
public class GameController {

    public Board board;

    public GameController(@NotNull Board board) {
        this.board = board;
    }

    /**
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

        for (int i = 0; i < board.getNumberOfPlayers(); i++) {
            Player player = board.getPlayer(i);
            if (player != null) {
                for (int j = 0; j < Player.NO_PLAYABLE_CARDS; j++) {
                    CommandCardField field = player.getPlayableCardField(j);
                    field.setCard(generateRandomCommandCard());
                    field.setVisible(true);
                }
            }
        }
    }

    public void loadProgrammingPhase() {

        board.setPhase(Phase.PROGRAMMING);
        board.setStep(0);
        for (Player p : board.getPlayers()) {
            for (int i = 0; i < Player.NO_REGISTER_CARDS; i++) {
                CommandCardField field = p.getRegisterCardField(i);
                field.setCard(null);
            }

        }
        board.turn++;
    }

    public CommandCard generateRandomCommandCard() {
        Command[] commands = Command.values();
        int random = (int) (Math.random() * commands.length);
        return new CommandCard(commands[random]);
    }

    public void finishProgrammingPhase() {
        board.setPhase(Phase.ACTIVATION);
        board.setCurrentPlayer(board.getPlayer(0));
    }

    /***
     * @author Tobias Schønau
     */
    public void continuePrograms() {
        do {
            executeNextStep();
        } while (board.getPhase() == Phase.ACTIVATION);
    }

    /***
     * Runs a single command card, for a single player. Is run by continuePrograms function until this function changes game state
     * @author Tobias Schønau s224327
     */
    public void executeNextStep() {
        Player currentPlayer = board.getCurrentPlayer();
        assert board.getPhase() == Phase.ACTIVATION && currentPlayer != null && board.getPhase() != Phase.GAMEOVER;

        int step = board.getStep();

        CommandCard card = currentPlayer.getRegisterCardField(step).getCard();
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
                currentPlayer = board.findWinner();
                board.setPhase(Phase.GAMEOVER);

            } else if (step < Player.NO_REGISTER_CARDS) {
                // makeProgramFieldsVisible(step);
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
            // their execution.
            // This should eventually be done in a more elegant way
            // (this concerns the way cards are modelled as well as the way they are
            // executed).

            switch (command) {
                case MOVE_1 -> this.moveForward(player);
                case MOVE_2 -> this.moveForward_2(player);
                case MOVE_3 -> this.moveForward_3(player);
                case RIGHT -> this.turnRight(player);
                case LEFT -> this.turnLeft(player);
                case U_TURN -> this.uTurn(player);
                case MOVE_BACK -> this.moveBackwards(player);
                case AGAIN -> this.again(player);
                default -> throw new RuntimeException("NOT IMPLEMENTED YET.");
            }
        }
    }

    /***
     * Function for the MOVE_1 card.
     * First finds the next space, then attempts to push a
     * potential player on that space.
     * After that, we try to move forward to the next
     * space.
     *
     * @param player Player to move
     */
    public void moveForward(@NotNull Player player) {
        int x = player.getSpace().x;
        int y = player.getSpace().y;
        int[] nextCoords = Heading.headingToCoords(player.getHeading());
        Space nextSpace = board.getSpace(x + nextCoords[0], y + nextCoords[1]);
        push(nextSpace, player.getHeading());
        if (!isPlayerIsBlockedByWall(player, nextSpace)) {
            player.setSpace(board.getSpace(x + nextCoords[0], y + nextCoords[1]));
        }

    }

    public void moveBackwards(@NotNull Player player) {
        uTurn(player);
        moveForward(player);
        uTurn(player);
    }

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
     * If we are moving south and the next space has a north wall or the current
     * space has a south wall,
     * we can't move forward.
     * Also, if the next space has a player that hasn't been
     * pushed, we cant move there
     *
     * @param player    the player that is moving
     * @param nextSpace the space the player is moving to
     * @return true if the player can't move forward
     */
    public boolean isPlayerIsBlockedByWall(Player player, Space nextSpace) {
        if (nextSpace == null)
            return true;
        Heading playerHeading = player.getHeading();
        Heading oppositePlayerHeading = playerHeading.next().next();
        Set<Heading> currentSpaceWalls = player.getSpace().getWalls();
        Set<Heading> nextSpaceWalls = nextSpace.getWalls();
        return nextSpaceWalls.contains(oppositePlayerHeading) || currentSpaceWalls.contains(playerHeading)
                || nextSpace.getPlayer() != null;

    }

    public void turnRight(@NotNull Player player) {
        player.setHeading(player.getHeading().next());
    }

    public void turnLeft(@NotNull Player player) {
        player.setHeading(player.getHeading().prev());
    }

    public void uTurn(@NotNull Player player) {
        player.setHeading(player.getHeading().prev());
        player.setHeading(player.getHeading().prev());
    }

    /***
     * Function used to push, e.g., when moving forward.
     * Cannot push into walls or
     * out of bounds.
     * Recursively calls itself if it pushes into another player.
     *
     * @param space   The space from which to push.
     *                If the space has no player,
     *                nothing happens
     * @param heading The direction which to try to push the player
     */
    public void push(Space space, Heading heading) {
        if (space == null)
            return;
        Player player = space.getPlayer();

        if (null == player)
            return;
        int x = player.getSpace().x;
        int y = player.getSpace().y;
        int[] nextCoords = Heading.headingToCoords(heading);
        Space nextSpace = board.getSpace(x + nextCoords[0], y + nextCoords[1]);
        if (nextSpace == null)
            return;
        Player playerNextSpace = nextSpace.getPlayer();

        if (!isPlayerIsBlockedByWall(player, nextSpace)) {
            if (playerNextSpace != null) {
                push(nextSpace, heading);
            }
            player.setSpace(board.getSpace(x + nextCoords[0], y + nextCoords[1]));
        }
    }

    /***
     * Recursive function for the again card
     * Does not do anything if an again card is the first card played
     *
     * @param player Player, who executes the card
     * @param step   which step of the activation phase we are in
     */
    public void again(Player player, int step) {
        if (step < 0) {
            return;
        }
        CommandCard card = player.getRegisterCardField(step - 1).getCard();
        if (card.command == Command.AGAIN) {
            again(player, step - 1);
        } else {
            executeCommand(player, card.command);
        }
    }

    /***
     * Wrapper function used to call the other again function with the current step
     *
     * @param player Player, who executes the card
     */
    public void again(@NotNull Player player) {
        again(player, board.getStep());
    }

    public Board getBoard() {
        return this.board;
    }

    @Override
    public String toString() {
        return "GameController{" + "board=" + board + '}';
    }
}
