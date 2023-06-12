package dk.dtu.eighteen.controller;

import dk.dtu.eighteen.roborally.controller.Status;
import dk.dtu.eighteen.roborally.fileaccess.LoadBoard;
import dk.dtu.eighteen.roborally.model.Board;
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
        var MIN_APP_WIDTH = 800;
        var MIN_APP_HEIGHT = 1000;
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
        stage.setOnCloseRequest(e -> {
            e.consume();
            webAppController.exit();
        });
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();
    }

    public void setStatus(Status gameStatus) {
        switch (gameStatus) {
            case NOT_INITIATED_GAME -> setStatusText("Game not running");
            case INIT_NEW_GAME -> setStatusText("New game with ID: " + getGameId() + "\nWaiting for players to join");
            case INIT_LOAD_GAME -> setStatusText("Loaded game with ID: " + getGameId());
            case RUNNING -> setStatusText("Running game with ID: " + getGameId());
            case INTERACTIVE ->
                    setStatusText("Awaiting input from " + webAppController.playerName + " in game with ID: " + getGameId());
            case QUITTING -> setStatusText("Quitting and saving game with ID: " + getGameId());
            case INVALID_GAME_ID -> setStatusText("Invalid game ID");
        }
    }

    public void createBoardView(Board board) {
        // if present, remove old BoardView
        boardRoot.getChildren().clear();
        // TODO find a way to show the board and make it interactive
        BoardView boardView = new BoardView(webAppController, board);

        boardRoot.setCenter(boardView);
    }

    public void setStatusText(String s) {
        this.statusText.setText("Status: " + s);
    }

    public int getGameId() {
        return webAppController.gameId;
    }


//    public void setStatus(Status status) {
//        String s = "";
//        
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
//        
//        
//        setStatus(status);
//        timesPolled++;
//        if (status == Status.INIT_NEW_GAME) {
//            
//        } else if (status == Status.RUNNING) {
//            setStatus(Status.RUNNING);
//            renderBoard();
//        }
//
//
}