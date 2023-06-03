package eighteen.view;

import javafx.application.Platform;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WebAppController {
    public void newGame() throws IOException, InterruptedException, URISyntaxException {
        System.out.println("make http request for new game");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/start"))
                .header("testHeader", "teztValue")
                .GET()
                .build();

        // TODO make async
        HttpResponse<String> response = HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("response headers: " + response.headers().toString());
        System.out.println("response body: " + response.body());

//        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(PLAYER_NUMBER_OPTIONS.get(0), PLAYER_NUMBER_OPTIONS);
//        dialog.setTitle("Player number");
//        dialog.setHeaderText("Select number of players");
//        Optional<Integer> result = dialog.showAndWait();
//
//        if (result.isPresent()) {
//            if (gameController != null) {
//                // The UI should not allow this, but in case this happens anyway.
//                // give the user the option to save the game or abort this operation!
//                if (!stopGame()) {
//                    return;
//                }
//            }
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
}
