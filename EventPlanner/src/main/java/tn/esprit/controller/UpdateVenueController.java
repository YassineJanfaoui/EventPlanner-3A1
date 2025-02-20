package tn.esprit.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import tn.esprit.entities.Venue;
import tn.esprit.services.VenueServices;

import java.io.IOException;
import java.sql.SQLException;

public class UpdateVenueController {

    @FXML
    private TextField venueNameField;
    @FXML
    private TextField locationField;
    @FXML
    private TextField nbrPlacesField;
    @FXML
    private ComboBox<String> availabilityComboBox;
    @FXML
    private TextField costField;
    @FXML
    private ComboBox<String> parkingComboBox;

    private final VenueServices venueService = new VenueServices();
    private Venue currentVenue;

    public UpdateVenueController() throws SQLException {
    }

    public void initialize() {
        // Initialize availability choices
        ObservableList<String> availabilityOptions = FXCollections.observableArrayList(
                "8h-20h", "8h-13h", "15h-20h"
        );
        availabilityComboBox.setItems(availabilityOptions);

        // Initialize parking choices
        ObservableList<String> parkingOptions = FXCollections.observableArrayList(
                "Available", "Unavailable"
        );
        parkingComboBox.setItems(parkingOptions);
    }

    @FXML
    public void handleUpdateVenue(ActionEvent event) {
        try {
            String venueName = venueNameField.getText().trim();
            String location = locationField.getText().trim();
            String nbrPlacesText = nbrPlacesField.getText().trim();
            String availability = availabilityComboBox.getValue();
            String costText = costField.getText().trim();
            String parking = parkingComboBox.getValue();

            if (venueName.isEmpty() || location.isEmpty() || nbrPlacesText.isEmpty() || costText.isEmpty() || availability == null || parking == null) {
                showAlert(Alert.AlertType.ERROR, "Form Error", "All fields must be completed.");
                return;
            }

            int nbrPlaces;
            float cost;
            try {
                nbrPlaces = Integer.parseInt(nbrPlacesText);
                cost = Float.parseFloat(costText);
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Number of Places must be an integer and Cost must be a valid number.");
                return;
            }

            Venue venue = new Venue(
                    currentVenue.getVenueId(), // Preserve the same VenueId
                    location,
                    nbrPlaces,
                    venueName,
                    availability,
                    cost,
                    parking
            );

            try {
                venueService.update(venue);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Venue updated successfully.");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Unexpected Error", "An error occurred while updating the venue.");
        }
    }

    @FXML
    public void navigateToShowVenue(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ShowVenue.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Show Venues");
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setCurrentVenue(Venue venue) {
        this.currentVenue = venue;
        venueNameField.setText(currentVenue.getVenueName());
        locationField.setText(currentVenue.getLocation());
        nbrPlacesField.setText(String.valueOf(currentVenue.getNbrPlaces()));
        availabilityComboBox.setValue(currentVenue.getAvailability());
        costField.setText(String.valueOf(currentVenue.getCost()));
        parkingComboBox.setValue(currentVenue.getParking());
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
