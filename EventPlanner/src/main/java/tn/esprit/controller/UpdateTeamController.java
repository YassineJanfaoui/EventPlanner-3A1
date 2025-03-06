package tn.esprit.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import tn.esprit.entities.Team;
import tn.esprit.services.TeamServices;

import java.io.IOException;
import java.util.Map;

public class UpdateTeamController {

    @FXML
    private TextField teamNameField;
    @FXML
    private ComboBox<String> eventNameComboBox; // Ensure this matches the fx:id in FXML
    @FXML
    private Button updateButton;

    private final TeamServices teamService = new TeamServices();
    private Team currentTeam;

    @FXML
    public void initialize() {
        try {
            // Debugging: Check if eventNameComboBox is null
            if (eventNameComboBox == null) {
                System.out.println("eventNameComboBox is null!");
            } else {
                System.out.println("eventNameComboBox is initialized.");
            }

            // Load event names for the ComboBox
            Map<String, Integer> eventMap = teamService.eventNames(); // Fetch event names and IDs
            ObservableList<String> eventNameList = FXCollections.observableArrayList(eventMap.keySet());
            eventNameComboBox.setItems(eventNameList);

            // Set initial values if currentTeam is already set
            if (currentTeam != null) {
                teamNameField.setText(currentTeam.getTeamName());
                // Set the selected event name based on the current team's event ID
                String eventName = getKeyFromValue(eventMap, currentTeam.getEventId());
                eventNameComboBox.getSelectionModel().select(eventName);
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Initialization Error", "Error loading event names: " + e.getMessage());
        }
    }

    @FXML
    public void handleUpdateTeam(ActionEvent event) {
        try {
            // Get the form input values
            String teamName = teamNameField.getText().trim();
            String eventName = eventNameComboBox.getValue();

            // Check for empty fields
            if (teamName.isEmpty() || eventName == null) {
                showAlert(Alert.AlertType.ERROR, "Form Error", "All fields must be completed.");
                return;
            }

            // Get the event ID from the selected event name
            Map<String, Integer> eventMap = teamService.eventNames();
            Integer eventId = eventMap.get(eventName);

            if (eventId == null) {
                showAlert(Alert.AlertType.ERROR, "Form Error", "Invalid event selection.");
                return;
            }

            // Create a new Team object with the updated values
            Team updatedTeam = new Team(currentTeam.getId(), teamName, currentTeam.getScore(), currentTeam.getRank(), eventId);

            // Update the team using the service
            teamService.update(updatedTeam);

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Success", "Team updated successfully.");

            // Optionally, close the window after update
            navigateToShowTeam(event);

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Update Error", "Error updating team: " + e.getMessage());
        }
    }

    @FXML
    public void navigateToShowTeam(ActionEvent event) {
        try {
            // Navigate back to the teams list view
            Parent root = FXMLLoader.load(getClass().getResource("/ShowTeam.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Show Teams");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Error navigating to Show Teams: " + e.getMessage());
        }
    }

    public void setCurrentTeam(Team team) {
        this.currentTeam = team;
        teamNameField.setText(team.getTeamName());

        // Set the selected event name based on the current team's event ID
        try {
            Map<String, Integer> eventMap = teamService.eventNames();
            String eventName = getKeyFromValue(eventMap, team.getEventId());
            eventNameComboBox.getSelectionModel().select(eventName);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error setting event name: " + e.getMessage());
        }
    }

    // Helper method to get the key (event name) from the value (event ID) in a map
    private String getKeyFromValue(Map<String, Integer> map, Integer value) {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}