package dk.dtu.eighteen.controller;

import dk.dtu.eighteen.roborally.controller.Status;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;
import org.apache.tomcat.websocket.WsWebSocketContainer;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

//interface ApiResponseCallback {
//    void onResponse(String response);
//}

public class RequestController {
    static int timesPolled = 0;
    ClientController clientController;
    ScheduledService<String> scheduledService;

    public RequestController(ClientController roborallyClient) {
        this.clientController = roborallyClient;
    }

    public void createScheduledService(String playerName) {
        if (playerName == null) {
            throw new NullPointerException("Player name is null");
        }
        this.scheduledService = new ScheduledService<>() {
            @Override
            protected Task<String> createTask() {
                return new Task<>() {
                    @Override
                    protected String call() throws Exception {

                        timesPolled++;
                        HttpRequest request = HttpRequest.newBuilder().uri(new URI("http://localhost:8080/game/" + clientController.getGameId())).header("roborally-player-name", playerName).GET().build();
                        HttpResponse<String> response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
                        // For debugging
//                        if (timesPolled == 1) {
//                            var req = HttpRequest.newBuilder()
//                                    .uri(new URI("http://localhost:8080/game/" + clientController.getGameId()))
//                                    .header("roborally-player-name", "debug-name")
//                                    .GET()
//                                    .build();
//                            HttpResponse<Void> res = HttpClient.newBuilder()
//                                    .build()
//                                    .send(req, HttpResponse.BodyHandlers.discarding());
//                            return response.body().toString();
//                        }
                        return response.body().toString();
                    }
                };
            }
        };
        scheduledService.setPeriod(Duration.seconds(1));
    }
//


    public void stopPolling() {
        scheduledService.cancel();
    }

    public void startPolling() {
        scheduledService.setOnSucceeded(event1 -> {
            String response = scheduledService.getValue();
            // Process the response
            JSONObject jsonObject = new JSONObject(response);
            Status status = Status.of(jsonObject.get("status").toString());
            setStatus(status);
            if (status == Status.PLAYERS_PROGRAMMING) {
                stopPolling();
                clientController.createBoardView(jsonObject.get("board").toString());
            }
        });

        scheduledService.setOnFailed(event1 -> {
            try {
                throw scheduledService.getException();
            } catch (Throwable e) {
                System.err.println("Error: " + e.getMessage());
                throw new RuntimeException(e);
            }
        });
        scheduledService.reset();
        scheduledService.start();
    }

    public void setStatus(Status status) {
        String s = "";
        switch (status) {
            case NOT_INITIATED_GAME -> clientController.setStatusText("Game not running");
            case INIT_NEW_GAME -> {
                s = "Times polled: " + timesPolled + "\n";
                clientController.setStatusText(s + "New game with ID: " + clientController.getGameId() + "\nWaiting for players to join");
            }
            case INIT_LOAD_GAME ->
                    clientController.setStatusText("Loaded game with ID: " + clientController.getGameId());
            case UPDATING_GAMEBOARD ->
                    clientController.setStatusText("Waiting for all players to get updates from game with ID: " + clientController.getGameId());
            case PLAYERS_PROGRAMMING ->
                    clientController.setStatusText("Running game with ID: " + clientController.getGameId());
            case QUITTING ->
                    clientController.setStatusText("Quitting and saving game with ID: " + clientController.getGameId());
            case INVALID_GAME_ID -> clientController.setStatusText("Invalid game ID");
        }
    }

    public void postMoves(List<String> cardIds) {
        try {
            String requestBody = "[" + String.join(",", cardIds.stream().map(c -> c.toString()).toList()) + "]";
            // Send the request and get the response

            JSONArray jsonArray = new JSONArray(requestBody);
            // Print the response body

            HttpRequest request = HttpRequest.newBuilder().uri(new URI("http://localhost:8080/game/" + clientController.getGameId() + "/moves")).header("Content-Type", "application/json").header("roborally-player-name", clientController.webAppController.playerName).POST(HttpRequest.BodyPublishers.ofString(String.valueOf(jsonArray))).build();
            HttpResponse<String> response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}


