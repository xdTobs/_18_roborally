package dk.dtu.eighteen.roborally;

//import dk.dtu.eighteen.roborally.API.Status;

import dk.dtu.eighteen.roborally.controller.AppController;
import dk.dtu.eighteen.roborally.controller.GameController;
import dk.dtu.eighteen.roborally.controller.Status;
import dk.dtu.eighteen.roborally.fileaccess.LoadBoard;
import dk.dtu.eighteen.roborally.model.Board;
import dk.dtu.eighteen.roborally.model.Moves;
import dk.dtu.eighteen.roborally.model.Player;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
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
    List<String> boardNames = getResourceFolderFiles("playableBoards");


    public static void main(String[] args) {
        Board b = LoadBoard.loadNewGameBoard("test.json");
        Player.createAddPlayerToEmptySpace(b, null, "p1");
        Player.createAddPlayerToEmptySpace(b, null, "p2");
        GameController gc = new GameController(b);
//        SpringApplication.run(Server.class, args);
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
        int numberOfPlayersWhenGameIsFull = Integer.parseInt(userMap.get("numberOfPlayersWhenGameIsFull"));
        int id = counter.incrementAndGet();
        var board = LoadBoard.loadNewGameBoard(boardName);
        var player = new Player(board, null, playerName);
        board.addPlayer(player);
        var appController = new AppController(board, numberOfPlayersWhenGameIsFull, Status.INIT_NEW_GAME);
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
    public Map<String, Object> getGame(@PathVariable int gameId) {
        AppController appController = appControllerMap.get(gameId);
        var board = appController.getGameController().getBoard();
        Map<String, Object> map = new HashMap<>();
        map.put("board", board);
        map.put("status", appController.status);
        return map;
    }

    @DeleteMapping("/game/{gameId}")
    public void quitGame(@PathVariable int gameId) {
        AppController appController = appControllerMap.get(gameId);
        appController.stopGame(gameId);
    }


    /**
     * POST /game: Join a game
     * If joining a new game, then you can pick any playerName,
     * but if joining a game that has been loaded you have to join with one of the names that exists.
     *
     * @param gameId     ID of the game to join
     *                   (required)
     * @param playerName Name of the player to join the game
     *                   (required)
     * @return Board retrieved successfully (status code 200)
     * or Game not found (status code 404)
     * or Player name not found (status code 404)
     */
    @PostMapping("/game/{gameId}/player/{playerName}")
    public void joinGame(@PathVariable int gameId, @PathVariable String playerName) {
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


    //    @GetMapping("/join/{gameid}")
    //    public int joinGame(@PathVariable int gameid) {
    //        if (!mainControllers.containsKey(gameid))
    //            return -1;
    //        User newUser = new User();
    //        mainControllers.get(gameid).addUser(newUser);
    //        return newUser.getID();
    //    }

    //    @GetMapping("/start/{gameid}")
    //    public String startGame(@PathVariable int gameid) {
    //        if (!mainControllers.containsKey(gameid))
    //            return "game not found";
    //
    //        mainControllers.get(gameid).appController.newGame(getStandardBoard());
    //        return "Game with the ID: " + gameid + " was started";
    //    }

    //    @GetMapping(value = "/setup")
    //    @ResponseBody
    //    public int setupGame() {
    //        int id = counter.incrementAndGet();
    //        mainControllers.put(id, new Game(new AppController()));
    //        return id;
    //    }

    //    @GetMapping(value = "/test/{id}")
    //    @ResponseBody
    //    public int test(@PathVariable int id) {
    //        return id;
    //    }
    //
    //    @PostMapping(value = "/game/move/{gameid}/{userid}")
    //    @ResponseBody
    //    public String updateMove(@PathVariable int gameid, @PathVariable int userid, @RequestBody Move move) {
    //        if (!mainControllers.containsKey(gameid))
    //            return "Game not found";
    //        Game game = mainControllers.get(gameid);
    //        List<Integer> IDs = game.getUsers().stream().map(User::getID).toList();
    //        if (!IDs.contains(userid))
    //            return "User not found";
    //        if (!move.checkValid())
    //            return "Invalid move";
    //        game.appController.getGameController().board.getPlayer(IDs.indexOf(userid)).setCurrentMove(move);
    //        return "Successfully updated move";
    //    }
    //
    //
    //    @GetMapping("/endProgramming")
    //    public String endProgramming(@RequestParam(name = "id") Integer id,
    //                                 @RequestParam(name = "x", required = false, defaultValue = "1") Integer x,
    //                                 @RequestParam(name = "y", required = false, defaultValue = "1") Integer y
    //    ) {
    //
    //        /*List<Integer> IDs = game.getUsers().stream().map(User::getID).toList();
    //        if (!IDs.contains(id))
    //            return "This user is unknown";
    //        int playerNo = IDs.indexOf(id) ;
    //
    //        gameController.endProgramming(playerNo, x, y);
    //        return String.format("Moved player %d, to (%d,%d)%n", playerNo, x, y);*/
//        return "not implemented";
//    }
}
