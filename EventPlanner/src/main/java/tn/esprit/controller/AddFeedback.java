package tn.esprit.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.entities.Feedback;
import tn.esprit.entities.Team;
import tn.esprit.services.FeedbackServices;
import tn.esprit.services.TeamServices;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class AddFeedback {
    @FXML
    private TextField feedbackIdField;
    @FXML
    private TextArea commentArea;
    @FXML
    private TextField rateField;
    @FXML
    private ComboBox<Integer> teamIdComboBox;
    @FXML
    private ComboBox<Integer> eventIdComboBox;
    @FXML
    private Button submitButton;

    private final FeedbackServices feedbackService = new FeedbackServices();

    @FXML
    private void initialize() {
        try {
            List<Integer> teamIds = feedbackService.getTeamIds(); // Directly get List<Integer>
            ObservableList<Integer> teamIdList = FXCollections.observableArrayList(teamIds);
            teamIdComboBox.setItems(teamIdList);

            if (!teamIdList.isEmpty()) {
                teamIdComboBox.getSelectionModel().selectFirst();
            }


            // Fetch and populate event IDs
            List<Integer> eventIds = feedbackService.getEventIds();
            ObservableList<Integer> eventIdList = FXCollections.observableArrayList(eventIds);
            eventIdComboBox.setItems(eventIdList);
            if (!eventIdList.isEmpty()) {
                eventIdComboBox.getSelectionModel().selectFirst();
            }

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }




    @FXML
    private void handleSubmit() {
        try {
            String comment = commentArea.getText().trim();
            int rate = Integer.parseInt(rateField.getText().trim());
            Integer teamId = teamIdComboBox.getValue();
            Integer eventId = eventIdComboBox.getValue();

            if (comment.isEmpty() || teamId == null || eventId == null) {
                showAlert("Error", "All fields must be filled out!");
                return;
            }
            if (rate < 1 || rate > 10) {
                showAlert("Error", "Rate must be between 1 and 10!");
                return;
            }

            Feedback feedback = new Feedback();
            feedback.setComment(comment);
            feedback.setRate(rate);
            feedback.setTeamId(teamId);
            feedback.setEventId(eventId);

            feedbackService.add(feedback);
            showAlert("Success", "Feedback added successfully!");
            clearForm();

        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid number for Rate!");
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to save feedback: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdate() {
        try {
            // Parse the feedback ID
            int feedbackId = Integer.parseInt(feedbackIdField.getText().trim());

            // Get the values from the input fields
            String comment = commentArea.getText().trim();
            int rate = -1;

            // Ensure rateField is not empty and is a valid number
            try {
                rate = Integer.parseInt(rateField.getText().trim());
                if (rate < 1 || rate > 10) {
                    showAlert("Error", "Rate must be between 1 and 10!");
                    return;
                }
            } catch (NumberFormatException e) {
                if (!rateField.getText().trim().isEmpty()) {
                    showAlert("Error", "Please enter a valid number for Rate!");
                    return;
                }
            }

            Integer teamId = teamIdComboBox.getValue();
            Integer eventId = eventIdComboBox.getValue();

            // Validate that all required fields are filled out
            if (comment.isEmpty() || teamId == null || eventId == null) {
                showAlert("Error", "All fields must be filled out!");
                return;
            }

            // Create Feedback object and set the values
            Feedback feedback = new Feedback();
            feedback.setFeedbackId(feedbackId);
            feedback.setComment(comment);
            feedback.setRate(rate);
            feedback.setTeamId(teamId);
            feedback.setEventId(eventId);

            // Update the feedback
            feedbackService.update(feedback);
            showAlert("Success", "Feedback updated successfully!");
            clearForm();
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid Feedback ID!");
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to update feedback: " + e.getMessage());
        }
    }


    @FXML
    private void handleDelete() {
        try {
            int feedbackId = Integer.parseInt(feedbackIdField.getText().trim());
            feedbackService.delete(feedbackId);
            showAlert("Success", "Feedback deleted successfully!");
            clearForm();
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid Feedback ID!");
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to delete feedback: " + e.getMessage());
        }
    }

    private void clearForm() {
        commentArea.clear();
        rateField.clear();
        teamIdComboBox.getSelectionModel().clearSelection();
        eventIdComboBox.getSelectionModel().clearSelection();
        feedbackIdField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void handleDisplayAll(javafx.event.ActionEvent actionEvent) {
        FeedbackServices feedbackServices = new FeedbackServices();
        try {
            List<Feedback> teamList = feedbackServices.getAll();
            if (teamList.isEmpty()) {
                showAlert("Info", "No Feedbacks found.");
            } else {
                StringBuilder sb = new StringBuilder();
                for (Feedback feedback : teamList) {
                    sb.append(feedback.toString()).append("\n\n");
                }
                TextArea textArea = new TextArea(sb.toString());
                textArea.setEditable(false);
                textArea.setWrapText(true);
                textArea.setPrefWidth(600);
                textArea.setPrefHeight(400);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("All Feedbacks");
                alert.setHeaderText("Feedbacks from the Database:");
                alert.getDialogPane().setContent(textArea);
                alert.showAndWait();
            }
        } catch (SQLException e) {
            showAlert("Error", "Failed to retrieve Feedbacks. " + e.getMessage());
        }
    }

}