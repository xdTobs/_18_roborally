package eighteen.controller;

import javafx.application.Platform;
import javafx.scene.control.ChoiceDialog;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class WebAppController {
    public void newGame() throws IOException, InterruptedException, URISyntaxException {

        serverRequest("start");
        // This should be modyfied to actually allow players to select gameboards
        List<Integer> gameboardNames = Arrays.asList(1);

        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(gameboardNames.get(0), gameboardNames);
        dialog.setTitle("Gameboard Selector");
        dialog.setHeaderText("Select gameboard");
        Optional<Integer> result = dialog.showAndWait();

        if (result.isPresent() && result.equals(1)) {
            serverRequest("defaultboard");

        }
//
//            // XXX the board should eventually be created programmatically or loaded from a file
//            //     here we just create an empty board with the required number of players.
//            Board board = new Board(8, 8);
//            gameController = new GameController(board);
//            int no = result.get();
//            for (int i = 0; i < no; i++) {
//                Player player = new Player(board, PLAYER_COLORS.get(i), "Player " + (i + 1));
//                board.addPlayer(player);
//                player.setSpace(board.getSpace(i % board.width, i));
//            }
//
//            // XXX: V2
//            // board.setCurrentPlayer(board.getPlayer(0));
//            gameController.startProgrammingPhase();
//
//            roboRally.createBoardView(gameController);
//        }
    }

    public void startPolling() throws IOException, InterruptedException {


    }

    public void stopGame() {
        System.err.println("stop game not implemented");
        Platform.exit();
    }

    public void saveGame() {
        System.err.println("save game not implemented");
        Platform.exit();
    }

    public void loadGame() {
        System.err.println("load game not implemented");
        Platform.exit();
    }

    public void exit() {
        Platform.exit();
    }

    public boolean isGameRunning() {
        return false;
    }

    private HttpResponse serverRequest(String target) throws IOException, InterruptedException, URISyntaxException {
        System.out.println("make http request for " + target);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/" + target))
                .header("testHeader", "teztValue")
                .GET()
                .build();

        // TODO make async
        HttpResponse<String> response = HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("response headers: " + response.headers().toString());
        System.out.println("response body: " + response.body());
        return response;
    }
}
