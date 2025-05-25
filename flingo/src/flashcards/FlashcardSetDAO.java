package flashcards;

import db.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FlashcardSetDAO {

    // Model for returning sets
    public List<FlashcardSet> getSetsForUser(int userId) {
        List<FlashcardSet> sets = new ArrayList<>();
        String sql = "SELECT set_id, set_name, num_cards FROM flashcard_sets WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                sets.add(new FlashcardSet(
                    rs.getInt("set_id"),
                    userId,
                    rs.getString("set_name"),
                    rs.getInt("num_cards")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sets;
    }

    // Add a new set for this user
    public boolean addSet(int userId, String setName, int numCards) {
        String sql = "INSERT INTO flashcard_sets (user_id, set_name, num_cards) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, setName);
            stmt.setInt(3, numCards);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Failed to add set: " + e.getMessage());
            return false;
        }
    }

    // Edit set name or card count
    public boolean updateSet(int setId, String setName, int numCards) {
        String sql = "UPDATE flashcard_sets SET set_name = ?, num_cards = ? WHERE set_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, setName);
            stmt.setInt(2, numCards);
            stmt.setInt(3, setId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Failed to update set: " + e.getMessage());
            return false;
        }
    }

    // Delete a set by ID
    public boolean deleteSet(int setId) {
        String sql = "DELETE FROM flashcard_sets WHERE set_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, setId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Failed to delete set: " + e.getMessage());
            return false;
        }
    }
}