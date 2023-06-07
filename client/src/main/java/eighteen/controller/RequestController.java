package eighteen.controller;

import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


interface ApiResponseCallback {
    void onResponse(String response);
}

public class RequestController<T> {
    static int timesPolled = 0;
    ClientController clientController;
    ScheduledService<String> scheduledService = new ScheduledService<String>() {

        @Override
        protected Task<String> createTask() {
            return new Task<>() {
                @Override
                protected String call() throws Exception {
                    // Perform the HTTP request
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(new URI("http://localhost:8080/board"))
                            .GET()
                            .build();
                    HttpResponse<String> response = HttpClient.newBuilder()
                            .build()
                            .send(request, HttpResponse.BodyHandlers.ofString());

                    return response.body().toString();
                }
            };
        }
    };

    public RequestController(ClientController roborallyClient) {
        this.clientController = roborallyClient;
    }


    ApiResponseCallback newGameCallback() {
        return new ApiResponseCallback() {
            @Override
            public void onResponse(String status) {
                System.out.println(status);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
//                        clientController.setStatus(status);
                    }
                });
            }
        };
    }

    public void pollGame() {
        scheduledService.setPeriod(Duration.seconds(1));

    }

    public void setStatus(Status status) {
        String s = "";
        System.out.println("STATUS: " + status);
        switch (status) {
            case NOT_INITIATED_GAME -> {
                clientController.setStatusText("Game not running");
            }
            case INIT_NEW_GAME -> {
                s = "Times polled: " + timesPolled + "\n";
                clientController.setStatusText(s + "New game with ID: " + clientController.getGameId() + "\nWaiting for players to join");
            }
            case INIT_LOAD_GAME -> {
                clientController.setStatusText("Loaded game with ID: " + clientController.getGameId());
            }
            case RUNNING -> {
                clientController.setStatusText("Running game with ID: " + clientController.getGameId());
            }
            case QUITTING -> {
                clientController.setStatusText("Quitting and saving game with ID: " + clientController.getGameId());
            }
        }
    }
}

