package dk.dtu.eighteen.controller;

import dk.dtu.eighteen.roborally.controller.Status;
import dk.dtu.eighteen.roborally.fileaccess.LoadBoard;
import dk.dtu.eighteen.roborally.model.Board;
import dk.dtu.eighteen.roborally.model.Phase;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

//interface ApiResponseCallback {
//    void onResponse(String response);
//}

public class RequestController {
    static int timesPolled = 0;
    ClientController clientController;
    ScheduledService<HttpResponse<String>> scheduledService = null;

    Board board = null;

    public RequestController(ClientController roborallyClient) {
        this.clientController = roborallyClient;
    }


    public void createScheduledService(String playerName) {
        if (scheduledService != null) {
            return;
        }
        if (playerName == null) {
            throw new NullPointerException("Player-name is null");
        }
        this.scheduledService = new ScheduledService<>() {
            @Override
            protected Task<HttpResponse<String>> createTask() {
                return new Task<>() {
                    @Override
                    protected HttpResponse<String> call() throws Exception {

                        timesPolled++;
                        HttpRequest request = HttpRequest.newBuilder().uri(new URI("http://localhost:8080/game/" + clientController.getGameId())).header("roborally-player-name", playerName).GET().build();
                        HttpResponse<String> response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
                        System.out.println(response);
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
                        return response;
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
            HttpResponse<String> response = scheduledService.getValue();
            // Process the response
            JSONObject jsonObject = new JSONObject(response.body());
            int responseCode = response.statusCode();

            if (responseCode < 200 || responseCode >= 300) {
                return;
            }

            Status gameStatus = Status.of(jsonObject.get("gameStatus").toString());
            System.out.println("polling" + clientController.webAppController.playerName);
            clientController.setStatus(gameStatus);
            if (gameStatus == Status.INTERACTIVE) {
                stopPolling();
                System.out.println("Running interactive action");
                JSONArray options = (JSONArray) jsonObject.get("options");
                System.out.println(options);
                List<String> optionsList = new ArrayList<>();
                for (int i = 0; i < options.length(); i++) {
                    optionsList.add(options.get(i).toString());
                }
                String move = clientController.webAppController.showChoiceDialog(optionsList, "Interactive move");
//                try {
////                    HttpRequest request = HttpRequest.newBuilder().
////                            uri(new URI("http://localhost:8080/game/" + clientController.getGameId() + "/moves/" + move)).
////                            header("roborally-player-name", clientController.webAppController.playerName).POST().build();
////                    HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.discarding());
//                } catch (URISyntaxException | IOException | InterruptedException e) {
//                    throw new RuntimeException(e);
//                }

                return;
            }

            if (gameStatus == Status.GAMEOVER) {
                clientController.webAppController.gameOver(jsonObject.get("winner").toString());
                return;
            }
            if (gameStatus == Status.RUNNING) {
                String json = jsonObject.get("board").toString();
                Board board = null;
                try {
                    board = LoadBoard.loadBoardFromJSONString(json);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (this.board == null || this.board.turn != board.turn) {
                    stopPolling();
                    this.board = board;
                    clientController.createBoardView(board);
                }
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


