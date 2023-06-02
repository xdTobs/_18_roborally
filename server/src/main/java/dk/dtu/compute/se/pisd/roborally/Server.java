package dk.dtu.compute.se.pisd.roborally;

import dk.dtu.compute.se.pisd.roborally.API.Game;
import dk.dtu.compute.se.pisd.roborally.API.User;
import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.BoardTemplate;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@RestController
public class Server {
    AppController mainController = new AppController();
    GameController gameController = null;
    Game game = null;

    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
    }

    public static Board getStandardBoard() {
        return Board.createBoardFromResource("/boards/dizzy_highway.json");
    }

    @GetMapping("/board")
    public BoardTemplate getBoard() {
        return mainController.getBoardTemplate();
    }

    @GetMapping("/join")
    public int joinGame() {
        User newUser = new User();
        game.addUser(newUser);
        return newUser.getID();
    }

    @GetMapping("/start")
    public String startGame() {
        mainController.newGame(getStandardBoard());
        gameController = mainController.getGameController();
        return "Game Started";
    }

    @GetMapping("/setup")
    public String setupGame() {
        game = new Game(mainController);
        return "Game Setup";
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
