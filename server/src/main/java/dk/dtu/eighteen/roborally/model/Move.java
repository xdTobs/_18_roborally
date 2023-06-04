package dk.dtu.eighteen.roborally.model;

public class Move {
    private int cardCounter = 0;
    private final int[] cardIndex = new int[5];

    public Move(int[] cardIndex) {
        for (int i = 0; i < 5; i++) {
            this.cardIndex[i] = cardIndex[i];
        }
    }

    public Move() {
    }

    public int[] getCardIndex() {
        return cardIndex;
    }

    public int getCardAtIndex(int index) {
        return cardIndex[index];
    }

    public int getCardAtCounter(){
        //something out of bounds maybe
        return cardIndex[cardCounter];
    }
    /*
    could be refactored to a single method getCardCounter that also increments and maybe keeps tracke of out of bounds
     */

    public int getCardCounter(){
        return cardCounter;
    }

    public void incrementCardCounter(){
        cardCounter++;
    }
    public void resetCardCounter(){
        cardCounter = 0;
    }
}
