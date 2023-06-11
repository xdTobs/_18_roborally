package dk.dtu.eighteen;

import dk.dtu.eighteen.controller.ClientController;

import java.util.concurrent.ExecutionException;

public class StartRoborallyClient {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ClientController.main(args);
    }
}