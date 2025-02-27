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
import tn.esprit.entities.*;
import tn.esprit.services.*;

import java.io.IOException;
import java.util.List;

public class UpdateParticipantController {

    @FXML
    private TextField AffiliationField;

    @FXML
    private TextField Age;

    @FXML
    private TextField nameField;
    private int ParticipantId;
    @FXML
    private ComboBox<Team> teamComboBox;

    private final ParticipantService participantService = new ParticipantService();

    public void setParticipantData(Participant participant) {
        this.ParticipantId = participant.getParticipantId();
        nameField.setText(participant.getName());
        Age.setText(String.valueOf(participant.getAge()));
        AffiliationField.setText(participant.getAffiliation());
        //String teamname=toStringName((participantService.getTeam(participant.getTeamid())));
        //teamComboBox.setAccessibleText(teamname);
    }

    @FXML
    public void initialize() {
        List<Team> teams = participantService.getTeams();
        teamComboBox.getItems().addAll(teams);

        // Set a custom cell factory to display only the team name
        teamComboBox.setCellFactory(param -> new ListCell<Team>() {
            @Override
            protected void updateItem(Team team, boolean empty) {
                super.updateItem(team, empty);
                if (empty || team == null) {
                    setText(null);
                } else {
                    setText(team.getTeamName()); // Display only the team name
                }
            }
        });

        // Set a custom button cell to display the selected team name
        teamComboBox.setButtonCell(new ListCell<Team>() {
            @Override
            protected void updateItem(Team team, boolean empty) {
                super.updateItem(team, empty);
                if (empty || team == null) {
                    setText(null);
                } else {
                    setText(team.getTeamName()); // Display only the team name
                }
            }
        });
    }

    @FXML
    public void handleUpdateParticipant(ActionEvent event) {
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

            Participant updatedParticipant = new Participant(ParticipantId, name, affiliation, age, selectedTeam.getId());
            participantService.update(updatedParticipant);
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

    public void navigateToShowParticipant(ActionEvent event) {
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

    public void navigateToAddParticipant(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddParticipant.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Add Participant");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}