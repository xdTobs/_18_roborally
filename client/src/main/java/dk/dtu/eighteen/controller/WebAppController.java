package dk.dtu.eighteen.controller;

import dk.dtu.eighteen.roborally.controller.Status;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextInputDialog;
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
import java.util.UUID;

public class WebAppController {
    public String playerName = null;
    RequestController requestController;
    Integer gameId = null;


    public WebAppController(ClientController clientController) {
        this.requestController = new RequestController(clientController);
    }


    String showChoiceDialog(List<String> options, String type) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>(options.get(0), options);
        dialog.setTitle(type + " selector");
        dialog.setHeaderText("Select " + type);
        dialog.setContentText("");
        return dialog.showAndWait().orElseThrow();

    }

    private void nameInputDialog() {
//        TextInputDialog textInputDialog = new TextInputDialog();
//        textInputDialog.setTitle("Name Selector");
//        textInputDialog.setHeaderText("Please enter your name");
//        textInputDialog.setContentText("Name: ");
//
//        var answer = textInputDialog.showAndWait();
//        if (answer.isPresent()) {
//            this.playerName = answer.get();
//        } else {
//            throw new NullPointerException("No name entered");
//        }
        this.playerName = UUID.randomUUID().toString();
    }


    /**
     * This method starts a new game by requesting the name of avaliable boards, and then
     * prompting the user to select the desired gameboard, number of player and name.
     * It sends the choices to the server, which handles the creation of the game.
     *
     * @return
     * @throws IOException
     * @throws URISyntaxException
     * @throws InterruptedException
     */
    public void newGame() throws IOException, URISyntaxException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(new URI("http://localhost:8080/board")).GET().build();
        HttpResponse<String> response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());

        var body = response.body();
        JSONArray jsonArray = new JSONArray(body);
        List<String> boardNameList = new ArrayList<>();
        for (Object a : jsonArray) {
            String s = (String) a;
            boardNameList.add(s);
        }

        nameInputDialog();

        List<String> numPlayerOptions = new ArrayList<>();
        for (int i = 2; i < 7; i++) {
            numPlayerOptions.add(String.valueOf(i));
        }

        //int numberOfPlayers = Integer.parseInt(showChoiceDialog(numPlayerOptions, "number of players"));
        String boardName = showChoiceDialog(boardNameList, "gameboard");
        //String boardName = "a-test-board.json";
        int numberOfPlayers = 2;


//        clientController.setStatusText("You picked the board: " + boardName);

        // Creating a new JSON Object to send playerName, boardName and number of players to server
        JSONObject requestObject = new JSONObject();
        requestObject.put("boardName", boardName);
        requestObject.put("playerName", playerName);
        requestObject.put("playerCapacity", numberOfPlayers);

        request = HttpRequest.newBuilder().uri(new URI("http://localhost:8080/game")).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(requestObject.toString())).build();

        response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());

        this.gameId = Integer.valueOf(response.body());
        requestController.setStatus(Status.INIT_NEW_GAME);

        requestController.createScheduledService(this.playerName);
        this.requestController.startPolling();

    }


    public void stopGame() {
        System.err.println("stop game not implemented");
        Platform.exit();
    }

    /**
     * Sends a request to the server that saves the current game.
     *
     * @throws IOException
     * @throws InterruptedException
     * @throws URISyntaxException
     * @Author Frederik Rolsted, s224299@dtu.dk
     */
    public void saveGame() throws IOException, InterruptedException, URISyntaxException {

        TextInputDialog savedNameInputDialog = new TextInputDialog();
        savedNameInputDialog.setHeaderText("Please enter the name for the savefile");
        savedNameInputDialog.setContentText("Savefile name: ");
        String saveName = savedNameInputDialog.showAndWait().orElse("");

        if (!saveName.isEmpty()) {
            // Creating a new JSON Object to send
            JSONObject requestObject = new JSONObject();
            requestObject.put("saveName", saveName);
            requestObject.put("gameId", gameId);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/game/" + gameId + "/saveGame"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestObject.toString()))
                    .build();

            HttpResponse response = HttpClient.newBuilder()
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Alert savedAlert = new Alert(Alert.AlertType.INFORMATION);
                savedAlert.setTitle("Game Saved");
                savedAlert.setHeaderText(null);
                savedAlert.setContentText("Game saved successfully");
                savedAlert.showAndWait();
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Save Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Failed to save game. Response Code: " + response.statusCode());
                errorAlert.showAndWait();
            }
        }
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

    public void joinGame() throws NullPointerException {
        nameInputDialog();

        TextInputDialog gameIdInputDialog = new TextInputDialog();
        gameIdInputDialog.setTitle("Enter game ID");
        gameIdInputDialog.setHeaderText("Please enter the game ID you want to join");
        gameIdInputDialog.setContentText("Game ID: ");

        var answer = gameIdInputDialog.showAndWait();
        if (answer.isPresent()) {
            this.gameId = Integer.valueOf(answer.get());
        } else {
            throw new NullPointerException("Invalid/no game id entered");
        }
        requestController.createScheduledService(this.playerName);
        this.requestController.startPolling();
    }

    public void finishProgrammingPhase(List<String> cardIds) {
        requestController.postMoves(cardIds);
        requestController.startPolling();
    }

    public void gameOver(String winner) {
        Alert gameOver = new Alert(Alert.AlertType.INFORMATION);
        gameOver.setContentText("Choose how to continue");
        gameOver.setHeaderText("Game has ended, "+winner +" has won");
        gameOver.show();
        System.out.println("here lmao");

    }
}
//
//ObjectMapper objectMapper = new ObjectMapper();
//String requestBody = objectMapper
//        .writerWithDefaultPrettyPrinter()
//        .writeValueAsString(map);
//
//HttpRequest request = HttpRequest.newBuilder(uri)
//        .header("Content-Type", "application/json")
//        .POST(BodyPublishers.ofString(requestBody))
//        .build();
//
//return HttpClient.newHttpClient()
//        .sendAsync(request, BodyHandlers.ofString())
//        .thenApply(HttpResponse::statusCode)
//        .thenAccept(System.out::println);