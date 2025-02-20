package tn.esprit.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.entities.Team;
import tn.esprit.services.TeamServices;

import java.sql.SQLException;
import java.util.List;

public class ManageTeamEventPlanner {

    // --- FXML Fields ---
    @FXML
    private TextField teamIdField;           // TextField for Team ID
    @FXML
    private ComboBox<Integer> eventIdComboBox;  // ComboBox for Event ID
    @FXML
    private TextField scoreField;            // TextField for Score
    @FXML
    private TextField rankField;             // TextField for Rank

    @FXML
    private Button updateTeamButton;
    @FXML
    private Button deleteTeamButton;
    @FXML
    private Button displayAllButton;

    // --- Service initialization ---
    private final TeamServices teamServices = new TeamServices();

    // --- Initialize Method ---
    @FXML
    public void initialize() {
        try {
            // Populate the Event ID ComboBox
            List<Integer> eventIds = teamServices.getEventIds();
            ObservableList<Integer> eventIdList = FXCollections.observableArrayList(eventIds);
            eventIdComboBox.setItems(eventIdList);

            // If you want to auto-select the first event ID:
            if (!eventIdList.isEmpty()) {
                eventIdComboBox.getSelectionModel().selectFirst();
            }

        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load data: " + e.getMessage());
        }
    }

    // --- Update Team ---
    @FXML
    public void updateTeam(ActionEvent event) {
        try {
            String teamIdText = teamIdField.getText().trim();
            Integer eventId = eventIdComboBox.getValue();
            int score = Integer.parseInt(scoreField.getText().trim());
            int rank = Integer.parseInt(rankField.getText().trim());

            if (teamIdText.isEmpty() || eventId == null) {
                showAlert("Error", "Team ID and Event ID are required for update!");
                return;
            }

            int teamId;
            try {
                teamId = Integer.parseInt(teamIdText);
            } catch (NumberFormatException e) {
                showAlert("Error", "Invalid Team ID format.");
                return;
            }

            // Since teamNameField is removed, pass an empty string or a default value
            Team team = new Team(teamId, "", score, rank, eventId);
            teamServices.update(team);
            showAlert("Success", "Team updated successfully!");
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter valid numbers for Score and Rank!");
        }
    }

    // --- Delete Team ---
    @FXML
    public void deleteTeam(ActionEvent event) {
        try {
            // Parse Team ID from the TextField
            int teamId = Integer.parseInt(teamIdField.getText().trim());
            // Delete the team from the database
            Team t = new Team();
            t.setId(teamId);
            teamServices.delete(t);
            showAlert("Success", "Team deleted successfully!");

        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid numeric Team ID!");
        }
    }

    // --- Display All Teams ---
    @FXML
    public void handleDisplayAll(ActionEvent actionEvent) {
        try {
            List<Team> teamList = teamServices.returnList();
            if (teamList.isEmpty()) {
                showAlert("Info", "No Team found.");
            } else {
                StringBuilder sb = new StringBuilder();
                for (Team t : teamList) {
                    sb.append(t.toString()).append("\n\n");
                }
                TextArea textArea = new TextArea(sb.toString());
                textArea.setEditable(false);
                textArea.setWrapText(true);
                textArea.setPrefWidth(600);
                textArea.setPrefHeight(400);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("All Teams");
                alert.setHeaderText("Teams from the Database:");
                alert.getDialogPane().setContent(textArea);
                alert.showAndWait();
            }
        } catch (SQLException e) {
            showAlert("Error", "Failed to load teams: " + e.getMessage());
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
