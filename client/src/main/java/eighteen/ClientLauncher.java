package eighteen;

import eighteen.controller.WebAppController;
import eighteen.view.BoardView;
import eighteen.view.RoboRallyMenuBar;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Status;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Stream;

public class ClientLauncher extends Application {

    WebAppController webAppController = new WebAppController();
    private BorderPane boardRoot;
    private Stage stage;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
//        Parent root = FXMLLoader.load(getClass().getResource("/demo_application.fxml"));
//        Scene scene = new Scene(root);
//        stage.setTitle("FXML Welcome");
//        stage.setScene(scene);
//        scene.getStylesheets().add(getClass().getResource("/demo_application.css").toExternalForm());
//        stage.show();
        var MIN_APP_WIDTH = 800;
        var MIN_APP_HEIGHT = 800;
        stage = primaryStage;

//        AppController appController = new AppController(this);

        // create the primary scene with the a menu bar and a pane for
        // the board view (which initially is empty); it will be filled
        // when the user creates a new game or loads a game
        RoboRallyMenuBar menuBar = new RoboRallyMenuBar(webAppController);
        boardRoot = new BorderPane();
        VBox vbox = new VBox(menuBar, boardRoot);
        vbox.setMinWidth(MIN_APP_WIDTH);
        vbox.setMinHeight(MIN_APP_HEIGHT);
        Scene primaryScene = new Scene(vbox);

        stage.setScene(primaryScene);
        stage.setTitle("RoboRally");
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

}