package dk.dtu.eighteen.controller;

import dk.dtu.eighteen.roborally.fileaccess.LoadBoard;
import dk.dtu.eighteen.roborally.model.Board;
import dk.dtu.eighteen.roborally.model.Player;
import dk.dtu.eighteen.view.BoardView;
import dk.dtu.eighteen.view.RoboRallyMenuBar;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.stream.Stream;

public class ClientController extends Application {
    Text statusText = new Text("Status: start up");
    WebAppController webAppController = new WebAppController(this);
    private BorderPane boardRoot;
    private Stage stage;
//    private MinimalGameController minimalGameController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        var MIN_APP_WIDTH = 600;
        var MIN_APP_HEIGHT = 600;
        stage = primaryStage;
        RoboRallyMenuBar menuBar = new RoboRallyMenuBar(webAppController);
        boardRoot = new BorderPane(statusText);
        BorderPane.setAlignment(statusText, Pos.BOTTOM_CENTER);
        VBox vbox = new VBox(menuBar, boardRoot);
        vbox.setMinWidth(MIN_APP_WIDTH);
        vbox.setMinHeight(MIN_APP_HEIGHT);
        Scene primaryScene = new Scene(vbox);

        stage.setScene(primaryScene);
        stage.setTitle("Roborally");
        stage.setOnCloseRequest(
                e -> {
                    e.consume();
                    webAppController.exit();
                });
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();
//        try {
//            webAppController.newGame();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (URISyntaxException e) {
//            throw new RuntimeException(e);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
    }

    public void createBoardView(String json) {
        // if present, remove old BoardView
        boardRoot.getChildren().clear();
        Board board = null;
        try {
            board = LoadBoard.loadBoardFromJSONString(json);
            String[] colors = {"red","orange", "yellow", "green",  "blue", "indigo", "violet",}; // Add more colors if needed
            int i = 0;
            for (Player p : board.getPlayers()) {
                if (p.getColor() == null) {
                    String color = colors[i];
                    i++;
                    p.setColor(color);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // create and add view for new board
//        BoardView boardView = new BoardView(minimalGameController);
        // TODO find a way to show the board and make it interactive
        // Maybe we should create a IGameController interface and implement it in a minimal game controller here
        // and use that as a helper to create the board view
        BoardView boardView = new BoardView(webAppController, board);

        boardRoot.setCenter(boardView);
    }


    private ResponseEntity<String> getResponseEntity(String url) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForEntity(url, String.class);
    }

    private void jsonWriter(String json, Stream stream) {
        File file = new File("client/src/main/resources/jsonBoard.json");
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(json);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setStatusText(String s) {
        this.statusText.setText("Status: " + s);
    }

    public int getGameId() {
        return webAppController.gameId;
    }


//    public void setStatus(Status status) {
//        String s = "";
//        System.out.println("STATUS: " + status);
//        switch (status) {
//            case NOT_INITIATED_GAME -> {
//                this.setStatusText("Game not running");
//            }
//            case INIT_NEW_GAME -> {
//                s = "Times polled: " + timesPolled + "\n";
//                this.setStatusText(s + "New game with ID: " + gameId + "\nWaiting for players to join");
//            }
//            case INIT_LOAD_GAME -> {
//                this.setStatusText("Loaded game with ID: " + gameId);
//            }
//            case RUNNING -> {
//                this.setStatusText("Running game with ID: " + gameId);
//            }
//            case QUITTING -> {
//                this.setStatusText("Quitting and saving game with ID: " + gameId);
//            }
//        }
//    }

    void renderBoard(JSONObject board) {
//        BoardTemplate boardTemplate = new BoardTemplate();
//        clientLauncher.createBoardView();
    }

//    public void pollServer() throws URISyntaxException, IOException, InterruptedException {
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(new URI("http://localhost:8080/game/" + gameId))
//                .GET()
//                .header("roborally-player-name", "henrik")
//                .build();
//        var response = HttpClient.newBuilder()
//                .build()
//                .send(request, HttpResponse.BodyHandlers.ofString());
//        // put response in a json object
//        JSONObject jsonObject = new JSONObject(response.body());
//        // get the status of the game
//        Status status = Status.of(jsonObject.getString("status"));
//        System.out.println("status: " + status);
//        System.out.println("polling " + timesPolled);
//        setStatus(status);
//        timesPolled++;
//        if (status == Status.INIT_NEW_GAME) {
//            System.out.println("still waiting");
//        } else if (status == Status.RUNNING) {
//            setStatus(Status.RUNNING);
//            renderBoard();
//        }
//
//
}