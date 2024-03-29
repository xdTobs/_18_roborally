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
import dk.dtu.eighteen.roborally.fileaccess.model.CommandCardFieldTemplate;

/**
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class CommandCardField extends Subject {

    public Player player;

    private CommandCard card;

    private boolean visible;

    public CommandCardField(Player player) {
        this.player = player;
        this.card = null;
        this.visible = true;
    }

    /***
     * Constructor to convert from CommandCardFieldTemplate to CommandCardField
     * @param template template for CommandCardField
     * @param player Used to reinstate pointers
     */
    public CommandCardField(CommandCardFieldTemplate template, Player player) {
        if(template.card!=null)
            this.card = new CommandCard(template.card);
        this.visible = template.visible;
        this.player = player;
    }

    public CommandCard getCard() {
        return card;
    }

    public void setCard(CommandCard card) {
        if (card != this.card) {
            this.card = card;
            notifyChange();
        }
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        if (visible != this.visible) {
            this.visible = visible;
            notifyChange();
        }
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return "player={" + player +
                "card=" + card +
                ", visible=" + visible +
                '}';
    }
}
