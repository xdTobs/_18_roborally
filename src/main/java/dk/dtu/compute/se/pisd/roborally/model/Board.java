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
package dk.dtu.compute.se.pisd.roborally.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.util.*;

import static dk.dtu.compute.se.pisd.roborally.model.Phase.GAMEOVER;
import static dk.dtu.compute.se.pisd.roborally.model.Phase.INITIALISATION;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
// TODO add load and save from/to json
public class Board extends Subject implements Serializable {

    public final int height;
    public final String boardName;
    private final Space[][] spaces;
    private final List<Player> players = new ArrayList<>();
    public int width;
    private Integer gameId;
    private Player current;
    private Phase phase = INITIALISATION;
    private int step = 0;
    private boolean stepMode;

    //TODO: Add swtich to check which characters is in the boardAsString and make fields according to this.
    //TODO: Fix test board constructor
    public Board(int width, int height, @NotNull String boardName, Space[][] spaces) {

        this.boardName = boardName;
        this.width = width;
        this.height = height;
        this.spaces = spaces;
        this.stepMode = false;
    }

    public Board(int width, int height, String boardName) {
        this.width = width;
        this.height = height;
        this.boardName = boardName;
        this.spaces = new Space[width][height];
    }

    public static Optional<Board> createBoardFromInputStream(InputStream stream) {
//    public static Optional<Board> createBoardFromFile(File boardFile) {
        Reader reader = null;
        try {
            reader = new InputStreamReader(stream);
        } catch (NullPointerException e) {
            return Optional.empty();
        }
        Gson gson = new Gson();
        JsonObject jsonBoard = gson.fromJson(reader, JsonObject.class);

        String boardName = jsonBoard.get("boardName").getAsString();
        int width = jsonBoard.get("width").getAsInt();
        int height = jsonBoard.get("height").getAsInt();
        JsonArray boardRows = jsonBoard.getAsJsonArray("board");

        ArrayList<String> boardFromFile = new ArrayList<>();
        for (JsonElement row : boardRows) {
            JsonArray rowArray = row.getAsJsonArray();
            for (JsonElement cell : rowArray) {
                boardFromFile.add(cell.getAsString());
            }
        }

        Board board = new Board(width, height, boardName);
//        return new Board(width, height, boardName, boardFromFile);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                String valueAtSpace = boardFromFile.get(y * width + x);
                Space space = null;

                //When more spacetypes have been implemented, they can be put here.
                switch (valueAtSpace.charAt(0)) {
                    case 'w' -> {
                        space = new Space(x, y);
                        for (int i = 1; i < valueAtSpace.length(); i++) {
                            char c = valueAtSpace.charAt(i);
                            if (valueAtSpace.charAt(i) == 's') {
                                space.setWalls(Heading.SOUTH);
                            } else if (valueAtSpace.charAt(i) == 'w') {
                                space.setWalls(Heading.WEST);
                            } else if (valueAtSpace.charAt(i) == 'n') {
                                space.setWalls(Heading.NORTH);
                            } else if (valueAtSpace.charAt(i) == 'e') {
                                space.setWalls(Heading.EAST);
                            }
                        }
                    }
                    //TODO: Create way of adding different checkpoints
                    case 'c' -> {
                        space = new Checkpoint(board, x, y, 1);
                    }
                    case 'b' -> {
                        Heading heading = switch (valueAtSpace.charAt(2)) {
                            case 'n' -> Heading.NORTH;
                            case 'e' -> Heading.EAST;
                            case 's' -> Heading.SOUTH;
                            case 'w' -> Heading.WEST;
                            default ->
                                    throw new IllegalArgumentException("Invalid direction: " + valueAtSpace.charAt(2));
                        };
                        if (valueAtSpace.charAt(1) == 'g') {
                            space = new ConveyorBelt(board, x, y, heading);
                        } else if (valueAtSpace.charAt(1) == 'b') {
                            space = new FastConveyorBelt(board, x, y, heading);
                        }
                    }
                    case 'e' -> {
                        space = new Space(x, y);
                    }
                    default -> {
                        throw new RuntimeException("This kind of square does not exists.");
                    }
                }
                board.spaces[x][y] = space;
            }
        }
        return Optional.of(board);

    }

    // Creates a Board object with a given width, height, and name
    // and initializes a 2D array of Spaces with coordinates.
    //add checkpoint
    // The stepMode is set to false.

    public static String toJson(Board board) {
        return new Gson().toJson(board);
    }

    public static Board fromJson(String str) {
        Board b = new Gson().fromJson(str, Board.class);

        // We mark some fields as transient to avoid circular references when serializing to json.
        // When deserializing we need to recreate the avoided references.
        for (Space[] row : b.spaces) {
            for (Space space : row) {
                space.getPlayer().setSpace(space);
            }
        }
        for (Player player : b.getPlayers()) {
            for (int i = 0; i < Player.NO_REGISTERS; i++) {
                CommandCardField cf = player.getProgramField(i);
                cf.setPlayer(player);
            }

            for (int i = 0; i < Player.NO_CARDS; i++) {
                CommandCardField cf = player.getCardField(i);
                cf.setPlayer(player);
            }
        }
        return b;

    }

    public boolean isGameover() {
        return findWinner().isPresent();
    }

    // If there are no checkpoints game over is never reached.
    public Optional<Player> findWinner() {
        List<Checkpoint> checkpoints = this.getCheckpoints();
        HashMap<Player, Integer> timesLandedPerPlayer = new HashMap();
        for (Checkpoint c : checkpoints) {
            Set<Player> players = c.getPlayersLanded();
            for (Player p : players) {
                if (timesLandedPerPlayer.containsKey(p)) {
                    int val = timesLandedPerPlayer.get(p);
                    timesLandedPerPlayer.put(p, val + 1);
                } else {
                    timesLandedPerPlayer.put(p, 1);
                }
            }
        }
        for (Player p : timesLandedPerPlayer.keySet()) {
            int val = timesLandedPerPlayer.get(p);
            if (val == checkpoints.size()) {
                return Optional.of(p);
            }
        }
        return Optional.empty();

    }

    public List<Player> getPlayers() {
        return players;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        if (this.gameId == null) {
            this.gameId = gameId;
        } else {
            if (!this.gameId.equals(gameId)) {
                throw new IllegalStateException("A game with a set id may not be assigned a new id!");
            }
        }
    }

    // Returns the Space object at a specified (x,y) position in the spaces array.
    // If the position is out of bounds, it returns null.
    public Space getSpace(int x, int y) {
        if (x >= 0 && x < width &&
                y >= 0 && y < height) {
            return spaces[x][y];
        } else {
            return null;
        }
    }

    public void setSpace(int x, int y, Space space) {
        spaces[x][y] = space;
    }

    public int getPlayersNumber() {
        return players.size();
    }

    // Adds a new player
    public void addPlayer(@NotNull Player player) {
        players.add(player);
        notifyChange();
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
        return current;
    }

    // Set the current player to the specified player.
    public void setCurrentPlayer(Player player) {
        if (player != this.current && players.contains(player)) {
            this.current = player;
            notifyChange();
        }
    }

    // Returns the current phase
    public Phase getPhase() {
        return phase;
    }

    // Changes the current phase to a specified phase.
    public void setPhase(Phase phase) {
        if (phase == this.phase || this.phase == GAMEOVER) return;
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

    public int getPlayerNumber(@NotNull Player player) {
        return players.indexOf(player);
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
        return "Phase: " + getPhase().name() +
                ", Player = " + getCurrentPlayer().getName() +
                ", Step: " + getStep();
    }

    public List<Checkpoint> getCheckpoints() {
        List<Checkpoint> checkpoints = new ArrayList<>();

        for (Space[] row : spaces) {
            for (Space space : row) {
                if (space instanceof Checkpoint checkpoint) {
                    checkpoints.add(checkpoint);
                }
            }
        }
        return checkpoints;

    }

}
