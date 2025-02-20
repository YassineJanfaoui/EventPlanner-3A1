package tn.esprit.controllers;

import tn.esprit.entities.Reservation;
import tn.esprit.services.ReservationService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class Addreservation {

    @FXML
    private ChoiceBox<Integer> venueChoiceBox; // Choice box for venues (changed to Integer)
   // Choice box for venues (changed to Integer)
    @FXML
    private ChoiceBox<Integer> eventChoiceBox; // Choice box for events (changed to Integer)
    @FXML
    private TextField reservationDate; // TextField for reservation date
    @FXML
    private TextField reservationPrice; // TextField for reservation price
    @FXML
    private Button saveReservationButton; // Button to save reservation
    @FXML
    private Button btnModifyEventsss;

    // Method to handle the save reservation action
    @FXML
    void Validate(ActionEvent event) throws IOException {

        // Validation to check if fields are filled
        if (venueChoiceBox.getValue() == null || // Check if venue is selected
                eventChoiceBox.getValue() == null || // Check if event is selected
                reservationDate.getText().isEmpty() ||
                reservationPrice.getText().isEmpty()) { // Check if price is entered
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Tous les champs doivent être remplis");
            alert.show();
            return; // Don't proceed if validation fails
        }

        try {
            // Creating Reservation object using the form data
            Reservation newReservation = new Reservation(
                    venueChoiceBox.getValue(), // Venue ID as Integer
                    eventChoiceBox.getValue(), // Event ID as Integer
                    reservationDate.getText(), // Reservation Date
                    reservationPrice.getText()// Reservation Price
            );

            // Saving the reservation using ReservationService
            ReservationService reservationService = new ReservationService();
            reservationService.ajouter(newReservation);

            // Show success alert
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Confirmation");
            alert.setContentText("La réservation a été ajoutée avec succès!");
            alert.show();

        } catch (NumberFormatException e) {
            // Show error alert if parsing fails (for reservation price)
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur de format");
            alert.setContentText("Le prix de la réservation est invalide!");
            alert.show();
        }
    }

    // Method to initialize the controller
    @FXML
    private void initialize() {
        ReservationService service = new ReservationService();

        // Charger les IDs de la table correspondante (ex: type d'activité)
        venueChoiceBox.getItems().addAll(service.getAvailableIds("venue", "venueId"));

        // Optionnel : Sélectionner la première valeur par défaut
        if (!venueChoiceBox.getItems().isEmpty()) venueChoiceBox.setValue(venueChoiceBox.getItems().get(0));

        eventChoiceBox.getItems().addAll(service.getAvailableIds("event", "eventId"));

        // Optionnel : Sélectionner la première valeur par défaut

    }

    @FXML
    private void goToAjouterEventss(ActionEvent event) {
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
    private void handleModifyEventss(ActionEvent event) {
        try {
            // Load the event2.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifyreservation.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) btnModifyEventsss.getScene().getWindow();

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
}