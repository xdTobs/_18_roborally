package dk.dtu.eighteen.roborally;

import com.google.gson.JsonArray;
import dk.dtu.eighteen.roborally.controller.AppController;
import dk.dtu.eighteen.roborally.controller.Status;
import dk.dtu.eighteen.roborally.fileaccess.LoadBoard;
import dk.dtu.eighteen.roborally.fileaccess.model.BoardTemplate;
import dk.dtu.eighteen.roborally.model.Board;
import dk.dtu.eighteen.roborally.model.Command;
import dk.dtu.eighteen.roborally.model.CommandCard;
import dk.dtu.eighteen.roborally.model.Player;
import jdk.jfr.Description;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Testclass testing the logic of the GameController
 *
 * @author Jakob Hansen, s224312@dtu.dk
 * @author Frederik Rolsted, s224299@dtu.dk
 */
@SpringBootApplication
@RestController
public class Server {
    private static final AtomicInteger counter = new AtomicInteger(0);
    private static final Map<Integer, AppController> appControllerMap = new ConcurrentHashMap<>();

    /**
     * The main method.
     *
     * @param args give as first argument the folder you want to use as a save
     *             folder.
     */
    public static void main(String[] args) {
        File f = new File("./savedGames");
        if (!f.exists()) {
            if (f.mkdir()) {
                System.out.println("created a new folder in the same folder as the jar file for storing save games.");
                System.out.println("location: " + f.getAbsolutePath());
            }
        }
        LoadBoard.saveFolder = f;
        SpringApplication.run(Server.class, args);
    }

    /**
     * GET /game: Get the names of available games
     *
     * @return Game names retrieved successfully (status code 200)
     */
    @GetMapping("/board")
    @Description("Get the names of available boards")
    public List<String> getBoardNames() {
        return getResourceFolderFiles();
    }

    /**
     * GET /game: Load a game from file.
     *
     * @param savegame   The name of the save game to load
     * @param playerName The name of the player to load the game for
     * @return Game loaded successfully (status code 200)
     * or Save game not found (status code 404)
     * or Player name not found in save game (status code 400)
     */

    @GetMapping("/game")
    public int loadGame(@RequestHeader("roborally-save-name") String savegame,
                        @RequestHeader("roborally-player-name") String playerName) {
        Board board;
        try {
            board = LoadBoard.loadSavedGameFromFile(savegame + ".json");
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Save game not found", e);
        }
        var players = board.getPlayers();
        List<String> names = new ArrayList<>();
        names.add("Frederik");
        names.add("Henrik");
        if (players.stream().anyMatch(player -> player.getName().equals(playerName))) {
            int id = counter.incrementAndGet();
            var appController = new AppController(board, board.getPlayers().size(), Status.INIT_LOAD_GAME);
            appControllerMap.put(id, appController);
            appController.incActionCounter();
            appController.joinedPlayers.add(board.getPlayer(playerName));
            return id;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Player name not found in save game");
        }

    }

    /**
     * POST /game: Create a new game from file.
     *
     * @param userMap contains boardName, playerName and playerCapacity
     * @return Game created successfully (status code 200), and gameId
     */

    @PostMapping("/game")
    public int createNewGame(@RequestBody Map<String, String> userMap) {
        String boardName = userMap.get("boardName");
        String playerName = userMap.get("playerName");
        int playerCapacity = Integer.parseInt(userMap.get("playerCapacity"));
        Board board;
        try {
            board = LoadBoard.loadNewGameFromFile(boardName);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found", e);
        }
        int id = counter.incrementAndGet();
        var appController = new AppController(board, playerCapacity, Status.INIT_NEW_GAME);
        appController.incActionCounter();
        board.createAddPlayerToEmptySpace(null, playerName);

        var player = board.getPlayer(playerName);
        for (int i = 0; i < 5; i++) {
            var f = player.getPlayableCardField(i);
            f.setCard(new CommandCard(Command.OPTION_LEFT_RIGHT));
        }
        appControllerMap.put(id, appController);
        return id;
    }

    /**
     * GET /game/{gameId}: Get the game state
     *
     * @param playerName the name of the player
     * @param gameId     the id of the game
     * @return Game state retrieved successfully (status code 200), and a map
     * containing different things depending on the state of the game or
     * Game not found (status code 404)
     */

    @GetMapping("/game/{gameId}")
    public Map<String, Object> getGame(@RequestHeader("roborally-player-name") String playerName,
                                       @PathVariable int gameId) {
        AppController appController = appControllerMap.get(gameId);
        if (appController == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");
        }
        Board board = appController.getGameController().getBoard();
        Player player = board.getPlayer(playerName);
        Status status = appController.getStatus();
        HashMap<String, Object> map = new HashMap<>();
        map.put("gameStatus", status.toString());
        if (status == Status.GAMEOVER)
            map.put("winner", appController.getGameController().board.getCurrentPlayer().getName());
        if (status == Status.INTERACTIVE) {
            Player playerToInteract = board.getCurrentPlayer();
            if (playerToInteract.getName().equals(playerName)) {
                int step = board.getStep();
                Command card = playerToInteract.getRegisterCardField(step).getCard().command;
                JsonArray jsonArray = new JsonArray();
                for (Command opts : card.getOptions()) {
                    jsonArray.add(opts.toString());
                }
                map.put("options", jsonArray);
                return map;
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "It is not your turn to make interactive move..");
            }
        }

