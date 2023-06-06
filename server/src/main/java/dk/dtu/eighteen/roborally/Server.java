package dk.dtu.eighteen.roborally;

import dk.dtu.eighteen.roborally.API.Status;
import dk.dtu.eighteen.roborally.controller.AppController;
import dk.dtu.eighteen.roborally.fileaccess.LoadBoard;
import dk.dtu.eighteen.roborally.model.Board;
import dk.dtu.eighteen.roborally.model.Player;
import org.springframework.boot.SpringApplication;
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

//        var b = LoadBoard.loadSaveState("playableBoards/test.json");
//        System.out.println(b);
        SpringApplication.run(Server.class, args);
    }


    public static Board getStandardBoard() {
//        return Board.createBoardFromResource("/playableBoards/2x2-board-empty-json");
        return null;
    }

//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
//        return true;
//    }

    //    private static List<String> getBoardNames() {
//        var a = Server.class.get
//
//    }
    private static List<String> getResourceFolderFiles(String folderName) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(folderName);
        String path = url.getPath();
        List<File> files = List.of(new File(path).listFiles());
        return files.stream().map(file -> file.getName()).toList();
    }
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
        System.out.println("1: " + boardName);
        System.out.println("2: " + playerName);
        System.out.println("3: " + numberOfPlayersWhenGameIsFull);
        var board = LoadBoard.loadSaveState("/playableBoards/" + boardName);
        var player = new Player(board, null, playerName);
        board.addPlayer(player);
        var appController = new AppController(board, numberOfPlayersWhenGameIsFull, Status.INIT_NEW_GAME);
        appControllerMap.put(id, appController);
        return id;
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
    public void quitGame(@PathVariable int gameId){
        AppController appController = appControllerMap.get(gameId);
        //appController.saveState(String.valueOf(gameId));
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


        if (appController.status == Status.INIT_NEW_GAME) {
            var player = new Player(board, null, playerName);
            board.addPlayer(player);
        } else if (appController.status == Status.INIT_LOAD_GAME) {
            throw new RuntimeException("Not implemented yet");
        }

        int currentNumberOfPlayers = board.getNumberOfPlayers();
        var numberOfPlayersWhenGameIsFull = appController.getNumberOfPlayersWhenGameIsFull();
        if (currentNumberOfPlayers == numberOfPlayersWhenGameIsFull) {
            appController.status = Status.RUNNING;
        }
    }

    @PostMapping("/game/{gameId}/player/{playerName}/moves")
    public void executeMove(@PathVariable int gameId, @PathVariable String playerName){
        AppController appController = appControllerMap.get(gameId);

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
