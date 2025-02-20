package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView; // Import ImageView for pdpImageView
import javafx.event.ActionEvent;
import tn.esprit.services.EventService;
import tn.esprit.entities.Event;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Addfront {

    @FXML
    private TextField name; // Event Name
    @FXML
    private DatePicker startDate; // Start Date
    @FXML
    private DatePicker endDate; // End Date
    @FXML
    private TextField maxParticipants; // Max Participants
    @FXML
    private TextField description; // Event Description
    @FXML
    private TextField fee; // Event Fee
    @FXML
    private ChoiceBox<Integer> userId; // User ID
    @FXML
    private TextField image; // Image Path
    @FXML
    private ChoiceBox<Integer> partnerChoiceBox;
    // Declare the missing fields
    @FXML
    private TextField pdpPathField; // Path field for the image
    @FXML
    private ImageView pdpImageView; // ImageView for displaying the image
    @FXML
    private Label pdpIconLabel; // Label for icon visibility
    @FXML
    private Button btnModifyEvent;
  // Button to navigate to Modify Event

    @FXML
    void Validate(ActionEvent event) throws IOException {
        // Validation to check if fields are filled
        if (name.getText().isEmpty() ||
                startDate.getValue() == null ||
                endDate.getValue() == null ||
                maxParticipants.getText().isEmpty() ||
                fee.getText().isEmpty()){  // Check if the image path is not empty
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Tous les champs doivent être remplis");
            alert.show();
            return; // Don't proceed if validation fails
        }

        try {
            // Creating Event object using the form data
            Event newEvent = new Event(
                    name.getText(),                     // Event Name
                    startDate.getValue().toString(),    // Event Date (start date)
                    endDate.getValue().toString(),      // Event Date (end date)
                    Integer.parseInt(maxParticipants.getText()), // Max Participants
                    description.getText(),               // Event Description
                    Integer.parseInt(fee.getText()),    // Event Fee
                    userId.getValue(),                  // User ID
                    pdpPathField.getText() // If image is empty, use a default image path
            );

            // Saving the event using EventService
            EventService eventService = new EventService();
            eventService.ajouter(newEvent);

            // Show success alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmation");
            alert.setContentText("L'événement a été ajouté avec succès!");
            alert.show();

        } catch (NumberFormatException e) {
            // Show error alert if parsing fails (for max participants, fee, or userId)
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de format");
            alert.setContentText("Le nombre de participants, le prix ou l'ID utilisateur est invalide!");
            alert.show();
        }
    }

    @FXML
    private void initialize() {
        EventService service = new EventService();

        // Charger les IDs de la table correspondante (ex: type d'activité)

        userId.getItems().addAll(service.getAvailableIds("user", "userid"));

        // Optionnel : Sélectionner la première valeur par défaut
        if (!userId.getItems().isEmpty()) userId.setValue(userId.getItems().get(0));

    }

    public void choisirImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            String imagePath = selectedFile.toURI().toString();
            pdpPathField.setText(selectedFile.getAbsolutePath());
            pdpImageView.setImage(new Image(pdpPathField.getText()));
            pdpIconLabel.setVisible(false);
        }
    }
    @FXML
    private void goToAjouterEvent(ActionEvent event) {
        try {
            // Charger la nouvelle page (ajoutez le bon chemin FXML)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/event3.fxml"));
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
    @FXML
    private void goToAjouterEvents(ActionEvent event) {
        try {
            // Charger la nouvelle page (ajoutez le bon chemin FXML)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addreservation.fxml"));
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
    @FXML
    private void handleModifyEvent(ActionEvent event) {
        try {
            // Load the event2.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/event2.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) btnModifyEvent.getScene().getWindow();

            // Set the new scene with the event2.fxml content
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleHomeButton(ActionEvent event) {
        try {
            // Load the Afficherfront.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Afficherfront.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene with the Afficherfront.fxml content
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
