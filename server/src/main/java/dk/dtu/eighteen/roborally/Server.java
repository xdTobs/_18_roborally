package dk.dtu.eighteen.roborally;

import dk.dtu.eighteen.roborally.API.Game;
import dk.dtu.eighteen.roborally.API.User;
import dk.dtu.eighteen.roborally.controller.AppController;
import dk.dtu.eighteen.roborally.controller.GameController;
import dk.dtu.eighteen.roborally.fileaccess.model.BoardTemplate;
import dk.dtu.eighteen.roborally.model.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Description;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    GameController gameController = null;
    Game game = null;
    @Autowired
    ResourcePatternResolver resourceResolver;

//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
//        return true;
//    }

    public static Board getStandardBoard() {
        return Board.createBoardFromResource("/boards/dizzy_highway.json");
    }

    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
    }

    static PathMatchingResourcePatternResolver getResources() {
        try {
            resourceResolver.getResources("classpath:boards/*.json");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping(value = "/game/{boardId}", produces = "application/json")
    @Description("start a new game and returns the id of the game.")
    public Map<String, Integer> startGame(@PathVariable String boardId) {
        // This increments counter in a thread safe way and returns prev value.
        int i = counter.incrementAndGet();

        AppController appController = new AppController();
        appControllerMap.put(i, appController);
        appController.newGame(findBoards(boardId));
        gameController = appController.getGameController();

        Map<String, Integer> res = new HashMap();
        res.put("id", i);
        return res;
    }

    private Board findBoards(String boardId) {
        var x = getResources();
    }

    @GetMapping(value = "/board", produces = "application/json")
    @Description("retrieves a list of all boards containing info about width, height, boardId and name.")
    public String getBoards() {
        // TODO remove this, only for simulating lag.
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        InputStream in = this.getClass().getResourceAsStream("/boards/big-board.json");
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String res = "";
        try {
            for (String s; (s = br.readLine()) != null; res += s.trim()) ;
            System.out.println(res);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    @GetMapping("/board/{name}")
    public BoardTemplate getBoard(@PathVariable String name) {
        InputStream inputStream = this.getClass().getResourceAsStream(name);
        var a = inputStream.toString();
        return null;
    }

    @GetMapping("/join/{id}")
    public int joinGame(@PathVariable String id) {
        User newUser = new User();
        game.addUser(newUser);
        return newUser.getID();
    }


    @GetMapping(value = "/setup/{id}", produces = "application/json")
    @ResponseBody
    public String setupGame(@PathVariable int id) {
        game = new Game(appControllerMap.get(id));
        return "game: " + game;
    }

    @GetMapping("/endProgramming")
    public String endProgramming(@RequestParam(name = "ID") Integer ID,
                                 @RequestParam(name = "x", required = false, defaultValue = "1") Integer x,
                                 @RequestParam(name = "y", required = false, defaultValue = "1") Integer y
    ) {

        List<Integer> IDs = game.getUsers().stream().map(User::getID).toList();
        if (!IDs.contains(ID))
            return "This user is unknown";
        int playerNo = IDs.indexOf(ID) + 1;

        gameController.endProgramming(playerNo, x, y);
        return String.format("Moved player %d, to (%d,%d)%n", playerNo, x, y);
    }
}
