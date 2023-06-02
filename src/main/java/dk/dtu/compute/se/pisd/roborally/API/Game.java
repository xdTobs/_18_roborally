package dk.dtu.compute.se.pisd.roborally.API;

import dk.dtu.compute.se.pisd.roborally.controller.AppController;

import java.util.ArrayList;
import java.util.List;

public class Game {
    static int IDcounter = 0;
    int ID;
    List<User> users = new ArrayList<>();
    AppController appController;

    public Game(AppController appController) {
        this.ID = IDcounter++;
        this.appController = appController;
    }

    public List<User> getUsers() {
        return users;
    }
    public void addUser(User user){
        users.add(user);
    }
}
