package auth;

public class User {
    public int userId;
    public String username;
    public String password;

    public User(int userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    // Add this constructor:
    public User(int userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }
}