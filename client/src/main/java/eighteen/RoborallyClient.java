package eighteen;

import eighteen.controller.CallbackBuilder;
import eighteen.controller.WebAppController;
import eighteen.view.RoboRallyMenuBar;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Stream;

public class RoborallyClient extends Application {

    WebAppController webAppController = new WebAppController(this, new CallbackBuilder(this));
    Text status;
    RoboRallyMenuBar menuBar;
    private BorderPane root;
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
        var MIN_APP_HEIGHT = 500;
        stage = primaryStage;

//        AppController appController = new AppController(this);

        // create the primary scene with the a menu bar and a pane for
        // the board view (which initially is empty); it will be filled
        // when the user creates a new game or loads a game
        this.menuBar = new RoboRallyMenuBar(webAppController);
        this.root = new BorderPane();
        this.status = new Text("Status: ");
        VBox vbox = new VBox(menuBar, status, root);
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

    public void setStatus(String status) {
        this.status.setText("status: " + status);
    }
}