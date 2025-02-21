package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.services.ParticipantService;
import tn.esprit.entities.Participant;
import javafx.event.ActionEvent;
import java.io.IOException;

public class ModifyParticipant {

    @FXML
    private TextField participantIdField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField affiliationField;

    @FXML
    private TextField ageField;

    @FXML
    private ChoiceBox<Integer> teamIdField;

    // Validate form and update participant
    @FXML
    void validate(ActionEvent event) throws IOException {
        try {
            // Validate and parse input fields
            int participantId = Integer.parseInt(participantIdField.getText());
            String name = nameField.getText();
            String affiliation = affiliationField.getText();
            int age = Integer.parseInt(ageField.getText());
            int teamId = teamIdField.getValue(); // Get the selected team ID directly from ChoiceBox
            // Create a Participant object
            Participant updatedParticipant = new Participant(participantId, name, affiliation, age, teamId);

            // Update the participant in the database
            ParticipantService participantService = new ParticipantService();
            participantService.modifier(updatedParticipant);

            // Show confirmation message
            showAlert("Confirmation", "Participant modifié avec succès !");
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des valeurs valides pour les champs numériques (ID, Âge, ID d'équipe).");
        } catch (NullPointerException e) {
            showAlert("Erreur", "Tous les champs doivent être remplis.");
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur s'est produite : " + e.getMessage());
        }
    }

    // Initialize form with existing participant data
    public void initData(Participant selectedParticipant) {
        if (selectedParticipant == null) {
            showAlert("Erreur", "Aucun participant sélectionné.");
            return;
        }

        // Initialize fields with selected participant data
        participantIdField.setText(String.valueOf(selectedParticipant.getParticipantId()));
        participantIdField.setDisable(true); // Prevent ID modification

        nameField.setText(selectedParticipant.getName());
        affiliationField.setText(selectedParticipant.getAffiliation());
        ageField.setText(String.valueOf(selectedParticipant.getAge()));
        teamIdField.setValue(selectedParticipant.getTeamid());
    }

    // Show alert helper method
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }

    // Placeholder for getting the participant ID
    private int getParticipantId() {
        // Implement how to retrieve the participant ID
        return 0; // Replace with actual logic
    }

    // Load existing participant data into the form (to be called in initialize)
    private void loadParticipantData(int participantId) {
        ParticipantService service = new ParticipantService();
        Participant participant = service.getParticipantById(participantId); // Assuming this method exists

        if (participant != null) {
            initData(participant);
        }
    }

    // Initialize the controller (load participant data)
    @FXML
    private void initialize() {
        ParticipantService service = new ParticipantService();
        teamIdField.getItems().addAll(service.getAvailableIdss("team", "teamid"));

        if (!teamIdField.getItems().isEmpty()) teamIdField.setValue(teamIdField.getItems().get(0));
        int participantId = getParticipantId(); // Replace with actual method to get participant ID
        loadParticipantData(participantId);
    }

    public TextField getParticipantIdField() {
        return participantIdField;
    }

    public void setParticipantIdField(TextField participantIdField) {
        this.participantIdField = participantIdField;
    }

    public TextField getNameField() {
        return nameField;
    }

    public void setNameField(TextField nameField) {
        this.nameField = nameField;
    }

    public TextField getAffiliationField() {
        return affiliationField;
    }

    public void setAffiliationField(TextField affiliationField) {
        this.affiliationField = affiliationField;
    }

    public TextField getAgeField() {
        return ageField;
    }

    public void setAgeField(TextField ageField) {
        this.ageField = ageField;
    }
    @FXML
    private void goToAjouterEvents(ActionEvent event) {
        loadScene(event, "/AfficherParticipant.fxml");
    }

    private void loadScene(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}