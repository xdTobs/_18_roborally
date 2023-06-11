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
import dk.dtu.eighteen.roborally.controller.Actions.IFieldAction;
import dk.dtu.eighteen.roborally.designpatterns.observer.Subject;
import dk.dtu.eighteen.roborally.fileaccess.model.SpaceTemplate;

import java.util.*;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class Space extends Subject {
    public final int x;
    public final int y;
    public Board board;
    private Set<Heading> walls = new HashSet<>();
    private List<IFieldAction> actions = new ArrayList<>();


    public Space(Board board, int x, int y) {
        this.board = board;
        this.x = x;
        this.y = y;
    }

    public Space(SpaceTemplate template, Board board) {
        this.x = template.x;
        this.y = template.y;
        this.walls = template.walls;
        this.actions = template.actions;
        this.board = board;

    }

    public String toJson() {
        GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.toJson(this);
    }


    void playerChanged() {
        // This is a minor hack; since some views that are registered with the space
        // also need to update when some player attributes change, the player can
        // notify the space of these changes by calling this method.
    }

    public Set<Heading> getWalls() {
        return walls;
    }

    public void addWalls(Heading... walls) {
        this.walls.addAll(Arrays.asList(walls));
    }

    public List<IFieldAction> getActions() {
        return actions;
    }

    public void addActions(IFieldAction... actions) {
        this.actions.addAll(Arrays.asList(actions));
    }

    @Override
    public String toString() {
        return "Space{" +
                "x=" + x +
                ", y=" + y +
                ", board=" + board +
                ", walls=" + walls +
                ", actions=" + actions +
                '}';
    }
}
