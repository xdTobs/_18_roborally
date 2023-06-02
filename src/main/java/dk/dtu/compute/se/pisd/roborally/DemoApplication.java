package dk.dtu.compute.se.pisd.roborally;
import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.BoardTemplate;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@SpringBootApplication
@RestController
public class DemoApplication {
    AppController mainController = new AppController();
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
    @GetMapping("/board")
    public BoardTemplate hello() {
        return mainController.getBoardTemplate();
    }
    @GetMapping("/start")
    public String startGame() {
        mainController.newGame(getStandardBoard());
        return "Success";
    }

    public static Board getStandardBoard() {
        return Board.createBoardFromBoardFile(new File("Boards/DIZZY_HIGHWAY.json"));
    }
}
