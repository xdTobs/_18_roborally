package dk.dtu.eighteen.roborally;

import dk.dtu.eighteen.roborally.API.Game;
import dk.dtu.eighteen.roborally.API.User;
import dk.dtu.eighteen.roborally.controller.AppController;
import dk.dtu.eighteen.roborally.fileaccess.model.BoardTemplate;
import dk.dtu.eighteen.roborally.model.Board;
import dk.dtu.eighteen.roborally.model.Move;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
@RestController
public class Server {
    private static AtomicInteger counter = new AtomicInteger(0);
    UUID uuid = UUID.randomUUID();
    HashMap<Integer, Game> mainControllers = new HashMap<>();

    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
//        var a = getResourceFolderFiles("playableBoards");
//        a.forEach(name -> System.out.println(name));
    }


    public static Board getStandardBoard() {
        return Board.createBoardFromResource("/playableBoards/2x2-board-empty-json");
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
        List<File> fileArr = List.of(new File(path).listFiles());
        return fileArr.stream().map(file -> file.getName()).toList();
        //
        // File[] files -> String filename
        // files.map(file -> file.toString())
        //
        // numbers.map(i => i + 3);
        // let myFunction = () => console.log("aa")
        // function myFunction() {
        //      console.log("aaa")
        //  }
    }

    @GetMapping("/board/{gameid}/{userid}")
    public BoardTemplate getBoard(@PathVariable int gameid, @PathVariable int userid) {
        if (!mainControllers.containsKey(gameid))
            return null;
        Game game = mainControllers.get(gameid);
        List<Integer> IDs = game.getUsers().stream().map(User::getID).toList();
        if (!IDs.contains(userid))
            return null;
        return new BoardTemplate(mainControllers.get(gameid).appController.getGameController().board, IDs.indexOf(userid));
    }

    @GetMapping("/board")
    public List<String> getBoardNames() {
        return getResourceFolderFiles("playableBoards");
    }

    @GetMapping("/board/{gameid}")
    public BoardTemplate getBoard(@PathVariable int gameid) {
        if (!mainControllers.containsKey(gameid))
            return null;
        Game game = mainControllers.get(gameid);
        List<Integer> IDs = game.getUsers().stream().map(User::getID).toList();

        return new BoardTemplate(mainControllers.get(gameid).appController.getGameController().board);
    }

    @GetMapping("/join/{gameid}")
    public int joinGame(@PathVariable int gameid) {
        if (!mainControllers.containsKey(gameid))
            return -1;
        User newUser = new User();
        mainControllers.get(gameid).addUser(newUser);
        return newUser.getID();
    }

    @GetMapping("/start/{gameid}")
    public String startGame(@PathVariable int gameid) {
        if (!mainControllers.containsKey(gameid))
            return "game not found";

        mainControllers.get(gameid).appController.newGame(getStandardBoard());
        return "Game with the ID: " + gameid + " was started";
    }

    @GetMapping(value = "/setup")
    @ResponseBody
    public int setupGame() {
        int id = counter.incrementAndGet();
        mainControllers.put(id, new Game(new AppController()));
        return id;
    }

    @GetMapping(value = "/test/{id}")
    @ResponseBody
    public int test(@PathVariable int id) {
        return id;
    }

    @PostMapping(value = "/game/move/{gameid}/{userid}")
    @ResponseBody
    public String updateMove(@PathVariable int gameid, @PathVariable int userid, @RequestBody Move move) {
        if (!mainControllers.containsKey(gameid))
            return "Game not found";
        Game game = mainControllers.get(gameid);
        List<Integer> IDs = game.getUsers().stream().map(User::getID).toList();
        if (!IDs.contains(userid))
            return "User not found";
        if (!move.checkValid())
            return "Invalid move";
        game.appController.getGameController().board.getPlayer(IDs.indexOf(userid)).setCurrentMove(move);
        return "Successfully updated move";
    }


    @GetMapping("/endProgramming")
    public String endProgramming(@RequestParam(name = "id") Integer id,
                                 @RequestParam(name = "x", required = false, defaultValue = "1") Integer x,
                                 @RequestParam(name = "y", required = false, defaultValue = "1") Integer y
    ) {

        /*List<Integer> IDs = game.getUsers().stream().map(User::getID).toList();
        if (!IDs.contains(id))
            return "This user is unknown";
        int playerNo = IDs.indexOf(id) ;

        gameController.endProgramming(playerNo, x, y);
        return String.format("Moved player %d, to (%d,%d)%n", playerNo, x, y);*/
        return "not implemented";
    }
}
