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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static dk.dtu.eighteen.roborally.model.Phase.INITIALISATION;

@SpringBootApplication
@RestController
public class Server {
    private static AtomicInteger counter = new AtomicInteger(0);
    private static Map<Integer, AppController> appControllerMap = new ConcurrentHashMap<>();


    public static void main(String[] args) {
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
        return getResourceFolderFiles("playableBoards");
    }

    @PostMapping("/game")
    public int createNewGame(@RequestBody Map<String, String> userMap) {
        String boardName = userMap.get("boardName");
        String playerName = userMap.get("playerName");
        int playerCapacity = Integer.parseInt(userMap.get("playerCapacity"));
        Board board = null;
        try {
            board = LoadBoard.loadNewGameFromFile(boardName);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found", e);
        }
        int id = counter.incrementAndGet();
        var appController = new AppController(board, playerCapacity, Status.INIT_NEW_GAME);
        appController.incActionCounter();
        board.createAddPlayerToEmptySpace(null, playerName);

        appControllerMap.put(id, appController);
        return id;
    }

    @GetMapping("/game/{gameId}")
    public Map<String, Object> getGame(@RequestHeader("roborally-player-name") String playerName, @PathVariable int gameId) {
        AppController appController = appControllerMap.get(gameId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("status", appController.getStatus().toString());

        if (appController == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");
        }
        if (appController.getStatus() == Status.INTERACTIVE) {
            Player currentPlayer = appController.getGameController().getBoard().getPlayer(playerName);
            if (currentPlayer.getName().equals(playerName)) {
                int step = appController.getGameController().getBoard().getStep();
                Command card = currentPlayer.getRegisterCardField(step).getCard().command;
                JsonArray jsonArray = new JsonArray();
                for (Command opts : card.getOptions()) {
                    jsonArray.add(opts.toString());
                }
                map.put("options", jsonArray);
                return map;
            }
        }
        Status status = appController.getStatus();
        var playerExistsOnBoard = appController.getGameController().getBoard().getPlayer(playerName) != null;

        if (status == Status.INIT_LOAD_GAME && playerExistsOnBoard || status == Status.INIT_NEW_GAME && !playerExistsOnBoard) {
            joinGame(gameId, playerName);
        }
        if(appController.getGameController().board.getPhase() == INITIALISATION)
            appController.getGameController().startProgrammingPhase();
        Player player = appController.getGameController().getBoard().getPlayer(playerName);
        var board = appController.getGameController().getBoard();
        BoardTemplate boardTemplate = new BoardTemplate(board, player);
        map.put("board", boardTemplate);
        return map;
    }


    @DeleteMapping("/game/{gameId}")
    public void quitGame(@PathVariable int gameId) {
        AppController appController = appControllerMap.get(gameId);
        var isSaved = appController.saveGame(gameId);
        appControllerMap.remove(gameId);
        if (!isSaved) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "game could not be saved, but has been removed");
        }
    }

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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can only request adding a player when game is INIT_NEW_GAME or INIT_LOAD_GAME");
        }
        var i = appController.incActionCounter();
        if (i == appController.getPlayerCapacity()) {
            appController.setStatus(Status.RUNNING);
            appController.resetTakenAction();
        }

    }

    @PostMapping("/game/{gameId}/moves")
    public String planMoves(@RequestHeader("roborally-player-name") String playerName, @RequestBody List<String> moveNames, @PathVariable int gameId) {

        AppController appController = appControllerMap.get(gameId);
        if (appController.getActionCounter() == appController.getPlayerCapacity()) {
            appController.setStatus(Status.RUNNING);
        }
        Player player = appController.getGameController().getBoard().getPlayer(playerName);
        System.out.println(playerName);
        System.out.println(appController.getStatus());
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
        }
        return "moves submitted: " + String.join(", ", moveNames);
    }

//    private void runGame(AppController appController) {
//        appController.getGameController().finishProgrammingPhase();
//        appController.runActivationPhase();
//        if (appController.getStatus() == Status.RUNNING) {
//            appController.resetTakenAction();
//        }
//    }

    @PostMapping("/game/{gameId}/moves/{stringCommand}")
    public String submitInteractiveMove(@RequestHeader("roborally-player-name") String playerName,
                                        @PathVariable String stringCommand,
                                        @PathVariable int gameId) {
        Command c = Command.of(stringCommand);
        AppController appController = appControllerMap.get(gameId);
        Player player = appController.getGameController().getBoard().getPlayer(playerName);
        if (appController.getStatus() != Status.INTERACTIVE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can only request interactive moves when game is in INTERACTIVE mode");
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

    private static List<String> getResourceFolderFiles(String folderName) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(folderName);
        String path = url.getPath();
        List<File> files = List.of(new File(path).listFiles());
        return files.stream().map(file -> file.getName()).toList();
    }
}

