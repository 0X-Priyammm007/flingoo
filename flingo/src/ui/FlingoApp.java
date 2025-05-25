package ui;
import flashcards.FlashcardSet;
import flashcards.FlashcardSetDAO;
import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.*;

import java.util.List;

public class FlingoApp extends Application {

    private int currentUserId = 1; // Replace with logged-in user’s ID
    private final FlashcardSetDAO setDAO = new FlashcardSetDAO();
    private GridPane cardGrid;

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) {
        // UI setup
        BorderPane root = new BorderPane();

        // Top bar
        HBox topBar = new HBox();
        Label logo = new Label("FLINGO");
        logo.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        logo.setStyle("-fx-text-fill: #274472;");
        HBox.setHgrow(logo, Priority.ALWAYS);
        Button logoutBtn = new Button("Log Out");
        topBar.getChildren().addAll(logo, logoutBtn);
        topBar.setPadding(new Insets(16, 16, 0, 16));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setSpacing(12);
        root.setTop(topBar);

        // Title
        Label flashcardsLabel = new Label("Flashcards");
        flashcardsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        flashcardsLabel.setPadding(new Insets(16, 0, 16, 16));

        cardGrid = new GridPane();
        cardGrid.setHgap(16);
        cardGrid.setVgap(16);
        cardGrid.setPadding(new Insets(0, 16, 16, 16));
        updateGrid();

        VBox centerBox = new VBox(flashcardsLabel, cardGrid);
        centerBox.setSpacing(8);
        root.setCenter(centerBox);

        Scene scene = new Scene(root, 400, 540);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Flingo Flashcards");
        primaryStage.show();
    }

    private void updateGrid() {
        cardGrid.getChildren().clear();
        List<FlashcardSet> sets = setDAO.getSetsForUser(currentUserId);

        int col = 0, row = 0;
        for (FlashcardSet set : sets) {
            VBox card = createCard(set);
            cardGrid.add(card, col, row);
            col++;
            if (col == 2) { col = 0; row++; }
        }
        // Add plus/new set card
        VBox newSetCard = createNewSetCard();
        cardGrid.add(newSetCard, col, row);
    }

    private VBox createCard(FlashcardSet set) {
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        box.setSpacing(8);
        box.setPrefSize(140, 100);
        box.setStyle(
                "-fx-background-color: #fff;" +
                        "-fx-border-color: #e0e0e0;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(60,60,60,0.09), 2,0,0,2);" +
                        "-fx-cursor: hand;"
        );

        Label titleLabel = new Label(set.setName);
        titleLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 16));
        titleLabel.setWrapText(true);
        titleLabel.setTextAlignment(TextAlignment.CENTER);

        Label subtitleLabel = new Label(set.numCards + " cards");
        subtitleLabel.setFont(Font.font("Arial", 13));
        subtitleLabel.setStyle("-fx-text-fill: #555;");

        HBox actionBar = new HBox(6);
        actionBar.setAlignment(Pos.CENTER);

        Button editBtn = new Button("Edit");
        Button delBtn = new Button("Delete");
        editBtn.setOnAction(e -> showEditSetDialog(set));
        delBtn.setOnAction(e -> {
            setDAO.deleteSet(set.setId);
            updateGrid();
        });

        actionBar.getChildren().addAll(editBtn, delBtn);

        box.getChildren().addAll(titleLabel, subtitleLabel, actionBar);
        box.setOnMouseClicked(e -> {
            // TODO: Open/flash set UI
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Open set: " + set.setName);
            alert.showAndWait();
        });
        return box;
    }

    private VBox createNewSetCard() {
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        box.setSpacing(6);
        box.setPrefSize(140, 90);
        box.setStyle(
                "-fx-background-color: #fff;" +
                        "-fx-border-color: #e0e0e0;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(60,60,60,0.09), 2,0,0,2);" +
                        "-fx-cursor: hand;"
        );
        Label plus = new Label("+\nNew set");
        plus.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 16));
        plus.setTextAlignment(TextAlignment.CENTER);
        box.getChildren().add(plus);
        box.setOnMouseClicked(e -> showNewSetDialog());
        return box;
    }

    private void showNewSetDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Create New Flashcard Set");

        VBox vbox = new VBox(12);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);

        TextField nameField = new TextField();
        nameField.setPromptText("Set Name (e.g. Math)");
        Spinner<Integer> countSpinner = new Spinner<>(1, 999, 10);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        Button addBtn = new Button("Add Set");
        addBtn.setDefaultButton(true);

        addBtn.setOnAction(e -> {
            String setName = nameField.getText().trim();
            int numCards = countSpinner.getValue();
            if (setName.isEmpty()) {
                errorLabel.setText("Name required.");
                return;
            }
            boolean ok = setDAO.addSet(currentUserId, setName, numCards);
            if (ok) {
                updateGrid();
                dialog.close();
            } else {
                errorLabel.setText("Failed to add set.");
            }
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(e -> dialog.close());

        HBox btnBox = new HBox(10, addBtn, cancelBtn);
        btnBox.setAlignment(Pos.CENTER);

        vbox.getChildren().addAll(new Label("Set Name:"), nameField,
                new Label("Number of cards:"), countSpinner, errorLabel, btnBox);

        dialog.setScene(new Scene(vbox, 320, 240));
        dialog.showAndWait();
    }

    private void showEditSetDialog(FlashcardSet set) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Edit Set");

        VBox vbox = new VBox(12);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);

        TextField nameField = new TextField(set.setName);
        Spinner<Integer> countSpinner = new Spinner<>(1, 999, set.numCards);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        Button saveBtn = new Button("Save");
        saveBtn.setDefaultButton(true);

        saveBtn.setOnAction(e -> {
            String setName = nameField.getText().trim();
            int numCards = countSpinner.getValue();
            if (setName.isEmpty()) {
                errorLabel.setText("Name required.");
                return;
            }
            boolean ok = setDAO.updateSet(set.setId, setName, numCards);
            if (ok) {
                updateGrid();
                dialog.close();
            } else {
                errorLabel.setText("Failed to update set.");
            }
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(e -> dialog.close());

        HBox btnBox = new HBox(10, saveBtn, cancelBtn);
        btnBox.setAlignment(Pos.CENTER);

        vbox.getChildren().addAll(new Label("Set Name:"), nameField,
                new Label("Number of cards:"), countSpinner, errorLabel, btnBox);

        dialog.setScene(new Scene(vbox, 320, 240));
        dialog.showAndWait();
    }
}