package tn.esprit.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.entities.Feedback;
import tn.esprit.entities.ProjectSubmission;
import tn.esprit.entities.Team;
import tn.esprit.services.FeedbackServices;
import tn.esprit.services.ProjectSubmissionServices;
import tn.esprit.services.TeamServices;

import java.sql.SQLException;
import java.util.List;

public class AddProject {

    // Project Submission Fields
    @FXML private TextField projectIdField;
    @FXML private TextField projectTitleField;
    @FXML private TextField fileLinkField;
    @FXML private TextField teamIdField;
    @FXML private DatePicker submissionDatePicker;
    @FXML private Button submitProjectButton;
    @FXML private Button updateProjectButton;
    @FXML private Button deleteProjectButton;

    private final ProjectSubmissionServices projectSubmissionService = new ProjectSubmissionServices();

    @FXML private ComboBox<Integer> teamIdComboBox; // Declare the missing ComboBox

    @FXML
    private void initialize() {
        try {
            List<Integer> teamIds = projectSubmissionService.getTeamIds();
            ObservableList<Integer> teamIdList = FXCollections.observableArrayList(teamIds);
            teamIdComboBox.setItems(teamIdList);

            if (!teamIdList.isEmpty()) {
                teamIdComboBox.getSelectionModel().selectFirst();
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load team IDs: " + e.getMessage());
        }
    }


    // Handles Project Submission
    @FXML
    private void handleSubmitProject() {
        try {
            String projectTitle = projectTitleField.getText().trim();
            String fileLink = fileLinkField.getText().trim();
            // Get teamId from the ComboBox instead of teamIdField
            Integer teamId = teamIdComboBox.getValue();
            String submissionDate = submissionDatePicker.getValue() != null
                    ? submissionDatePicker.getValue().toString()
                    : "";

            // Validate that all fields are provided
            if (projectTitle.isEmpty() || fileLink.isEmpty() || submissionDate.isEmpty() || teamId == null) {
                showAlert("Error", "All fields are required!");
                return;
            }

            // Create ProjectSubmission entity
            ProjectSubmission projectSubmission = new ProjectSubmission();
            projectSubmission.setTitle(projectTitle);
            projectSubmission.setFileLink(fileLink);
            projectSubmission.setTeamId(teamId);
            projectSubmission.setSubmissionDate(java.sql.Date.valueOf(submissionDate));

            projectSubmissionService.add(projectSubmission);
            showAlert("Success", "Project added successfully!");
            clearProjectForm();
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter valid numbers for Team ID!");
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to save project: " + e.getMessage());
        }
    }


    // Handles Project Update
    @FXML
    private void handleUpdateProject() {
        try {
            int projectId = Integer.parseInt(projectIdField.getText().trim());
            String projectTitle = projectTitleField.getText().trim();
            String fileLink = fileLinkField.getText().trim();
            // Retrieve the Team ID from the ComboBox instead of a TextField
            Integer teamId = teamIdComboBox.getValue();
            String submissionDate = submissionDatePicker.getValue() != null
                    ? submissionDatePicker.getValue().toString()
                    : "";

            // Validate required fields (including that teamId is selected)
            if (projectTitle.isEmpty() || fileLink.isEmpty() || submissionDate.isEmpty() || teamId == null) {
                showAlert("Error", "All fields are required!");
                return;
            }

            // Create and populate the ProjectSubmission entity
            ProjectSubmission projectSubmission = new ProjectSubmission();
            projectSubmission.setSubmissionId(projectId);
            projectSubmission.setTitle(projectTitle);
            projectSubmission.setFileLink(fileLink);
            projectSubmission.setTeamId(teamId);
            projectSubmission.setSubmissionDate(java.sql.Date.valueOf(submissionDate));

            // Perform update operation
            projectSubmissionService.update(projectSubmission);
            showAlert("Success", "Project updated successfully!");
            clearProjectForm();
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid Project ID format!");
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to update project: " + e.getMessage());
        }
    }


    // Handles Project Deletion
    @FXML
    private void handleDeleteProject() {
        try {
            int projectId = Integer.parseInt(projectIdField.getText().trim());
            projectSubmissionService.delete(projectId);
            showAlert("Success", "Project deleted successfully!");
            clearProjectForm();
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid Project ID!");
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to delete project: " + e.getMessage());
        }
    }
    public void handleDisplayAll(javafx.event.ActionEvent actionEvent) {
        ProjectSubmissionServices p = new ProjectSubmissionServices();
        try {
            List<ProjectSubmission> projectSubmissionList = p.getAll();
            if (projectSubmissionList.isEmpty()) {
                showAlert("Info", "No Team found.");
            } else {
                StringBuilder sb = new StringBuilder();
                for (ProjectSubmission projectSubmission : projectSubmissionList) {
                    sb.append(projectSubmission.toString()).append("\n\n");
                }
                TextArea textArea = new TextArea(sb.toString());
                textArea.setEditable(false);
                textArea.setWrapText(true);
                textArea.setPrefWidth(600);
                textArea.setPrefHeight(400);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("All Submissions");
                alert.setHeaderText("Submissions from the Database:");
                alert.getDialogPane().setContent(textArea);
                alert.showAndWait();
            }
        } catch (SQLException e) {
            showAlert("Error", "Failed to retrieve Submissions. " + e.getMessage());
        }
    }

    private void clearProjectForm() {
        projectIdField.clear();
        projectTitleField.clear();
        fileLinkField.clear();
        teamIdField.clear();
        submissionDatePicker.setValue(null);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
