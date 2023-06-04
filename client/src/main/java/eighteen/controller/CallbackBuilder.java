package eighteen.controller;

import eighteen.RoborallyClient;
import javafx.application.Platform;

interface ApiResponseCallback {
    void onResponse(String response);
}

public class CallbackBuilder {
    RoborallyClient roborallyClient;

    public CallbackBuilder(RoborallyClient roborallyClient) {
        this.roborallyClient = roborallyClient;
    }

    ApiResponseCallback newGameCallback() {
        return new ApiResponseCallback() {
            @Override
            public void onResponse(String status) {
                System.out.println(status);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        roborallyClient.setStatus(status);
                    }
                });
            }
        };
    }
}
