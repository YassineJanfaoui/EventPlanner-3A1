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
import java.util.Map;

public class UpdateFeedbackController {

    @FXML
    private TextArea commentArea;
    @FXML
    private HBox starRatingBox;  // Changed from TextField to HBox
    @FXML
    private ComboBox<String> teamIdComboBox;
    @FXML
    private ComboBox<String> eventIdComboBox;
    @FXML
    private Label sentimentLabel;

    private final FeedbackServices feedbackService = new FeedbackServices();
    private ToggleButton[] stars;  // Star rating buttons
    private int selectedRating = 0; // Store selected rating
    public Feedback currentFeedback;

    @FXML
    public void initialize() {
        try {
            initializeStarRating();

            // Load team names
            Map<String, Integer> teamMap = feedbackService.teamNames();
            ObservableList<String> teamNameList = FXCollections.observableArrayList(teamMap.keySet());
            teamIdComboBox.setItems(teamNameList);

            // Load event names
            Map<String, Integer> eventMap = feedbackService.eventNames();
            ObservableList<String> eventNameList = FXCollections.observableArrayList(eventMap.keySet());
            eventIdComboBox.setItems(eventNameList);

            // Set initial values
            if (currentFeedback != null) {
                commentArea.setText(currentFeedback.getComment());
                setRating(currentFeedback.getRate());
                updateSentimentDisplay(SentimentAnalyzer.analyze(currentFeedback.getComment()));

                // Set team and event selections
                teamIdComboBox.getSelectionModel().select(getKeyFromValue(teamMap, currentFeedback.getTeamId()));
                eventIdComboBox.getSelectionModel().select(getKeyFromValue(eventMap, currentFeedback.getEventId()));
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Initialization Error", "Error loading IDs: " + e.getMessage());
        }

        commentArea.textProperty().addListener((obs, oldVal, newVal) -> {
            String sentiment = SentimentAnalyzer.analyze(newVal);
            updateSentimentDisplay(sentiment);
        });
    }

    private void initializeStarRating() {
        stars = new ToggleButton[5];
        for (int i = 0; i < 5; i++) {
            ToggleButton star = new ToggleButton();
            star.setText("☆");
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
            stars[i].setText(i < rating ? "★" : "☆");
            stars[i].setSelected(i < rating);
        }
    }

    // Helper to get ComboBox selection from ID
    private String getKeyFromValue(Map<String, Integer> map, Integer value) {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    @FXML
    public void handleUpdateFeedback(ActionEvent event) {
        try {
            String comment = commentArea.getText().trim();
            String teamName = teamIdComboBox.getValue();
            String eventName = eventIdComboBox.getValue();

            if (comment.isEmpty() || teamName == null || eventName == null || selectedRating == 0) {
                showAlert(Alert.AlertType.ERROR, "Form Error", "All fields must be completed.");
                return;
            }

            Integer teamId = feedbackService.teamNames().get(teamName);
            Integer eventId = feedbackService.eventNames().get(eventName);

            if (teamId == null || eventId == null) {
                showAlert(Alert.AlertType.ERROR, "Form Error", "Invalid team or event selection.");
                return;
            }

            Feedback feedback = new Feedback(
                    currentFeedback.getFeedbackId(),
                    comment,
                    selectedRating, // Use star rating
                    teamId,
                    eventId
            );

            feedbackService.update(feedback);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Feedback updated successfully.");
            navigateToShowFeedback(event);

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Update Error", "Error updating feedback: " + e.getMessage());
        }
    }

    @FXML
    public void navigateToShowFeedback(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ShowFeedback.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Show Feedbacks");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Error loading feedback list: " + e.getMessage());
        }
    }

    public void setCurrentFeedback(Feedback feedback) {
        this.currentFeedback = feedback;
        commentArea.setText(feedback.getComment());
        setRating(feedback.getRate());
        // Update sentiment display
        updateSentimentDisplay(SentimentAnalyzer.analyze(feedback.getComment()));
    }

    private void updateSentimentDisplay(String sentiment) {
        sentimentLabel.setText("Sentiment: " + sentiment.toUpperCase());
        sentimentLabel.getStyleClass().removeAll("positive", "negative", "neutral");
        sentimentLabel.getStyleClass().add(sentiment.toLowerCase());
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}