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
import tn.esprit.entities.Team;
import tn.esprit.services.TeamServices;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class AddTeamController {

    @FXML
    private TextField teamNameField;

    @FXML
    private ComboBox<String> eventIdComboBox;

    private final TeamServices teamService = new TeamServices();

    public void initialize() {
        try {
            Map<String, Integer> eventMap = teamService.eventNames();
            ObservableList<String> eventNameList = FXCollections.observableArrayList(eventMap.keySet());
            eventIdComboBox.setItems(eventNameList);
            if (!eventNameList.isEmpty()) eventIdComboBox.getSelectionModel().selectFirst();
        } catch (SQLException e) {
            System.out.println("Error fetching names: " + e.getMessage());
        }
    }

    @FXML
    public void handleAddTeam(ActionEvent event) {
        try {
            String teamName = teamNameField.getText().trim();
            String selectedEventName = eventIdComboBox.getValue();

            if (teamName.isEmpty() || selectedEventName == null) {
                showAlert(Alert.AlertType.ERROR, "Form Error", "All fields must be completed.");
                return;
            }
            Integer eventId = teamService.eventNames().get(selectedEventName);
            Team team = new Team(0, teamName, 0, 0, eventId);
            teamService.add(team);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Team added successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add team.");
        }
    }

    @FXML
    public void navigateToShowTeams(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ShowTeam.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Show Teams");
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
