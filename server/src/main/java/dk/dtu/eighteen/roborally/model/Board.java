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


    public boolean isGameover() {
        // TODO CHANGE THIS
        return false;
//        return findWinner() != null;
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
        
        }
    }


    public void createAddPlayerToEmptySpace(String color, String name) {
        Player player = new Player(this, color, name);
        Space space = getFirstEmptySpace();
        player.setSpace(space);
        players.add(player);
    }

    private Space getFirstEmptySpace() {
        for (int i = 0; i < spaces.length; i++) {
            for (int j = 0; j < spaces[i].length; j++) {
                Space space = spaces[i][j];

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

    // Returns the current player
    public Player getCurrentPlayer() {
        return getPlayer(currentPlayerIndex);
    }

    // Set the current player to the specified player.
    public void setCurrentPlayer(Player player) {
        Player current = getCurrentPlayer();
        if (player != current && players.contains(player)) {
            currentPlayerIndex = players.indexOf(player);
        
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
    
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        if (step != this.step) {
            this.step = step;
        
        }
    }

    public boolean isStepMode() {
        return stepMode;
    }

    public void setStepMode(boolean stepMode) {
        if (stepMode != this.stepMode) {
            this.stepMode = stepMode;
        
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

    public String getStatusMessage(String playerName) {
        // this is actually a view aspect, but for making assignment V1 easy for
        // the students, this method gives a string representation of the current
        // status of the game

        // XXX: V2 changed the status so that it shows the phase, the player and the step
        return "Phase: " + getPhase().name() + ", Player = " + playerName + ", Step: " + getStep();
    }

    public List<Checkpoint> getCheckpoints() {
        List<Checkpoint> checkpoints = new ArrayList<>();

        for (Space[] row : spaces) {
            for (Space space : row) {
                for (IFieldAction actions : space.getActions()) {
                    if (actions instanceof Checkpoint checkpoint) checkpoints.add(checkpoint);
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
        return "Board{" + "height=" + height + ", boardName='" + boardName + '\'' + ", players=" + players + ", width=" + width + ", currentPlayerIndex=" + currentPlayerIndex + ", phase=" + phase + ", step=" + step + ", stepMode=" + stepMode + '}';
    }

    public Player getPlayer(String playerName) {
        for (Player player : players) {
            if (player.getName().equals(playerName)) {
                return player;
            }
        }
        return null;
    }

//    public void generateCardsForPlayers() {
//        for (Player player : players) {
//            player.generateCards();
//        }
//    }
}
