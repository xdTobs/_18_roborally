

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DemoApplication extends Application {

    private static final String START_URL = "http://localhost:8080/start";
    private static final String BOARD_URL = "http://localhost:8080/board";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Demo Application");

        Button startButton = new Button("Start");
        Button boardButton = new Button("Get Board");

        startButton.setOnAction(event -> {
            ResponseEntity<String> response = getResponseEntity(START_URL);
            if (response.getStatusCode().is2xxSuccessful()) {
                String jsonResponse = response.getBody();
                System.out.println(jsonResponse);
                // Process the JSON response or update the game board based on the received information
            }
        });

        boardButton.setOnAction(event -> {
            ResponseEntity<String> responseBoard = getResponseEntity(BOARD_URL);
            if (responseBoard.getStatusCode().is2xxSuccessful()) {
                String jsonResponseBoard = responseBoard.getBody();
                System.out.println(jsonResponseBoard);
                jsonBuilder(jsonResponseBoard);

                // Process the JSON response or update the game board based on the received information
            }
        });

        VBox root = new VBox(startButton, boardButton);
        primaryStage.setScene(new Scene(root, 500, 500));
        primaryStage.show();
    }

    private ResponseEntity<String> getResponseEntity(String url) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForEntity(url, String.class);
    }

    private void jsonBuilder(String jsonString) {
        String filePath = "client/src/main/resources/jsonBoard.json";
        File file = new File(filePath);
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(jsonString);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}