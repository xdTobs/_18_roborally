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

/**
 * @author Henrik Zenkert, s224281@dtu.dk
 */
public enum Heading {

    NORTH, EAST, SOUTH, WEST;

    /***
     * Convert heading to offset coordinates, giving the coordinates of the space in the direction given
     * @param heading the heading to get coords for
     * @return the offset coordinates
     */
    public static int[] headingToCoords(Heading heading) {
        int[] nextCoords = new int[2];
        switch (heading) {
            case NORTH -> nextCoords = new int[]{0, -1};
            case SOUTH -> nextCoords = new int[]{0, 1};
            case EAST -> nextCoords = new int[]{1, 0};
            case WEST -> nextCoords = new int[]{-1, 0};
        }
        return nextCoords;
    }

    /***
     * Returns heading if rotating in a clockwise direction
     * @return next heading
     */
    public Heading next() {
        return values()[(this.ordinal() + 1) % values().length];
    }

    public Heading prev() {
        return values()[(this.ordinal() + values().length - 1) % values().length];
    }
}
