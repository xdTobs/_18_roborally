package eighteen.controller;

import eighteen.ClientLauncher;
import javafx.application.Platform;
import javafx.scene.control.ChoiceDialog;
import org.json.JSONArray;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class WebAppController {
    private final ClientLauncher clientLauncher;
    private Status status = null;
    private Integer gameId = null;

    public WebAppController(ClientLauncher clientLauncher) {
        this.clientLauncher = clientLauncher;
        setStatus(null);
    }

    private String dialogChoice(List<String> options, String type) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>(options.get(0), options);
        dialog.setTitle(type + " selector");
        dialog.setHeaderText("Select " + type);
        dialog.setContentText("");
        return dialog.showAndWait().orElseThrow();

    }

    void renderBoard() {
        clientLauncher.createBoardView();
    }

    public void newGame() throws IOException, URISyntaxException, InterruptedException {
        this.status = Status.INIT_NEW_GAME;
        HttpResponse<String> response = serverRequest("/board");
        var body = response.body();
        JSONArray jsonArray = new JSONArray(body);
        List<String> boardNameList = new ArrayList<>();
        for (Object a : jsonArray) {
            String s = (String) a;
            boardNameList.add(s);
        }

        String boardName = dialogChoice(boardNameList, "gameboard");
        List<String> numPlayerOptions = new ArrayList<>();
        for (int i = 2; i < 7; i++) {
            numPlayerOptions.add(String.valueOf(i));

        }
        int numberOfPlayers = Integer.parseInt(dialogChoice(numPlayerOptions, "number of players"));
        clientLauncher.setStatusText("You picked the board: " + boardName);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/game/" + boardName))
                .POST(HttpRequest.BodyPublishers.ofString(boardName)) //Something else than bordName
                .build();

        response = HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());
       gameId = Integer.valueOf(response.body());
       setStatus(Status.INIT_NEW_GAME);

        // TODO make async
//        HttpResponse<String> response = HttpClient.newBuilder()
//                .build()
//                .send(request, HttpResponse.BodyHandlers.ofString());
//
//        System.out.println("response headers: " + response.headers().toString());
//        System.out.println("response body: " + response.body());
//        return response;
        //TODO: get correct board file and create boardview
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
   //     renderBoard();
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

    private HttpResponse<String> serverRequest(String target) throws IOException, InterruptedException, URISyntaxException {
        System.out.println("make http request for " + target);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080" + target))
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

    public void setStatus(Status status) {
        if(status == null){
            clientLauncher.setStatusText("game nut running");
            return;
        }

        switch (status) {
            case INIT_NEW_GAME -> {
                clientLauncher.setStatusText("New game with ID: " + gameId);
            }
            case INIT_LOAD_GAME -> {
                clientLauncher.setStatusText("Loaded game with ID: " + gameId);
            }
            case RUNNING -> {
                clientLauncher.setStatusText("Running game with ID: " + gameId);
            }
            case QUITTING -> {
                clientLauncher.setStatusText("Quitting and saving game with ID: " + gameId);
            }
        }
    }
}
