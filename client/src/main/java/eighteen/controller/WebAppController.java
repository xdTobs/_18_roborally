package eighteen.controller;

import javafx.application.Platform;
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
import java.util.List;

public class WebAppController {
    public final RequestController requestController;
    public String playerName = null;
    Integer gameId = null;


    public WebAppController(ClientController clientController, RequestController requestController) {
        this.requestController = requestController;
    }


    private String dialogChoice(List<String> options, String type) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>(options.get(0), options);
        dialog.setTitle(type + " selector");
        dialog.setHeaderText("Select " + type);
        dialog.setContentText("");
        return dialog.showAndWait().orElseThrow();

    }

    private String nameInputDialog() {
        if (true) {
            return "henrik";
        }
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("Name Selector");
        textInputDialog.setHeaderText("Please enter your name");
        textInputDialog.setContentText("Name: ");

        try {
            var answer = textInputDialog.showAndWait();
            if (answer.isPresent()) {
                return answer.get();
            } else {
                throw new NullPointerException("No name entered");
            }
        } catch (Exception e) {
            System.err.println("You didn't pick a name so your name is default name.\nError message: " + e);
            return "default name";
        }
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
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/board"))
                .GET()
                .build();
        HttpResponse<String> response = HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());

        var body = response.body();
        JSONArray jsonArray = new JSONArray(body);
        List<String> boardNameList = new ArrayList<>();
        for (Object a : jsonArray) {
            String s = (String) a;
            boardNameList.add(s);
        }

        this.playerName = nameInputDialog();

        List<String> numPlayerOptions = new ArrayList<>();
        for (int i = 2; i < 7; i++) {
            numPlayerOptions.add(String.valueOf(i));
        }

//        int numberOfPlayers = Integer.parseInt(dialogChoice(numPlayerOptions, "number of players"));
//
//        String boardName = dialogChoice(boardNameList, "gameboard");

        int numberOfPlayers = 2;
        String boardName = "a-test-board.json";

//        clientController.setStatusText("You picked the board: " + boardName);

        // Creating a new JSON Object to send playerName, boardName and number of players to server
        JSONObject requestObject = new JSONObject();
        requestObject.put("boardName", boardName);
        requestObject.put("playerName", playerName);
        requestObject.put("playerCapacity", numberOfPlayers);

        request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/game"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestObject.toString()))
                .build();

        response = HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());

        this.gameId = Integer.valueOf(response.body());
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