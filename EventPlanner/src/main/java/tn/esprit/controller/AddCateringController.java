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
import tn.esprit.entities.Catering;
import tn.esprit.services.CateringServices;
import tn.esprit.services.VenueServices;

import java.io.IOException;
import java.sql.SQLException;

public class AddCateringController {

    @FXML
    private ComboBox<String> menuTypeComboBox;
    @FXML
    private TextField nbrPlatesField;
    @FXML
    private TextField pricingField;
    @FXML
    private ComboBox<String> mealScheduleComboBox;
    @FXML
    private ComboBox<String> beveragesComboBox;
    @FXML
    private ComboBox<Integer> venueIdComboBox;

    private final CateringServices cateringService = new CateringServices();
    private final VenueServices venueService = new VenueServices();

    public AddCateringController() throws SQLException {
    }

    public void initialize() {
        // Initialize menu type choices
        ObservableList<String> menuOptions = FXCollections.observableArrayList(
                "Normal", "Vegan", "Vegetarian", "Gluten Free"
        );
        menuTypeComboBox.setItems(menuOptions);
        menuTypeComboBox.getSelectionModel().selectFirst();

        System.out.println("Debug: menuTypeComboBox Items = " + menuTypeComboBox.getItems());

        // Initialize meal schedule choices
        ObservableList<String> mealOptions = FXCollections.observableArrayList(
                "Breakfast", "Lunch", "Dinner", "Snacks"
        );
        mealScheduleComboBox.setItems(mealOptions);
        mealScheduleComboBox.getSelectionModel().selectFirst();

        // Initialize beverages choices
        ObservableList<String> beverageOptions = FXCollections.observableArrayList(
                "Water", "Coffee", "Tea"
        );
        beveragesComboBox.setItems(beverageOptions);
        beveragesComboBox.getSelectionModel().selectFirst();

        // Fetch available venue IDs dynamically
        try {
            int[] venueIDs = venueService.venueIDs();
            ObservableList<Integer> venueIdList = FXCollections.observableArrayList();
            for (int id : venueIDs) {
                venueIdList.add(id);
            }
            venueIdComboBox.setItems(venueIdList);
            if (!venueIdList.isEmpty()) {
                venueIdComboBox.getSelectionModel().selectFirst();
            }
        } catch (Exception e) {
            System.out.println("Error fetching venue IDs: " + e.getMessage());
        }
    }

    @FXML
    public void handleAddCatering(ActionEvent event) {
        try {
            String menuType = menuTypeComboBox.getValue();
            String mealSchedule = mealScheduleComboBox.getValue();
            String beverages = beveragesComboBox.getValue();
            String nbrPlatesText = nbrPlatesField.getText().trim();
            String pricingText = pricingField.getText().trim();
            Integer venueId = venueIdComboBox.getValue();

            System.out.println("Debug: Selected MenuType = " + menuType);
            System.out.println("Debug: ComboBox Items = " + menuTypeComboBox.getItems());

            if (menuType == null || menuType.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Form Error", "Please select a Menu Type.");
                return;
            }
            if (menuType == null || mealSchedule == null || beverages == null || nbrPlatesText.isEmpty() || pricingText.isEmpty() || venueId == null) {
                showAlert(Alert.AlertType.ERROR, "Form Error", "All fields must be completed.");
                return;
            }

            int nbrPlates;
            float pricing;
            try {
                nbrPlates = Integer.parseInt(nbrPlatesText);
                pricing = Float.parseFloat(pricingText);
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Number of Plates must be an integer and Pricing must be a valid number.");
                return;
            }

            System.out.println("Selected Menu Type: " + menuTypeComboBox.getValue());

            Catering catering = new Catering(0, menuType, nbrPlates, pricing, mealSchedule, beverages, venueId);

            try {
                cateringService.addP(catering);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Catering service added successfully.");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Unexpected Error", "An error occurred while adding the catering service.");
        }
    }

    @FXML
    public void navigateToShowCatering(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ShowCatering.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Show Catering Services");
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