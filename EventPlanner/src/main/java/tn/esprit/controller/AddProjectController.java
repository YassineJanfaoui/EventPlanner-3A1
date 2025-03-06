package tn.esprit.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.entities.ProjectSubmission;
import tn.esprit.services.ProjectSubmissionServices;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Date;
import java.util.Map;

public class AddProjectController {

    @FXML private TextField projectIdField;
    @FXML private TextField projectTitleField;
    @FXML private TextField fileLinkField;
    @FXML private ComboBox<String> teamIdComboBox;
    @FXML private DatePicker submissionDatePicker;

    private final ProjectSubmissionServices projectService = new ProjectSubmissionServices();

    @FXML
    public void initialize() {
        try {
            Map<String, Integer> teamMap = projectService.teamNames();
            ObservableList<String> teamNameList = FXCollections.observableArrayList(teamMap.keySet());
            teamIdComboBox.setItems(teamNameList);

            if (!teamNameList.isEmpty()) teamIdComboBox.getSelectionModel().selectFirst();

        } catch (SQLException e) {
            System.out.println("Error fetching names: " + e.getMessage());
        }
    }

    @FXML
    public void handleSubmitProject(ActionEvent event) {
        try {
            String title = projectTitleField.getText().trim();
            String fileLink = fileLinkField.getText().trim();
            String teamId = teamIdComboBox.getValue();
            java.util.Date submissionDate = java.sql.Date.valueOf(submissionDatePicker.getValue());

            if (title.isEmpty() || fileLink.isEmpty() || teamId == null ) {
                showAlert(Alert.AlertType.ERROR, "Form Error", "All fields must be completed");
                return;
            }

            ProjectSubmission project = new ProjectSubmission();
            project.setTitle(title);
            project.setFileLink(fileLink);
            project.setTeamId(Integer.parseInt(teamId));
            project.setSubmissionDate(Date.valueOf(String.valueOf(submissionDate)));

            projectService.add(project);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Project added successfully " );

        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Date Error", "Invalid date format. Use yyyy-MM-dd");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error adding project: " + e.getMessage());
        }
    }

    @FXML
    public void navigateToShowProjects(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ShowProject.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Project List");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Error loading project list: " + e.getMessage());
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