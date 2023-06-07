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

import dk.dtu.eighteen.designpatterns.observer.Subject;
import dk.dtu.eighteen.roborally.fileaccess.model.PlayerTemplate;
import org.jetbrains.annotations.NotNull;

import static dk.dtu.eighteen.roborally.model.Heading.SOUTH;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class Player extends Subject {

    final public static int NO_REGISTERS = 5;
    final public static int NO_AVAILABLE_CARDS = 8;

    public Board board;
    int x;
    int y;
    private String name;
    private String color;
    private int checkpointCounter;
    private Heading heading = SOUTH;
    private Moves currentMoves;
    private CommandCardField[] availableCardSlots;

    public Player(@NotNull Board board, String color, @NotNull String name) {
        this.board = board;
        this.name = name;
        this.color = color;


        this.currentMoves = new Moves();

        availableCardSlots = new CommandCardField[NO_AVAILABLE_CARDS];
        for (int i = 0; i < availableCardSlots.length; i++) {
            availableCardSlots[i] = new CommandCardField(this);
        }
    }

    //private boolean hasMovedThisTurn = false;

    public Player(PlayerTemplate template, Board board) {
        this.name = template.name;
        this.color = template.color;
        this.board = board;
        this.x = board.getSpace(template.x, template.y).x;
        this.y = board.getSpace(template.x, template.y).y;
        this.heading = template.heading;
        this.currentMoves = template.currentMoves;

        availableCardSlots = new CommandCardField[NO_AVAILABLE_CARDS];
        for (int i = 0; i < availableCardSlots.length; i++) {
            availableCardSlots[i] = new CommandCardField(template.availableCardSlots[i], this);
        }
    }

    public static void createAddPlayerToEmptySpace(Board board, String color, String name) {
        Player player = new Player(board, color, name);
        board.addPlayer(player);
        int x = 0;
        int y = 0;
        while (board.getSpace(x, y) != null) {
            x++;
            if (x == board.width - 1) {
                x = 0;
                y++;
            }
        }
    }

    public Moves getCurrentMoves() {
        return currentMoves;
    }

    public int getCheckpointCounter() {
        return checkpointCounter;
    }

    public void incrementCheckpointCounter() {
        this.checkpointCounter++;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && !name.equals(this.name)) {
            this.name = name;
            notifyChange();
        }
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        notifyChange();
    }

    public Space getSpace() {
        return board.getSpace(x, y);
    }

    public void setSpace(Space space) {
        this.x = space.x;
        this.y = space.y;

    }

    public Heading getHeading() {
        return heading;
    }

    public void setHeading(@NotNull Heading heading) {
        this.heading = heading;
    }

    /*public boolean hasMovedThisTurn() {
        return hasMovedThisTurn;
    }

    public void setHasMovedThisTurn(boolean hasMovedThisTurn) {
        this.hasMovedThisTurn = hasMovedThisTurn;
    }*/


    public CommandCardField getAvailableCardSlot(int i) {
        return availableCardSlots[i];
    }

    public CommandCardField[] getAvailableCardSlots() {
        return availableCardSlots;
    }

    public Moves getCurrentMove() {
        return currentMoves;
    }

}
