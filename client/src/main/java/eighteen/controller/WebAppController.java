package eighteen.controller;

import eighteen.RoborallyClient;
import javafx.application.Platform;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class WebAppController {

    private final RoborallyClient roborallyClient;

    public WebAppController(RoborallyClient roborallyClient) {
        this.roborallyClient = roborallyClient;
    }

    public void newGame() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/board"))
                    .GET()
                    .build();
            HttpResponse<String> response = HttpClient.newBuilder()
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());
            roborallyClient.setStatus(response.body());
        } catch (InterruptedException | URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }

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

