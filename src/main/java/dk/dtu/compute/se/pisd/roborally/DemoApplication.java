package dk.dtu.compute.se.pisd.roborally;
import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.BoardTemplate;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.File;

@SpringBootApplication
@RestController
public class DemoApplication {
    AppController mainController = new AppController();
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
    @GetMapping("/board")
    public BoardTemplate getBoard() {
        return mainController.getBoardTemplate();
    }
    @GetMapping("/start")
    public String startGame() {
        mainController.newGame(getStandardBoard());
        return "Success";
    }
    @GetMapping("/endProgramming")
    public String endProgramming(@RequestParam(name="playerNo", required = false, defaultValue = "1") Integer playerNo,
                                 @RequestParam(name="x", required = false, defaultValue = "1") Integer x,
                                 @RequestParam(name="y", required = false, defaultValue = "1") Integer y
    ) {
        playerNo %=4;
        x %=8;
        y%=8;
        mainController.endProgramming(playerNo,x,y);
        return String.format("Moved player %d, to (%d,%d)%n",playerNo,x,y);
    }

    public static Board getStandardBoard() {
        return Board.createBoardFromBoardFile(new File("Boards/DIZZY_HIGHWAY.json"));
    }
}
