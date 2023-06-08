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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dk.dtu.eighteen.roborally.controller.Actions.Checkpoint;
import dk.dtu.eighteen.roborally.controller.Actions.IFieldAction;
import dk.dtu.eighteen.roborally.designpatterns.observer.Subject;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
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
    private boolean stepMode;

    //TODO: Add swtich to check which characters is in the boardAsString and make fields according to this.
    //TODO: Fix test board constructor
//    public Board(int width, int height, @NotNull String boardName, Space[][] spaces) {
//        this.boardName = boardName;
//        this.width = width;
//        this.height = height;
//        this.spaces = spaces;
//        this.stepMode = false;
//    }

    public Board(int width, int height, String boardName) {
        this.width = width;
        this.height = height;
        this.boardName = boardName;
        this.spaces = new Space[width][height];
    }


    // Creates a Board object with a given width, height, and name
    // and initializes a 2D array of Spaces with coordinates.
    //add checkpoint
    // The stepMode is set to false.

    public static String toJson(Board board) {
        GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.toJson(board);
    }

    public static Board fromJson(BufferedReader bufferedReader) {

        String json = bufferedReader.lines().collect(Collectors.joining("\n"));
        Board board = new Gson().fromJson(json, Board.class);
        for (Player player : board.getPlayers()) {
            player.board = board;
            // Make the two references one.
            int x = player.getSpace().x;
            int y = player.getSpace().y;
            Space space = board.getSpace(x, y);
            player.setSpace(space);
            space.setPlayer(player);
            /*for (int i = 0; i < Player.NO_REGISTERS; i++) {
                CommandCardField commandCardField = player.getRegisterSlot(i);
                commandCardField.player = player;
                CommandCard commandCard = commandCardField.getCard();
                if (commandCard != null) {
                }
            }*/
            for (int i = 0; i < Player.NO_AVAILABLE_CARDS; i++) {
                CommandCardField commandCardField = player.getAvailableCardSlot(i);
                commandCardField.player = player;
                CommandCard commandCard = commandCardField.getCard();
                if (commandCard != null) {
                }
            }
        }
        for (Space[] row : board.getSpaces()) {
            for (Space space : row) {
                space.board = board;
            }
        }

        return board;

    }

    public boolean isGameover() {
        return findWinner() != null;
    }

    public Player findWinner() {
        List<Checkpoint> checkpoints = this.getCheckpoints();
        for (Player player : players)
            if (player.getCheckpointCounter() == checkpoints.size()) {
                return player;
            }

        return null;

    }

    public List<Player> getPlayers() {
        return players;
    }


    // Returns the Space object at a specified (x,y) position in the spaces array.
    // If the position is out of bounds, it returns null.
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

    //     Adds a new player
    public void addPlayer(@NotNull Player player) {
        if (player.board == this && !players.contains(player)) {
            players.add(player);
            notifyChange();
        }
    }


    public void createAddPlayerToEmptySpace(String color, String name) {
        Player player = new Player(this, color, name);
        Space space = getFirstEmptySpace();
        player.setSpace(space);
        space.setPlayer(player);
        players.add(player);
    }

    private Space getFirstEmptySpace() {
        for (Space[] row : spaces) {
            for (Space space : row) {
                if (space.getPlayer() == null) {
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

    // Returns the current player
    public Player getCurrentPlayer() {
        return getPlayer(currentPlayerIndex);
    }

    // Set the current player to the specified player.
    public void setCurrentPlayer(Player player) {
        Player current = getCurrentPlayer();
        if (player != current && players.contains(player)) {
            currentPlayerIndex = players.indexOf(player);
            notifyChange();
        }
    }

    // Returns the current phase
    public Phase getPhase() {
        return phase;
    }

    // Changes the current phase to a specified phase.
    public void setPhase(Phase phase) {
        if (phase == this.phase || this.phase == Phase.GAMEOVER) return;
        this.phase = phase;
        notifyChange();
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        if (step != this.step) {
            this.step = step;
            notifyChange();
        }
    }

    public boolean isStepMode() {
        return stepMode;
    }

    public void setStepMode(boolean stepMode) {
        if (stepMode != this.stepMode) {
            this.stepMode = stepMode;
            notifyChange();
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

    public int getPlayerNumber(@NotNull Player player) {
        if (player.board == this) {
            return players.indexOf(player);
        } else {
            return -1;
        }
    }

    /**
     * Returns the neighbour of the given space of the board in the given heading.
     * The neighbour is returned only, if it can be reached from the given space
     * (no walls or obstacles in either of the involved spaces); otherwise,
     * null will be returned.
     *
     * @param space   the space for which the neighbour should be computed
     * @param heading the heading of the neighbour
     * @return the space in the given direction; null if there is no (reachable) neighbour
     */
    public Space getNeighbour(@NotNull Space space, @NotNull Heading heading) {
        int x = space.x;
        int y = space.y;
        switch (heading) {
            case SOUTH:
                y = (y + 1) % height;
                break;
            case WEST:
                x = (x + width - 1) % width;
                break;
            case NORTH:
                y = (y + height - 1) % height;
                break;
            case EAST:
                x = (x + 1) % width;
                break;
        }

        return getSpace(x, y);
    }

    public String getStatusMessage() {
        // this is actually a view aspect, but for making assignment V1 easy for
        // the students, this method gives a string representation of the current
        // status of the game

        // XXX: V2 changed the status so that it shows the phase, the player and the step
        return "Phase: " + getPhase().name() + ", Player = " + getCurrentPlayer().getName() + ", Step: " + getStep();
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
        return "Board{" +
                "height=" + height +
                ", boardName='" + boardName + '\'' +
                ", players=" + players +
                ", width=" + width +
                ", currentPlayerIndex=" + currentPlayerIndex +
                ", phase=" + phase +
                ", step=" + step +
                ", stepMode=" + stepMode +
                '}';
    }

    public Player getPlayer(String playerName) {
        for (Player player : players) {
            if (player.getName().equals(playerName)) {
                return player;
            }
        }
        return null;
    }

}
