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

import dk.dtu.eighteen.roborally.designpatterns.observer.Subject;
import dk.dtu.eighteen.roborally.fileaccess.model.PlayerTemplate;
import org.jetbrains.annotations.NotNull;

import static dk.dtu.eighteen.roborally.model.Heading.SOUTH;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class Player extends Subject {

    final public static int NO_REGISTER_CARDS = 5;
    final public static int NO_PLAYABLE_CARDS = 8;
    private final String[] colors = {"red", "orange", "yellow", "green", "blue", "indigo", "violet",}; // Add more colors if needed
    public Board board;
    public int x;
    public int y;
    private String name;
    private String color;
    private int checkpointCounter;
    private Heading heading = SOUTH;
    private Move currentMove;
    private CommandCardField[] playableCards;
    private CommandCardField[] registerCards;

    public Player(@NotNull Board board, String color, @NotNull String name) {
        if (color == null) {
            // The same player name always gives the same color, for easier debugging.
            int n = Math.abs(name.hashCode() % 7);
            color = colors[n];
        }
        this.board = board;
        this.name = name;
        this.color = color;
        this.currentMove = new Move();

        playableCards = new CommandCardField[NO_PLAYABLE_CARDS];
        for (int i = 0; i < playableCards.length; i++) {
            playableCards[i] = new CommandCardField(this);
        }

        registerCards = new CommandCardField[NO_REGISTER_CARDS];
        for (int i = 0; i < registerCards.length; i++) {
            registerCards[i] = new CommandCardField(this);
        }
    }

    public Player(PlayerTemplate template, Board board) {
        this.name = template.name;
        this.color = template.color;
        this.board = board;
        this.x = board.getSpace(template.x, template.y).x;
        this.y = board.getSpace(template.x, template.y).y;
        this.heading = template.heading;
        this.currentMove = template.currentMoves;

        playableCards = new CommandCardField[NO_PLAYABLE_CARDS];
        registerCards = new CommandCardField[NO_REGISTER_CARDS];
        if (template.playableCards != null) {
            for (int i = 0; i < playableCards.length; i++) {
                playableCards[i] = new CommandCardField(template.playableCards[i], this);
            }
        }
        for (int i = 0; i < registerCards.length; i++) {
            registerCards[i] = new CommandCardField(this);
        }
    }

    //private boolean hasMovedThisTurn = false;

    public CommandCardField getRegisterCardField(int i) {
        return registerCards[i];
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


    public CommandCardField getPlayableCard(int i) {
        return playableCards[i];
    }

    public CommandCardField[] getPlayableCards() {
        return playableCards;
    }

    public Move getCurrentMove() {
        return currentMove;
    }

    public void setCurrentMove(Move currentMove) {
        this.currentMove = currentMove;
    }

    @Override
    public String toString() {
        return "Player{" +
                ", x=" + x +
                ", y=" + y +
                ", name='" + name + '\'' +
                '}';
    }
}
