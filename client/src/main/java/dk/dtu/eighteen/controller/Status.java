package dk.dtu.eighteen.controller;

public enum Status {
    NOT_INITIATED_GAME, INIT_NEW_GAME, INIT_LOAD_GAME, RUNNING, QUITTING, INVALID_GAME_ID;


    public static Status of(String statusString) {
        return switch (statusString.toUpperCase()) {
            case "NOT_INITIATED_GAME" -> NOT_INITIATED_GAME;
            case "INIT_NEW_GAME" -> INIT_NEW_GAME;
            case "INIT_LOAD_GAME" -> INIT_LOAD_GAME;
            case "RUNNING" -> RUNNING;
            case "QUITTING" -> QUITTING;
            default -> throw new IllegalStateException("Unexpected value: " + statusString.toUpperCase());
        };

    }
}
