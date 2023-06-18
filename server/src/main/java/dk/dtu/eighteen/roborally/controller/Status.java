package dk.dtu.eighteen.roborally.controller;

/**
 * Contains the state of the game.
 * This is not used primarily for keep track of board state, but of connection state, and load state and such.
 */
public enum Status {
	NOT_INITIATED_GAME, INIT_NEW_GAME, INIT_LOAD_GAME, RUNNING, INTERACTIVE, QUITTING, INVALID_GAME_ID, GAMEOVER;

	/**
	 * Converts a string to Status
	 * 
	 * @param statusString string containing the status
	 * @return Status
	 */
	public static Status of(String statusString) {
		return switch (statusString.toUpperCase()) {
			case "NOT_INITIATED_GAME" -> NOT_INITIATED_GAME;
			case "INIT_NEW_GAME" -> INIT_NEW_GAME;
			case "INIT_LOAD_GAME" -> INIT_LOAD_GAME;
			case "RUNNING" -> RUNNING;
			case "QUITTING" -> QUITTING;
			case "INTERACTIVE" -> INTERACTIVE;
			case "GAMEOVER" -> GAMEOVER;
			default -> throw new IllegalStateException("Unexpected value: " + statusString.toUpperCase());
		};

	}
}
