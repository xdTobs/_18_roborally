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
package dk.dtu.eighteen.roborally.model;

import dk.dtu.eighteen.roborally.controller.Actions.Checkpoint;
import dk.dtu.eighteen.roborally.controller.Actions.IFieldAction;
import dk.dtu.eighteen.roborally.designpatterns.observer.Subject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the board of the game.
 * It contains the spaces of the board, the players, the current player, the current phase and the current step.
 *
 * @author Tobias Schønau, s224327@dtu.dk
 * @author Henrik Zenkert, s224281@dtu.dk
 */
public class Board extends Subject {

    public final int height;
    public final String boardName;
    private final Space[][] spaces;
    private final List<Player> players = new ArrayList<>();
    public int width;
    private int currentPlayerIndex = 0;
    private Phase phase = Phase.INITIALISATION;
    private int step = 0;
    public int turn;

    public Board(int width, int height, String boardName) {
        this.width = width;
        this.height = height;
        this.boardName = boardName;
        this.spaces = new Space[width][height];
    }

    public boolean isGameover() {
        return findWinner() != null;
    }

    /***
     * Function for finding winning player, returns null if no winner yet.
     * Can never have a winner if no checkpoints on board
     *
     * @return Winning player, null if no winner yet
     */
    public Player findWinner() {
        List<Checkpoint> checkpoints = this.getCheckpoints();

        if (checkpoints.size() == 0)
            return null;
        for (Player player : players)
            if (player.getCheckpointCounter() == checkpoints.size()) {
                return player;
            }

        return null;

    }

    public List<Player> getPlayers() {
        return players;
    }

    /***
     * Returns the Space object at a specified (x,y) position in the space array.
     * If the position is out of bounds, it returns null.
     *
     * @return Space of given space
     */

    public Space getSpace(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return spaces[x][y];
        } else {
            return null;
        }
    }

    public void setSpace(int x, int y, Space space) {
        spaces[x][y] = space;
    }

    public int getNumberOfPlayers() {
        return players.size();
    }

    // Adds a new player
    public void addPlayer(@NotNull Player player) {
        if (player.board == this && !players.contains(player)) {
            players.add(player);

        }
    }

    /**
     * Creates a new player and adds it to the board in the first empty square we
     * can find.
     *
     * @param color the color of the player
     * @param name  the name of the player
     */
    public void createAddPlayerToEmptySpace(String color, String name) {
        Player player = new Player(this, color, name);
        Space space = getFirstEmptySpace();
        assert space != null;
        player.setSpace(space);
        players.add(player);
    }

    private Space getFirstEmptySpace() {
        for (Space[] value : spaces) {
            for (Space space : value) {
                if (players.stream().map(Player::getSpace).noneMatch(s -> s == space)) {
                    return space;
                }
            }
        }
        return null;
    }

    // Returns the player at a specified index from the list of players.
    // If the index is out of bounds, it returns null.
    public Player getPlayer(int i) {
        if (i >= 0 && i < players.size()) {
            return players.get(i);
        } else {
            return null;
        }
    }

    public Player getCurrentPlayer() {
        return getPlayer(currentPlayerIndex);
    }

    /**
     * Sets the current player to a specified player.
     *
     * @param player the player to be set string containing the status
     */
    public void setCurrentPlayer(Player player) {
        Player current = getCurrentPlayer();
        if (player != current && players.contains(player)) {
            currentPlayerIndex = players.indexOf(player);

        }
    }

    /**
     * Returns the current phase
     *
     * @return the current phase
     */
    public Phase getPhase() {
        return phase;
    }

    /**
     * Changes the current phase to a specified phase.
     *
     * @param phase phase to set
     */
    public void setPhase(Phase phase) {
        if (phase == this.phase || this.phase == Phase.GAMEOVER)
            return;
        this.phase = phase;

    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    /***
     * Method for moving cards from one Field to another. Called by dragging cards
     *
     * @param source Source Field
     * @param target Target Field
     * @return If the move was successful
     */
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

    public int getPlayerNumber(@NotNull Player player) {
        if (player.board == this) {
            return players.indexOf(player);
        } else {
            return -1;
        }
    }

    public String getStatusMessage(String playerName) {
        return "Phase: " + getPhase().name() + ", Player = " + playerName;
    }

    public List<Checkpoint> getCheckpoints() {
        List<Checkpoint> checkpoints = new ArrayList<>();

        for (Space[] row : spaces) {
            for (Space space : row) {
                for (IFieldAction actions : space.getActions()) {
                    if (actions instanceof Checkpoint checkpoint)
                        checkpoints.add(checkpoint);
                }
            }
        }
        return checkpoints;

    }

    public Space[][] getSpaces() {
        return spaces;
    }

    @Override
    public String toString() {
        return "Board{" + "height=" + height + ", boardName='" + boardName + '\'' + ", players=" + players + ", width="
                + width + ", currentPlayerIndex=" + currentPlayerIndex + ", phase=" + phase + ", step=" + step + '}';
    }

    /**
     * Returns the player with the specified name, or null if there is no player
     *
     * @param playerName name of player
     * @return the player with the specified name, or null if there is no player
     */
    public Player getPlayer(String playerName) {
        for (Player player : players) {
            if (player.getName().equals(playerName)) {
                return player;
            }
        }
        return null;
    }
}
