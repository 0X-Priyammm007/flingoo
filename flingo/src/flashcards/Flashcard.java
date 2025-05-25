package flashcards;

public class Flashcard {
    private int cardId;
    private int setId;
    private String question;
    private String answer;
    private String extra; // Add this field if needed

    public Flashcard(int cardId, int setId, String question, String answer) {
        this.cardId = cardId;
        this.setId = setId;
        this.question = question;
        this.answer = answer;
    }

    // Add this constructor if you want to accept 5 arguments
    public Flashcard(int cardId, int setId, String question, String answer, String extra) {
        this.cardId = cardId;
        this.setId = setId;
        this.question = question;
        this.answer = answer;
        this.extra = extra;
    }
}