        if (Status.INIT_LOAD_GAME == appController.getStatus()) {
            var alreadyJoined = appController.joinedPlayers.stream().anyMatch(p -> p.getName().equals(playerName));
            if (alreadyJoined) {
                return map;
            }
        }
        var playerExistsOnBoard = board.getPlayer(playerName) != null;
        if (status == Status.INIT_LOAD_GAME && playerExistsOnBoard
                || status == Status.INIT_NEW_GAME && !playerExistsOnBoard) {
            joinGame(gameId, playerName);
        }
        if (status == Status.INIT_LOAD_GAME && appController.getActionCounter() < appController.getPlayerCapacity()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Game is not yet ready to start, waiting for other player.");
        }

        BoardTemplate boardTemplate = new BoardTemplate(board, player);
        map.put("board", boardTemplate);
        return map;
    }

    /**
     * DELETE /game/{gameId}: Quit the game
     *
     * @param gameId the id of the game
     */
    @DeleteMapping("/game/{gameId}")
    public void quitGame(@PathVariable int gameId) {
        AppController appController = appControllerMap.get(gameId);
        var isSaved = appController.saveGame(gameId);
        appControllerMap.remove(gameId);
        if (!isSaved) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "game could not be saved, but has been removed");
        }
    }

    /**
     * a function for joining a game.
     *
     * @param gameId     the id of the game
     * @param playerName the name of the player
     */

    public void joinGame(int gameId, String playerName) {
        AppController appController = appControllerMap.get(gameId);
        var board = appController.getGameController().getBoard();
        if (board == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "board not found");
        }

        boolean playerExists = appController.getGameController().getBoard().getPlayer(playerName) != null;

        if (appController.getStatus() == Status.INIT_NEW_GAME) {
            if (playerExists) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Player name already exists");
            }
            board.createAddPlayerToEmptySpace(null, playerName);
        } else if (appController.getStatus() == Status.INIT_LOAD_GAME) {
            if (!playerExists) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Player name does not exist");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You can only request adding a player when game is INIT_NEW_GAME or INIT_LOAD_GAME");
        }
        var i = appController.incActionCounter();
        if (i == appController.getPlayerCapacity()) {
            appController.getGameController().startProgrammingPhase();
            appController.setStatus(Status.RUNNING);
            appController.resetTakenAction();
        }

    }

    @PostMapping("/game/{gameId}/moves")
    public String planMoves(@RequestHeader("roborally-player-name") String playerName,
                            @RequestBody List<String> moveNames, @PathVariable int gameId) {

        AppController appController = appControllerMap.get(gameId);
        if (appController.getActionCounter() == appController.getPlayerCapacity()) {
            appController.setStatus(Status.RUNNING);
        }
        Player player = appController.getGameController().getBoard().getPlayer(playerName);
        if (player == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "player not found");
        }

        for (int i = 0; i < 5; i++) {
            Command card = Command.of(moveNames.get(i));
            CommandCard commandCard = card == null ? null : new CommandCard(card);
            var regField = player.getRegisterCardField(i);
            regField.setCard(commandCard);
        }
        if (appController.incActionCounter() >= appController.getPlayerCapacity()) {
            appController.getGameController().finishProgrammingPhase();
            appController.runActivationPhase();
            appController.resetTakenAction();
        }
        return "moves submitted: " + String.join(", ", moveNames);
    }

    /**
     * POST /game/{gameId}/moves/{stringCommand}: Submit an interactive move
     *
     * @param playerName    the name of the player
     * @param stringCommand the command to be executed
     * @param gameId        the id of the game
     * @return Interactive move submitted successfully (status code 200)
     * or You can only request interactive moves when game is in INTERACTIVE
     * mode (status code 400)
     */
    @PostMapping("/game/{gameId}/moves/{stringCommand}")
    public String submitInteractiveMove(@RequestHeader("roborally-player-name") String playerName,
                                        @PathVariable String stringCommand, @PathVariable int gameId) {
        Command c = Command.of(stringCommand);
        AppController appController = appControllerMap.get(gameId);
        Player player = appController.getGameController().getBoard().getPlayer(playerName);
        if (appController.getStatus() != Status.INTERACTIVE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You can only request interactive moves when game is in INTERACTIVE mode");
        }
        if (player == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "player not found");
        }

        var regField = player.getRegisterCardField(appController.getGameController().getBoard().getStep());
        regField.setCard(new CommandCard(c));
        appController.setStatus(Status.RUNNING);
        appController.runActivationPhase();
        return "interactive move submitted, " + c;
    }

    /**
     * POST /game/{gameId}/save: Save the game
     *
     * @param gameId   the id of the game
     * @param saveName the name of the save
     */
    @PostMapping("/game/{gameId}")
    public void saveGame(@PathVariable int gameId, @RequestHeader("roborally-save-name") String saveName) {
        Board board = appControllerMap.get(gameId).getGameController().getBoard();
        LoadBoard.saveBoard(board, saveName + ".json");
    }

    private static List<String> getResourceFolderFiles() {
        List<String> filenames = new ArrayList<>();
        ClassLoader loader = Server.class.getClassLoader();
        try {
            InputStream inputStream = loader.getResourceAsStream("boards.txt");
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = r.readLine()) != null) {
                filenames.add(line);
            }
        } catch (Exception e) {
            filenames.add("DIZZY_HIGHWAY.json");
            System.err.println("Error: " + e.getMessage());
            System.out.println("Using default board, dizzy highway.json");
        }
        return filenames;
    }
}
