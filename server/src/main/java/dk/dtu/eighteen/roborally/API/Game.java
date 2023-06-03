package dk.dtu.eighteen.roborally.API;

import dk.dtu.eighteen.roborally.controller.AppController;

import java.util.*;

public class Game {
    static int IDcounter = 0;
    int ID;
    List<User> users = new ArrayList<>(10);
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
