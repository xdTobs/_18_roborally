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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Henrik Zenkert
 *  @author Hansen
 *
 */
public enum Command {

	MOVE_1("Move 1"),
	MOVE_2("Move 2"),
	MOVE_3("Move 3"),
	RIGHT("Turn Right"),
	LEFT("Turn Left"),
	U_TURN("U-Turn"),
	MOVE_BACK("Move Back"),
	AGAIN("Again"),

	// XXX Assignment P3
	OPTION_LEFT_RIGHT("Left OR Right", LEFT, RIGHT);

	final public String displayName;

	final private List<Command> options;

	/***
	 * Constructor for Command
	 * 
	 * @param displayName Name for command
	 * @param options     Optional, options for interactive cards
	 */
	Command(String displayName, Command... options) {
		this.displayName = displayName;
		this.options = Collections.unmodifiableList(Arrays.asList(options));
	}

	/**
	 * Used to convert a string received to or from the API into a Command
	 * 
	 * @param stringCommand string to convert
	 * @return command that was given as string
	 */
	public static Command of(String stringCommand) {
		return switch (stringCommand.toUpperCase()) {
			case "MOVE_1" -> MOVE_1;
			case "MOVE_2" -> MOVE_2;
			case "MOVE_3" -> MOVE_3;
			case "RIGHT" -> RIGHT;
			case "LEFT" -> LEFT;
			case "U_TURN" -> U_TURN;
			case "MOVE_BACK" -> MOVE_BACK;
			case "AGAIN" -> AGAIN;
			case "OPTION_LEFT_RIGHT" -> OPTION_LEFT_RIGHT;
			case "EMPTY" -> null;
			default -> throw new IllegalStateException("Unexpected value: " + stringCommand.toUpperCase());
		};
	}

	/**
	 * Used to see if a command is interactive
	 * 
	 * @return true if interactive
	 */
	public boolean isInteractive() {
		return !options.isEmpty();
	}

	public List<Command> getOptions() {
		return options;
	}

}
