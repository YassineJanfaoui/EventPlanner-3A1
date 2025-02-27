package tn.esprit.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.entities.Participant;
import tn.esprit.entities.Team;
import tn.esprit.services.ParticipantService;

import java.io.IOException;
import java.util.List;

public class AddParticipantController {

    @FXML
    private TextField AffiliationField;

    @FXML
    private TextField Age;

    @FXML
    private TextField nameField;

    // 1. Properly type the ComboBox with <Team>
    @FXML
    private ComboBox<Team> teamComboBox; // Changed from ComboBox<?>

    private final ParticipantService participantService = new ParticipantService();

    @FXML
    void navigateToShowParticipant(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ShowParticipant.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Show Participant");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        List<Team> teams = participantService.getTeams();
        // 2. Add all teams to the ComboBox
        teamComboBox.getItems().addAll(teams);

        // 3. Update cell factory to match ComboBox<Team> type
        teamComboBox.setCellFactory(param -> new ListCell<Team>() {
            @Override
            protected void updateItem(Team team, boolean empty) {
                super.updateItem(team, empty);
                if (empty || team == null) {
                    setText(null);
                } else {
                    setText(team.getTeamName());
                }
            }
        });

        // 4. Update button cell to match ComboBox<Team> type
        teamComboBox.setButtonCell(new ListCell<Team>() {
            @Override
            protected void updateItem(Team team, boolean empty) {
                super.updateItem(team, empty);
                if (empty || team == null) {
                    setText(null);
                } else {
                    setText(team.getTeamName());
                }
            }
        });
    }

    @FXML
    public void handleAddParticipant(ActionEvent event) {
        String name = nameField.getText().trim();
        String affiliation = AffiliationField.getText().trim();
        Team selectedTeam = teamComboBox.getSelectionModel().getSelectedItem();
        String ageText = Age.getText().trim();

        if (name.isEmpty() || affiliation.isEmpty() || selectedTeam == null || ageText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please fill all fields.");
            return;
        }

        try {
            int age = Integer.parseInt(ageText);
            if (age < 0) {
                showAlert(Alert.AlertType.ERROR, "Form Error!", "Age must be a positive number.");
                return;
            }

            Participant updatedParticipant = new Participant( name, affiliation, age, selectedTeam.getId());
            participantService.add(updatedParticipant);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Participant updated successfully.");

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Age must be a valid number.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}