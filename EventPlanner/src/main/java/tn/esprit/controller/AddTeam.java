package tn.esprit.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.entities.Team;
import tn.esprit.services.TeamServices;

import java.sql.SQLException;
import java.util.List;

public class AddTeam {

    @FXML
    private TextField teamIdField;  // Field to input Team ID for update/delete

    @FXML
    private TextField teamNameField;

    @FXML
    private ComboBox<Integer> eventIdComboBox;  // Change eventIdField to ComboBox

    @FXML
    private Button createTeamButton;

    @FXML
    private Button updateTeamButton;

    @FXML
    private Button deleteTeamButton;

    private TeamServices teamServices;

    private TeamServices teamService = new TeamServices();

    @FXML
    public void initialize() {
        try {
            eventIdComboBox.getItems().addAll(teamService.getEventIds());
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    // Initialize TeamServices
    public AddTeam() {
        teamServices = new TeamServices();
    }

    // Method to handle Create Team operation (unchanged)
    @FXML
    public void createTeam(ActionEvent event) {
        try {
            String teamName = teamNameField.getText().trim();
            Integer eventId = eventIdComboBox.getValue();  // Get the selected Event ID from ComboBox

            // Validate inputs
            if (teamName.isEmpty() || eventId == null) {
                showAlert("Error", "Team Name and Event ID must be filled out!");
                return;
            }

            // Create Team object (Score and Rank set to 0 by default)
            Team team = new Team(teamName, 0, 0, eventId);

            // Add Team to the database
            teamServices.add(team);
            showAlert("Success", "Team successfully created!");
        } catch (SQLException e) {
            showAlert("Error", "Failed to create team. " + e.getMessage());
        }
    }

    // Working Update Team method using Team ID from teamIdField
    @FXML
    public void updateTeam(ActionEvent event) {
        String teamIdText = teamIdField.getText().trim();
        String teamName = teamNameField.getText().trim();
        Integer eventId = eventIdComboBox.getValue();  // Get the selected Event ID from ComboBox

        // Validate inputs
        if (teamIdText.isEmpty() || teamName.isEmpty() || eventId == null) {
            showAlert("Error", "Team ID, Team Name, and Event ID are required for update!");
            return;
        }

        // Parse teamId
        int teamId;
        try {
            teamId = Integer.parseInt(teamIdText);
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid Team ID format.");
            return;
        }

        // Create updated Team object (Score and Rank remain 0 by default)
        Team team = new Team(teamId, teamName, 0, 0, eventId);

        // Update Team in the database
        teamServices.update(team);
        showAlert("Success", "Team successfully updated!");
    }

    // Working Delete Team method using Team ID from teamIdField
    @FXML
    public void deleteTeam(ActionEvent event) {

        String teamIdText = teamIdField.getText().trim();

        // Validate input
        if (teamIdText.isEmpty()) {
            showAlert("Error", "Please enter the Team ID to delete.");
            return;
        }

        // Parse teamId
        int teamId;
        try {
            teamId = Integer.parseInt(teamIdText);
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid Team ID. Please enter a valid number.");
            return;
        }

        // Delete the team from the database using the team ID
        Team t = new Team();
        t.setId(teamId);
        teamServices.delete(t);
        showAlert("Success", "Team successfully deleted!");
    }

    // Helper method to display alert messages
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void handleDisplayAll(javafx.event.ActionEvent actionEvent) {
        TeamServices team = new TeamServices();
        try {
            List<Team> teamList = team.returnList();
            if (teamList.isEmpty()) {
                showAlert("Info", "No Team found.");
            } else {
                StringBuilder sb = new StringBuilder();
                for (Team f : teamList) {
                    sb.append(f.toString()).append("\n\n");
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
            showAlert("Error", "Failed to retrieve feedbacks. " + e.getMessage());
        }
    }
}
