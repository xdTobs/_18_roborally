package dk.dtu.eighteen.controller;

import dk.dtu.eighteen.roborally.controller.Status;
import dk.dtu.eighteen.roborally.model.Board;
import dk.dtu.eighteen.view.BoardView;
import dk.dtu.eighteen.view.RoboRallyMenuBar;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * This class is the main controller for the client application.
 * @author Henrik Zenkert, s224281@dtu.dk
 */
public class ClientController extends Application {
    Text statusText = new Text("Status: start up");
    WebAppController webAppController = new WebAppController(this);
    private BorderPane boardRoot;
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        var MIN_APP_WIDTH = 800;
        var MIN_APP_HEIGHT = 1000;
        RoboRallyMenuBar menuBar = new RoboRallyMenuBar(webAppController);
        boardRoot = new BorderPane(statusText);
        BorderPane.setAlignment(statusText, Pos.BOTTOM_CENTER);
        VBox vbox = new VBox(menuBar, boardRoot);
        vbox.setMinWidth(MIN_APP_WIDTH);
        vbox.setMinHeight(MIN_APP_HEIGHT);
        Scene primaryScene = new Scene(vbox);

        primaryStage.setScene(primaryScene);
        primaryStage.setTitle("Roborally");
        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            webAppController.exit();
        });
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    public void setStatus(Status gameStatus) {
        switch (gameStatus) {
            case NOT_INITIATED_GAME -> setStatusText("Game not running");
            case INIT_NEW_GAME -> setStatusText("New game with ID: " + getGameId() + "\nWaiting for players to join");
            case INIT_LOAD_GAME -> setStatusText("Loaded game with ID: " + getGameId());
            case RUNNING -> setStatusText("Running game with ID: " + getGameId());
            case INTERACTIVE -> setStatusText(
                    "Awaiting input from " + webAppController.playerName + " in game with ID: " + getGameId());
            case QUITTING -> setStatusText("Quitting and saving game with ID: " + getGameId());
            case INVALID_GAME_ID -> setStatusText("Invalid game ID");
            case GAMEOVER -> setStatusText("GAME OVER");
        }
    }

    public void createBoardView(Board board) {
        // if present, remove old BoardView
        boardRoot.getChildren().clear();
        BoardView boardView = new BoardView(webAppController, board);

        boardRoot.setCenter(boardView);
    }

    public void setStatusText(String s) {
        this.statusText.setText("Status: " + s);
    }

    public int getGameId() {
        return webAppController.gameId;
    }

    public void gameOver(String winner) {
        Alert gameOver = new Alert(Alert.AlertType.INFORMATION);
        gameOver.setContentText("Press OK to end game");
        gameOver.setHeaderText("Game has ended, " + winner + " has won");
        gameOver.showAndWait();
    }

}