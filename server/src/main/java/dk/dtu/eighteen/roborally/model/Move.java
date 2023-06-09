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

    public static Move parseMoves(String moves) {
        // parse moves
        String[] moveStrings = moves.split(",");

        return null;
    }

    public int[] getCardIndex() {
        return cardIndex;
    }

    public void setCardIndex(int[] cardIndex) {
        for (int i = 0; i < 5; i++) {
            this.cardIndex[i] = cardIndex[i];
        }
    }


    public CommandCard getCardAtIndex(int index, CommandCardField[] cards) {
        return cards[cardIndex[index]].getCard();
    }

    public CommandCardField getCardFieldAtIndex(int index, CommandCardField[] cards) {
        return cards[cardIndex[index]];
    }


    public boolean areValid() {
        if (cardIndex.length != 5)
            return false;
        boolean duplicates = false;
        for (int j = 0; j < cardIndex.length; j++)
            for (int k = j + 1; k < cardIndex.length; k++)
                if (k != j && cardIndex[k] == cardIndex[j]) {
                    duplicates = true;
                    break;
                }

        if (duplicates)
            return false;
        for (int i : cardIndex)
            if (i < 0 || i >= 8)
                return false;
        return true;
    }
}
