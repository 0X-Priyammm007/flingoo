package flashcards;

public class FlashcardSet {
    public int setId;
    private int userId;
    public String setName;
    public int numCards;

    public FlashcardSet(int setId, int userId, String setName, int numCards) {
        this.setId = setId;
        this.userId = userId;
        this.setName = setName;
        this.numCards = numCards;
    }

    public int getSetId() {
        return setId;
    }

    public void setSetId(int setId) {
        this.setId = setId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSetName() {
        return setName;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }

    public int getNumCards() {
        return numCards;
    }

    public void setNumCards(int numCards) {
        this.numCards = numCards;
    }
}