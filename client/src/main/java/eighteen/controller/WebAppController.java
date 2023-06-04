package eighteen.controller;

import eighteen.RoborallyClient;
import javafx.application.Platform;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;


public class WebAppController {

    private final RoborallyClient roborallyClient;
    private final CallbackBuilder callbackBuilder;

    public WebAppController(RoborallyClient roborallyClient, CallbackBuilder callbackBuilder) {
        this.roborallyClient = roborallyClient;
        this.callbackBuilder = callbackBuilder;
    }

    public void newGame() {

        roborallyClient.setStatus("getting available 3x3board.json, please wait.");

        ApiResponseCallback newGameCb = callbackBuilder.newGameCallback();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/start"))
                    .header("testHeader", "teztValue")
                    .GET()
                    .build();

//            HttpResponse<String> response = HttpClient.newBuilder()
//                    .build()
//                    .send(request, HttpResponse.BodyHandlers.ofString());
            CompletableFuture.supplyAsync(new Supplier<String>() {
                @Override
                public String get() {
                    try {
                        HttpResponse<String> response = HttpClient.newBuilder()
                                .build()
                                .send(request, HttpResponse.BodyHandlers.ofString());
                        return response.body();
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).thenAcceptAsync(newGameCb::onResponse);

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // TODO make async


//            Board board = new Board(8, 8);
//            gameController = new GameController(board);
//            int no = result.get();
//            for (int i = 0; i < no; i++) {
//                Player player = new Player(board, PLAYER_COLORS.get(i), "Player " + (i + 1));
//                board.addPlayer(player);
//                player.setSpace(board.getSpace(i % board.width, i));
//
//            // XXX: V2
//            // board.setCurrentPlayer(board.getPlayer(0));
//            gameController.startProgrammingPhase();
//
//            roboRally.createBoardView(gameController);

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

