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

public class AddVenueController {

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

    public AddVenueController() throws SQLException {
    }

    public void initialize() {
        // Initialize availability choices
        ObservableList<String> availabilityOptions = FXCollections.observableArrayList(
                "8h-20h", "8h-13h", "15h-20h"
        );
        availabilityComboBox.setItems(availabilityOptions);
        availabilityComboBox.getSelectionModel().selectFirst();

        // Initialize parking choices
        ObservableList<String> parkingOptions = FXCollections.observableArrayList(
                "Available", "Unavailable"
        );
        parkingComboBox.setItems(parkingOptions);
        parkingComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    public void handleAddVenue(ActionEvent event) {
        try {
            String location = locationField.getText().trim();
            String venueName = venueNameField.getText().trim();
            String availability = availabilityComboBox.getValue();
            String parking = parkingComboBox.getValue();
            String nbrPlacesText = nbrPlacesField.getText().trim();
            String costText = costField.getText().trim();

            System.out.println("Debug: Selected Values");
            System.out.println("Location: " + location);
            System.out.println("Venue Name: " + venueName);
            System.out.println("Nbr Places: " + nbrPlacesText);
            System.out.println("Cost: " + costText);
            System.out.println("Selected Availability: " + availability);
            System.out.println("Selected Parking: " + parking);

            // ✅ Validate that Availability is not null
            if (availability == null || availability.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Form Error", "Please select an Availability option.");
                return;
            }

            if (location.isEmpty() || nbrPlacesText.isEmpty() || venueName.isEmpty() || costText.isEmpty() || availability == null || parking == null) {
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

            Venue venue = new Venue(0, location, nbrPlaces, venueName, availability, cost, parking);

            try {
                venueService.addP(venue);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Venue added successfully.");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Unexpected Error", "An error occurred while adding the venue.");
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

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

