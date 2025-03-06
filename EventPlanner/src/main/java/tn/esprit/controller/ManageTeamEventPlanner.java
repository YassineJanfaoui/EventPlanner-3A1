package tn.esprit.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.entities.Team;
import tn.esprit.services.TeamServices;

import java.sql.SQLException;
import java.util.Map;

public class ManageTeamEventPlanner {

    // --- FXML Fields ---
    @FXML
    private ComboBox<String> teamnameComboBox;
    @FXML
    private ComboBox<String> eventnameComboBox;
    @FXML
    private TextField scoreField;
    @FXML
    private TextField rankField;
    @FXML
    private Button updateTeamButton;

    // --- Service and Data Storage ---
    private final TeamServices teamServices = new TeamServices();
    private Map<String, Integer> teamNamesMap;
    private Map<String, Integer> eventNamesMap;

    // --- Initialize Method ---
    @FXML
    public void initialize() {
        try {
            // Load team and event names
            teamNamesMap = teamServices.teamNames();
            eventNamesMap = teamServices.eventNames();

            // Populate ComboBoxes
            ObservableList<String> teamNames = FXCollections.observableArrayList(teamNamesMap.keySet());
            ObservableList<String> eventNames = FXCollections.observableArrayList(eventNamesMap.keySet());

            teamnameComboBox.setItems(teamNames);
            eventnameComboBox.setItems(eventNames);

            // Select first items if available
            if (!teamNames.isEmpty()) teamnameComboBox.getSelectionModel().selectFirst();
            if (!eventNames.isEmpty()) eventnameComboBox.getSelectionModel().selectFirst();

        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load data: " + e.getMessage());
        }
    }

    // --- Update Team ---
    @FXML
    public void updateTeam(ActionEvent event) {
        try {
            // Get selected values
            String selectedTeam = teamnameComboBox.getValue();
            String selectedEvent = eventnameComboBox.getValue();

            // Validate selections
            if (selectedTeam == null || selectedEvent == null) {
                showAlert("Error", "Please select both a team and an event!");
                return;
            }

            // Get IDs from maps
            Integer teamId = teamNamesMap.get(selectedTeam);
            Integer eventId = eventNamesMap.get(selectedEvent);

            // Validate numeric fields
            int score = Integer.parseInt(scoreField.getText().trim());
            int rank = Integer.parseInt(rankField.getText().trim());

            // Create updated Team object
            Team team = new Team(
                    teamId,
                    selectedTeam,  // Team name from ComboBox
                    score,
                    rank,
                    eventId
            );

            // Perform update
            teamServices.update(team);
            showAlert("Success", "Team updated successfully!");

        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter valid numbers for Score and Rank!");
        }
    }

    // --- Helper Method: showAlert ---
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}