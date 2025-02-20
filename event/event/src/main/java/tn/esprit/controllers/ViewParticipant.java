package tn.esprit.controllers;

import tn.esprit.entities.Participant;
import tn.esprit.services.ParticipantService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class ViewParticipant {

    @FXML
    private TextField nameField; // Participant's name
    @FXML
    private TextField affiliationField; // Participant's affiliation
    @FXML
    private TextField ageField; // Participant's age
    // Event ID
    @FXML
    private ChoiceBox<Integer> teamid; // Event ID

    private final ParticipantService participantService = new ParticipantService(); // Adjust based on your actual service class

    @FXML
    void Validate(ActionEvent event) throws IOException {
        // Validation to check if fields are filled
        if (nameField.getText().isEmpty() || affiliationField.getText().isEmpty() || ageField.getText().isEmpty() || teamid.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Tous les champs doivent être remplis");
            alert.show();
            return; // Don't proceed if validation fails
        }

        // Try-catch block to handle parsing errors
        try {
            // Creating Participant object using the form data
            Participant newParticipant = new Participant(
                    nameField.getText(),
                    affiliationField.getText(),
                    Integer.parseInt(ageField.getText()),
                    teamid.getValue()
            );

            // Saving the participant using ParticipantService
            participantService.ajouter(newParticipant);

            // Show success alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmation");
            alert.setContentText("Le participant a été ajouté avec succès!");
            alert.show();

        } catch (NumberFormatException e) {
            // Show error alert if parsing fails (for age or team ID)
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de format");
            alert.setContentText("L'âge ou l'ID de l'équipe est invalide!");
            alert.show();
        }
    }

    @FXML
    void backToList(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/path/to/ParticipantListView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Impossible de charger la vue de la liste des participants");
            alert.show();
        }
    }
    @FXML
    private void initialize() {
        ParticipantService service = new ParticipantService();

        // Load IDs from the database
        List<Integer> teamIds = service.getAvailableIdss("team", "teamid");

        // Check if IDs were retrieved successfully
        if (teamIds != null && !teamIds.isEmpty()) {
            teamid.getItems().addAll(teamIds); // Add IDs to the ComboBox
            teamid.setValue(teamIds.get(0)); // Set the default value to the first item
        } else {
            System.out.println("No team IDs found or an error occurred.");
        }
    }
    @FXML
    private void goToAjouterEvents(ActionEvent event) {
        try {
            // Charger la nouvelle page (ajoutez le bon chemin FXML)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherParticipant.fxml"));
            Parent root = loader.load();

            // Récupérer la scène actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Changer la scène
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
