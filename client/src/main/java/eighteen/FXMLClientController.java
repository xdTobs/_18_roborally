package eighteen;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class FXMLClientController {

    private static final String START_URL = "http://localhost:8080/start";
    private static final String BOARD_URL = "http://localhost:8080/board";
    @FXML
    private Text messageField;

    private ResponseEntity<String> getResponseEntity(String url) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForEntity(url, String.class);
    }

    @FXML
    protected void handleNewGame(ActionEvent event) {
        messageField.setText("new game button pressed");
        ResponseEntity<String> response = getResponseEntity(START_URL);
        if (response.getStatusCode().is2xxSuccessful()) {
            String jsonResponse = response.getBody();
            System.out.println(jsonResponse);
            // Process the JSON response or update the game board based on the received information
        }
    }

    public void handleLoadGame(ActionEvent actionEvent) {
        ResponseEntity<String> responseBoard = getResponseEntity(BOARD_URL);
        if (responseBoard.getStatusCode().is2xxSuccessful()) {
            String jsonResponseBoard = responseBoard.getBody();
            System.out.println(jsonResponseBoard);
            // Process the JSON response or update the game board based on the received information
            messageField.setText(jsonResponseBoard);
        } else {

            messageField.setText("Unsuccessful in loading game");
        }
    }
}
