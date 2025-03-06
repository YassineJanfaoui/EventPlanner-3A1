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
import tn.esprit.entities.ProjectSubmission;
import tn.esprit.services.ProjectSubmissionServices;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

public class UpdateProjectController {

    @FXML
    private TextField titleField;
    @FXML
    private TextField fileLinkField;
    @FXML
    private DatePicker submissionDatePicker;
    @FXML
    private ComboBox<String> teamIdComboBox;

    private final ProjectSubmissionServices projectService = new ProjectSubmissionServices();
    private ProjectSubmission currentProject;

    @FXML
    public void initialize() {
        try {
            // Load team names for the ComboBox
            Map<String, Integer> teamMap = projectService.teamNames();
            ObservableList<String> teamNameList = FXCollections.observableArrayList(teamMap.keySet());
            teamIdComboBox.setItems(teamNameList);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Initialization Error", "Error loading team names: " + e.getMessage());
        }
    }

    @FXML
    public void handleUpdateProject(ActionEvent event) {
        try {
            String title = titleField.getText().trim();
            String fileLink = fileLinkField.getText().trim();
            Date submissionDate = java.sql.Date.valueOf(submissionDatePicker.getValue());
            String teamName = teamIdComboBox.getValue();

            if (title.isEmpty() || fileLink.isEmpty() || teamName == null) {
                showAlert(Alert.AlertType.ERROR, "Form Error", "All fields must be completed.");
                return;
            }

            // Get the team ID from the selected team name
            Integer teamId = projectService.teamNames().get(teamName);

            if (teamId == null) {
                showAlert(Alert.AlertType.ERROR, "Form Error", "Invalid team selection.");
                return;
            }

            // Create a new ProjectSubmission object with the updated values
            ProjectSubmission project = new ProjectSubmission(
                    currentProject.getSubmissionId(),
                    title,
                    fileLink,
                    submissionDate,
                    teamId
            );

            // Update the project using the service
            projectService.update(project);

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Success", "Project updated successfully.");

            // Navigate back to the projects list view
            navigateToShowProjects(event);

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Update Error", "Error updating project: " + e.getMessage());
        }
    }

    @FXML
    public void navigateToShowProjects(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ShowProject.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Show Projects");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Error loading project list: " + e.getMessage());
        }
    }

    public void setCurrentProject(ProjectSubmission project) {
        this.currentProject = project;
        titleField.setText(project.getTitle());
        fileLinkField.setText(project.getFileLink());
        submissionDatePicker.setValue(((java.sql.Date) project.getSubmissionDate()).toLocalDate());

        // Set the selected team name based on the current project's team ID
        try {
            Map<String, Integer> teamMap = projectService.teamNames();
            String teamName = getKeyFromValue(teamMap, project.getTeamId());
            teamIdComboBox.getSelectionModel().select(teamName);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error setting team name: " + e.getMessage());
        }
    }

    // Helper method to get the key (team name) from the value (team ID) in a map
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