package tn.esprit.controller;

import tn.esprit.entities.Reservation;
import tn.esprit.services.ReservationService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class Modifyreservation {
    @FXML
    private TextField participantIdField;
    @FXML
    private ChoiceBox<Integer> venueChoiceBox;

    @FXML
    private ChoiceBox<Integer> eventChoiceBox;

    @FXML
    private TextField reservationDateField;

    @FXML
    private TextField reservationPriceField;

    @FXML
    private Button saveReservationButton;

    // Validate form and update reservation
    // Show alert dialog
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }

    @FXML
    void validate(ActionEvent event) throws IOException {

            // Validate and parse input fields
            int eventVenueId = Integer.parseInt(participantIdField.getText());
            int venue = venueChoiceBox.getValue();
            int eventName = eventChoiceBox.getValue();
            String reservationDate = reservationDateField.getText();
            String reservationPriceText = reservationPriceField.getText();

            // Validate that no fields are null or empty

            // Parse reservation price
            String reservationPrice = String.valueOf(Double.parseDouble(reservationPriceText)); // Adjusted to parse to double

            // Create Reservation object
            Reservation modifiedReservation = new Reservation(
                    eventVenueId,
                    venue,
                    eventName,
                    reservationDate,
                    reservationPrice // Use the parsed price
            );

            // Save the modified reservation
            ReservationService reservationService = new ReservationService();
            reservationService.modifier(modifiedReservation);

          // Show confirmation message
            showAlert("Confirmation", "Reservation modifié avec succès !");

    }

    public ChoiceBox<Integer> getEventChoiceBox() {
        return eventChoiceBox;
    }

    public void setEventChoiceBox(ChoiceBox<Integer> eventChoiceBox) {
        this.eventChoiceBox = eventChoiceBox;
    }

    public ChoiceBox<Integer> getVenueChoiceBox() {
        return venueChoiceBox;
    }

    public void setVenueChoiceBox(ChoiceBox<Integer> venueChoiceBox) {
        this.venueChoiceBox = venueChoiceBox;
    }

    public TextField getReservationDateField() {
        return reservationDateField;
    }

    public void setReservationDateField(TextField reservationDateField) {
        this.reservationDateField = reservationDateField;
    }

    public TextField getReservationPriceField() {
        return reservationPriceField;
    }

    public void setReservationPriceField(TextField reservationPriceField) {
        this.reservationPriceField = reservationPriceField;
    }

    public Button getSaveReservationButton() {
        return saveReservationButton;
    }

    public void setSaveReservationButton(Button saveReservationButton) {
        this.saveReservationButton = saveReservationButton;
    }

    // Initialize form with existing reservation data
    public void initData(Reservation selectedParticipant) {
        if (selectedParticipant == null) {
            showAlert("Erreur", "Aucun participant sélectionné.");
            return;
        }
        // Initialize fields with selected participant data
        participantIdField.setText(String.valueOf(selectedParticipant.getEventVenueId()));
        participantIdField.setDisable(true); // Prevent ID modification
        venueChoiceBox.setValue(selectedParticipant.getVenueId()); // Assuming getVenue() returns a String
        eventChoiceBox.setValue(selectedParticipant.getEventId()); // Assuming getEventName() returns a String
        reservationDateField.setText(selectedParticipant.getReservationDate()); // Assuming getReservationDate() returns a String
        reservationPriceField.setText(String.valueOf(selectedParticipant.getReservationPrice())); // Assuming getReservationPrice() returns a double
    }

    private void loadReservationData(Reservation reservation) {
        if (reservation != null) {
            venueChoiceBox.setValue(reservation.getVenueId()); // Assuming venueId corresponds to the ChoiceBox
            eventChoiceBox.setValue(reservation.getEventId()); // Assuming eventId corresponds to the ChoiceBox
            reservationDateField.setText(reservation.getReservationDate());
            reservationPriceField.setText(reservation.getReservationPrice());
        }
    }

    @FXML
    private void initialize() {
        ReservationService service = new ReservationService();

        // Populate venueChoiceBox and eventChoiceBox with available IDs
        venueChoiceBox.getItems().addAll(service.getAvailableIds("venue", "venueId"));
        eventChoiceBox.getItems().addAll(service.getAvailableIds("event", "eventId"));

        // Set default selections
        if (!venueChoiceBox.getItems().isEmpty()) {
            venueChoiceBox.setValue(venueChoiceBox.getItems().get(0));
        }
        if (!eventChoiceBox.getItems().isEmpty()) {
            eventChoiceBox.setValue(eventChoiceBox.getItems().get(0));
        }

        // Replace with actual method to get reservation ID
        int reservationId = getReservationId();  // Ensure this method exists and returns a valid ID
        Reservation reservation = service.getReservationById(reservationId); // Call the service method

        // Check if the reservation is not null before loading data
        if (reservation != null) {
            loadReservationData(reservation); // Load existing reservation data if applicable

            // Optionally, set the venueChoiceBox and eventChoiceBox to the reservation's values
            Integer eventVenueId = reservation.getEventVenueId();
            if (eventVenueId != null) {
                venueChoiceBox.setValue(eventVenueId);
            }

            // Assuming reservation has a method to get eventId
            Integer eventId = reservation.getEventId();
            if (eventId != null) {
                eventChoiceBox.setValue(eventId);
            }
        } else {
            System.out.println("No reservation found for ID: " + reservationId);
        }
    }
    private int getReservationId() {
        // Assuming you have a ComboBox or ListView named reservationListView
        return 1;
    }
    public TextField getParticipantIdField() {
        return participantIdField;
    }
    public void setParticipantIdField(TextField participantIdField) {
        this.participantIdField = participantIdField;
    }
    @FXML
    private void goToAjouterEvents(ActionEvent event) {
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
    private void goToAjouterEventss(ActionEvent event) {
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
    @FXML
    private void goToAjouterEve(ActionEvent event) {
        try {
            // Charger la nouvelle page (ajoutez le bon chemin FXML)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Afficherreservation.fxml"));
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