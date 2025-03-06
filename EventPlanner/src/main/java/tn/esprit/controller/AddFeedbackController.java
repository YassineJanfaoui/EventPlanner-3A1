package tn.esprit.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import tn.esprit.entities.Feedback;
import tn.esprit.services.FeedbackServices;
import tn.esprit.utils.SentimentAnalyzer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public class AddFeedbackController {

    @FXML private TextArea commentArea;
    @FXML private HBox starRatingBox; // Replace TextField with HBox for stars
    @FXML private ComboBox<String> teamIdComboBox;
    @FXML private ComboBox<String> eventIdComboBox;
    @FXML private Label sentimentLabel;

    private final FeedbackServices feedbackServices = new FeedbackServices();
    private ToggleButton[] stars; // Array to hold star buttons
    private int selectedRating = 0; // Store the selected rating (1-5)

    @FXML
    public void initialize() {
        try {
            Map<String, Integer> teamMap = feedbackServices.teamNames();
            Map<String, Integer> eventMap = feedbackServices.eventNames();

            ObservableList<String> teamNameList = FXCollections.observableArrayList(teamMap.keySet());
            ObservableList<String> eventNameList = FXCollections.observableArrayList(eventMap.keySet());

            teamIdComboBox.setItems(teamNameList);
            eventIdComboBox.setItems(eventNameList);

            if (!teamNameList.isEmpty()) teamIdComboBox.getSelectionModel().selectFirst();
            if (!eventNameList.isEmpty()) eventIdComboBox.getSelectionModel().selectFirst();
        } catch (SQLException e) {
            System.out.println("Error fetching names: " + e.getMessage());
        }

        commentArea.textProperty().addListener((obs, oldVal, newVal) -> {
            String sentiment = SentimentAnalyzer.analyze(newVal);
            updateSentimentDisplay(sentiment);
        });

        // Initialize star rating system
        initializeStarRating();
    }

    private void initializeStarRating() {
        stars = new ToggleButton[5];
        for (int i = 0; i < 5; i++) {
            ToggleButton star = new ToggleButton();
            star.setText("☆"); // Empty star
            star.setStyle("-fx-font-size: 24px; -fx-background-color: transparent;");
            int rating = i + 1;
            star.setOnAction(e -> setRating(rating));
            stars[i] = star;
            starRatingBox.getChildren().add(star);
        }
    }

    private void setRating(int rating) {
        selectedRating = rating;
        for (int i = 0; i < 5; i++) {
            stars[i].setText(i < rating ? "★" : "☆"); // Filled star vs empty
            stars[i].setSelected(i < rating);
        }
    }

    private void updateSentimentDisplay(String sentiment) {
        sentimentLabel.setText("Sentiment: " + sentiment.toUpperCase());
        sentimentLabel.getStyleClass().removeAll("positive", "negative", "neutral");
        sentimentLabel.getStyleClass().add(sentiment.toLowerCase());
    }

    @FXML
    public void handleSubmit(ActionEvent event) {
        try {
            String comment = commentArea.getText().trim();
            String selectedTeamName = teamIdComboBox.getValue();
            String selectedEventName = eventIdComboBox.getValue();

            if (comment.isEmpty() || selectedTeamName == null || selectedEventName == null || selectedRating == 0) {
                showAlert(Alert.AlertType.ERROR, "Form Error", "All fields must be completed, including rating.");
                return;
            }

            Integer teamId = feedbackServices.teamNames().get(selectedTeamName);
            Integer eventId = feedbackServices.eventNames().get(selectedEventName);

            if (teamId == null || eventId == null) {
                showAlert(Alert.AlertType.ERROR, "Form Error", "Invalid selection.");
                return;
            }


            int rate = selectedRating ;
            Feedback feedback = new Feedback(0, comment, rate, teamId, eventId);
            feedbackServices.add(feedback);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Feedback added successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add feedback.");
        }
    }
    @FXML
    public void navigateToShowFeedbacks(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ShowFeedback.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Show Feedbacks");
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}