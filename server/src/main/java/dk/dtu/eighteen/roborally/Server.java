package dk.dtu.eighteen.roborally;


import dk.dtu.eighteen.roborally.controller.AppController;
import dk.dtu.eighteen.roborally.controller.Status;
import dk.dtu.eighteen.roborally.fileaccess.LoadBoard;
import dk.dtu.eighteen.roborally.fileaccess.model.BoardTemplate;
import dk.dtu.eighteen.roborally.model.Board;
import dk.dtu.eighteen.roborally.model.Moves;
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
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
@RestController
public class Server {
    private static AtomicInteger counter = new AtomicInteger(0);
    UUID uuid = UUID.randomUUID();
    HashMap<Integer, AppController> appControllerMap = new HashMap<>();


    public static void main(String[] args) {
//        Board b = LoadBoard.loadNewGameBoard("a-test-board.json");
//        Player.createAddPlayerToEmptySpace(b, null, "p1");
//        Player.createAddPlayerToEmptySpace(b, null, "p2");
//        GameController gc = new GameController(b);
        SpringApplication.run(Server.class, args);
    }

    private static List<String> getResourceFolderFiles(String folderName) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(folderName);
        String path = url.getPath();
        List<File> files = List.of(new File(path).listFiles());
        return files.stream().map(file -> file.getName()).toList();
    }

//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
//        return true;
//    }

//
//    @GetMapping("/board/{gameid}/{userid}")
//    public BoardTemplate getBoard(@PathVariable int gameid, @PathVariable int userid) {
//        if (!mainControllers.containsKey(gameid))
//            return null;
//        Game game = mainControllers.get(gameid);
//        List<Integer> IDs = game.getUsers().stream().map(User::getID).toList();
//        if (!IDs.contains(userid))
//            return null;
//        return new BoardTemplate(mainControllers.get(gameid).appController.getGameController().board, IDs.indexOf(userid));
//    }

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
            board = LoadBoard.loadNewGameBoard(boardName);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found", e);
        }
        int id = counter.incrementAndGet();
        var player = new Player(board, null, playerName);
        board.addPlayer(player);
        var appController = new AppController(board, playerCapacity, Status.INIT_NEW_GAME);
        appControllerMap.put(id, appController);
        debugPrintAppControllerMap();
        return id;
    }

    private void debugPrintAppControllerMap() {
        System.out.println("all current games:");
        for (Integer key : appControllerMap.keySet()) {
            var appController = appControllerMap.get(key);
            System.out.println("game id: " + key);
            System.out.println("number of players: " + appController.getGameController().getBoard().getNumberOfPlayers() + "/" + appController.getPlayerCapacity());
            System.out.println();

        }
    }

    @GetMapping("/game/{gameId}")
    public Map<String, Object> getGame(@RequestHeader("roborally-player-name") String playerName, @PathVariable int gameId) {
        AppController appController = appControllerMap.get(gameId);

        if (appController == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");
        }
        Status status = appController.status;
        var hasJoined = appController.getGameController().getBoard().getPlayer(playerName) != null;
        if (status == Status.INIT_NEW_GAME || status == Status.INIT_LOAD_GAME) {
            if (!hasJoined) {
                System.out.println("player " + playerName + " has joined game " + gameId);
                joinGame(gameId, playerName);
            } else {
                System.out.println("player " + playerName + "has polled for server info" + gameId);
            }
        }
        Player player = appController.getGameController().getBoard().getPlayer(playerName);
        var board = appController.getGameController().getBoard();
        HashMap<String, Object> map = new HashMap<>();
        BoardTemplate boardTemplate = new BoardTemplate(board, player);
        map.put("board", boardTemplate);
        map.put("status", appController.status.toString());
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

        if (appController.status == Status.INIT_NEW_GAME) {
            if (playerExists) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Player name already exists");
            }
            var player = new Player(board, null, playerName);
            board.addPlayer(player);
        } else if (appController.status == Status.INIT_LOAD_GAME) {
            if (!playerExists) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Player name does not exist");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can only request adding a player when game is INIT_NEW_GAME or INIT_LOAD_GAME");
        }
        var i = appController.incrementTakenAction();
        if (i == appController.getPlayerCapacity()) {
            appController.status = Status.RUNNING;
            appController.resetTakenAction();
        }

    }

    @PostMapping("/game/{gameId}/player/{playerName}/moves")
    public void planMoves(@RequestBody int[] moves, @PathVariable int gameId, @PathVariable String playerName) {
        AppController appController = appControllerMap.get(gameId);
        Player p = appController.getGameController().getBoard().getPlayer(playerName);
        if (p == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "player not found");
        }
        Moves playerMoves = p.getCurrentMoves();
        if (!playerMoves.areValid()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "moves are not valid");
        }
        playerMoves.setCardIndex(moves);
        var i = appController.incrementTakenAction();
        if (i == appController.getPlayerCapacity()) {
            appController.resetTakenAction();
        }
    }


//    }
}
