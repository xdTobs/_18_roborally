package dk.dtu.eighteen.roborally.API;

public class User {
    static int IDCounter = 0;
    int ID;

    public User() {
        this.ID = IDCounter++;
    }

    public int getID() {
        return ID;
    }
}
