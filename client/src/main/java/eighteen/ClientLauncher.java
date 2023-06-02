package eighteen;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Stream;

public class ClientLauncher extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/demo_application.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("FXML Welcome");
        stage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("/demo_application.css").toExternalForm());
        stage.show();
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