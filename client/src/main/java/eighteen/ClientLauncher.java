package eighteen;

import eighteen.controller.WebAppController;
import eighteen.view.BoardView;
import eighteen.view.RoboRallyMenuBar;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Stream;

public class ClientLauncher extends Application {
    Text statusText = new Text("Status: start up");
    WebAppController webAppController = new WebAppController(this);

    private BorderPane boardRoot;
    private Stage stage;

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
    }

    public void createBoardView() {
        // if present, remove old BoardView
        boardRoot.getChildren().clear();

        // create and add view for new board
        BoardView boardView = new BoardView();
        boardRoot.setCenter(boardView);

        stage.sizeToScene();
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

}