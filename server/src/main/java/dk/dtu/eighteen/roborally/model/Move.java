package dk.dtu.eighteen.roborally.model;

public class Move {

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

    public CommandCard getCardAtIndex(int index,CommandCardField[] cards) {
        return cards[cardIndex[index]].getCard();
    }

}
