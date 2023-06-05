package dk.dtu.eighteen.roborally.API;

import dk.dtu.eighteen.roborally.controller.AppController;

import java.util.ArrayList;
import java.util.List;

public class Game {
    List<User> users = new ArrayList<>(10);
    public AppController appController;

    public Game(AppController appController) {
        this.appController = appController;
    }

    public List<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        users.add(user);
    }
}
