package dk.dtu.eighteen.controller;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
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
import java.util.Arrays;
import java.util.List;

/**
 * This class is the controller for interaction with a client and player.
 * @author Henrik Zenkert
 */
public class WebAppController {
    public String playerName = null;
    RequestController requestController;
    Integer gameId = null;

    private boolean isGameRunning = false;

    public WebAppController(ClientController clientController) {
        this.requestController = new RequestController(clientController);
        nameInputDialog();
        requestController.createScheduledService(this.playerName);
    }

    String showChoiceDialog(List<String> options, String type) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>(options.get(0), options);
        dialog.setTitle(type + " selector");
        dialog.setHeaderText("Select " + type);
        dialog.setContentText("");
        return dialog.showAndWait().orElseThrow();

    }

    private void nameInputDialog() {
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("Name Selector");
        textInputDialog.setHeaderText("Please enter your name");
        textInputDialog.setContentText("Name: ");

        var answer = textInputDialog.showAndWait();
        if (answer.isPresent()) {
            this.playerName = answer.get();
        } else {
            throw new NullPointerException("No name entered");
        }
        // this.playerName = UUID.randomUUID().toString();
    }

    /**
     * This method starts a new game by requesting the name of available boards, and
     * then
     * prompting the user to select the desired gameboard, number of players and
     * name.
     * It sends the choices to the server, which handles the creation of the game.
     *
     * @throws IOException if the server is not running
     */
    public void newGame() throws IOException, URISyntaxException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(new URI("http://localhost:8080/board")).GET().build();
        HttpResponse<String> response = HttpClient.newBuilder().build().send(request,
                HttpResponse.BodyHandlers.ofString());

        var body = response.body();
        JSONArray jsonArray = new JSONArray(body);
        List<String> boardNameList = new ArrayList<>();
        for (Object a : jsonArray) {
            String s = (String) a;
            boardNameList.add(s);
        }

        List<String> numPlayerOptions = new ArrayList<>();
        for (int i = 2; i < 7; i++) {
            numPlayerOptions.add(String.valueOf(i));
        }

        int numberOfPlayers = Integer.parseInt(showChoiceDialog(numPlayerOptions, "number of players"));
        String boardName = showChoiceDialog(boardNameList, "gameboard");
        // String boardName = "a-test-board.json";
        // int numberOfPlayers = 2;

        // clientController.setStatusText("You picked the board: " + boardName);

        // Creating a new JSON Object to send playerName, boardName and number of
        // players to server
        JSONObject requestObject = new JSONObject();
        requestObject.put("boardName", boardName);
        requestObject.put("playerName", playerName);
        requestObject.put("playerCapacity", numberOfPlayers);

        request = HttpRequest.newBuilder().uri(new URI("http://localhost:8080/game"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestObject.toString())).build();

        response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());

        this.gameId = Integer.valueOf(response.body());
        isGameRunning = true;

        this.requestController.startPolling();
    }

    public void stopGame() throws IOException, URISyntaxException, InterruptedException {
        this.requestController.stopPolling();
        System.err.println("stop game not implemented");
        List<String> dialogOptions = Arrays.asList("Yes", "No");
        String answer = showChoiceDialog(dialogOptions, "yes if you want to save the game");

        if (answer.equals("Yes")) {
            saveGame();
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/game/" + gameId))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();

        HttpResponse<String> response = HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        Platform.exit();
    }

    /**
     * Sends a request to the server that saves the current game.
     *
     * @author Frederik Rolsted, s224299@dtu.dk
     */
    public void saveGame() throws IOException, InterruptedException, URISyntaxException {

        TextInputDialog savedNameInputDialog = new TextInputDialog();
        savedNameInputDialog.setHeaderText("Please enter the name for the savefile");
        savedNameInputDialog.setContentText("Savefile name: ");
        String saveName = savedNameInputDialog.showAndWait().orElse("");

        if (!saveName.isEmpty()) {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/game/" + gameId))
                    .header("Content-Type", "application/json")
                    .header("roborally-save-name", saveName)
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = HttpClient.newBuilder()
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 || response.statusCode() < 300) {
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
        try {
            TextInputDialog gameIdInputDialog = new TextInputDialog();
            gameIdInputDialog.setTitle("Save name");
            gameIdInputDialog.setHeaderText("Please enter the name of the save you want to load");
            gameIdInputDialog.setContentText("Save: ");

            var saveOptional = gameIdInputDialog.showAndWait();
            if (saveOptional.isPresent()) {
                this.gameId = requestController.loadGame(playerName, saveOptional.get());
                this.requestController.startPolling();
            }

        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void exit() {
        Platform.exit();
    }

    public boolean isGameRunning() {
        return this.isGameRunning;
    }

    public void joinGame() throws NullPointerException {

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
        this.requestController.startPolling();
    }

    public void finishProgrammingPhase(List<String> cardIds) {
        requestController.postMoves(cardIds);
        requestController.startPolling();
    }

}