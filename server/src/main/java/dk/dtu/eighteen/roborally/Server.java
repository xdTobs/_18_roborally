package dk.dtu.eighteen.roborally;

import dk.dtu.eighteen.roborally.API.Game;
import dk.dtu.eighteen.roborally.API.User;
import dk.dtu.eighteen.roborally.controller.AppController;
import dk.dtu.eighteen.roborally.controller.GameController;
import dk.dtu.eighteen.roborally.fileaccess.model.BoardTemplate;
import dk.dtu.eighteen.roborally.model.Board;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

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
    HashMap<Integer, AppController> mainControllers = new HashMap<>();
    GameController gameController = null;
    Game game = null;

    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
    }

    public static Board getStandardBoard() {
        return Board.createBoardFromResource("/boards/dizzy_highway.json");
    }

    @GetMapping("/board/")
    public BoardTemplate getBoard(String boardName) {
        return null;
    }

    @GetMapping("/join/{id}")
    public int joinGame(@PathVariable String id) {
        User newUser = new User();
        game.addUser(newUser);
        return newUser.getID();
    }

    @GetMapping("/start")
    public Map<String, String> startGame() {
        int i = counter.incrementAndGet();
        AppController mainController = new AppController();
        mainControllers.put(i, mainController);
        mainController.newGame(getStandardBoard());
        gameController = mainController.getGameController();
        // This increments counter in a thread safe way and returns prev value.
        Map<String, String> res = new HashMap<>();
        res.put("ID", String.valueOf(i));
        res.put("board", mainController.getBoardTemplate().toString());
        return res;
    }

    @GetMapping(value = "/setup/{id}", produces = "application/json")
    @ResponseBody
    public String setupGame(@PathVariable int id) {
        game = new Game(mainControllers.get(id));
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
