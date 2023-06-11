package dk.dtu.eighteen.roborally;


import dk.dtu.eighteen.roborally.controller.AppController;
import dk.dtu.eighteen.roborally.controller.Status;
import dk.dtu.eighteen.roborally.fileaccess.LoadBoard;
import dk.dtu.eighteen.roborally.fileaccess.model.BoardTemplate;
import dk.dtu.eighteen.roborally.model.Board;
import dk.dtu.eighteen.roborally.model.Command;
import dk.dtu.eighteen.roborally.model.CommandCard;
import dk.dtu.eighteen.roborally.model.Player;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Description;
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
        if (appController == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");
        }
        Status status = appController.getStatus();
        var playerExistsOnBoard = appController.getGameController().getBoard().getPlayer(playerName) != null;

        if (status == Status.PLAYERS_PROGRAMMING || status == Status.UPDATING_GAMEBOARD) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong phase for getting game update, wait for other players or game to catch up.");
        }
        if (status == Status.PLAYERS_GETTING_UPDATE) {
            appController.setRecievedBoardUpdate(playerName);
            if (appController.allPlayersHaveRecievedUpdate()) {
                appController.setStatus(Status.PLAYERS_PROGRAMMING);
            }
        }
        if (status == Status.INIT_LOAD_GAME && playerExistsOnBoard || status == Status.INIT_NEW_GAME && !playerExistsOnBoard) {
            joinGame(gameId, playerName);
        }

        Player player = appController.getGameController().getBoard().getPlayer(playerName);
        var board = appController.getGameController().getBoard();
        HashMap<String, Object> map = new HashMap<>();
        BoardTemplate boardTemplate = new BoardTemplate(board, player);
        map.put("status", appController.getStatus().toString());
        map.put("board", boardTemplate);
        appController.setRecievedBoardUpdate(playerName);
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
            appController.setStatus(Status.PLAYERS_PROGRAMMING);
            appController.resetTakenAction();
            appController.getGameController().startProgrammingPhase();

        }

    }

    @PostMapping("/game/{gameId}/moves")
    public String planMoves(@RequestHeader("roborally-player-name") String playerName, @RequestBody List<String> moveNames, @PathVariable int gameId) {

        AppController appController = appControllerMap.get(gameId);
        if (appController.getActionCounter() == appController.getPlayerCapacity()) {
            appController.setStatus(Status.UPDATING_GAMEBOARD);
        }
        Player player = appController.getGameController().getBoard().getPlayer(playerName);
        System.out.println(playerName);
        System.out.println(appController.getStatus());
        if (player == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "player not found");
        }

        outer:
        for (int i = 0; i < 5; i++) {
            Command card = Command.of(moveNames.get(i));
            CommandCard commandCard = card == null ? null : new CommandCard(card);
            var regField = player.getRegisterCardField(i);
            regField.setCard(commandCard);
        }

        if (appController.incActionCounter() == appController.getPlayerCapacity()) {
            appController.setStatus(Status.UPDATING_GAMEBOARD);
            appController.getGameController().finishProgrammingPhase();
            appController.getGameController().executePrograms();
            appController.resetTakenAction();
            appController.setStatus(Status.PLAYERS_GETTING_UPDATE);
        }
        return "moves submitted: " + String.join(", ", moveNames);

    }

    private static List<String> getResourceFolderFiles(String folderName) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(folderName);
        String path = url.getPath();
        List<File> files = List.of(new File(path).listFiles());
        return files.stream().map(file -> file.getName()).toList();
    }
}

