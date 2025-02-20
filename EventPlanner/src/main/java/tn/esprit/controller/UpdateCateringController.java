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

public class UpdateCateringController {

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
    private Catering currentCatering;

    public UpdateCateringController() throws SQLException {
    }

    public void initialize() {
        // Initialize Menu Type choices
        ObservableList<String> menuOptions = FXCollections.observableArrayList(
                "Normal", "Vegan", "Vegetarian", "Gluten Free"
        );
        menuTypeComboBox.setItems(menuOptions);

        // Initialize Meal Schedule choices
        ObservableList<String> mealOptions = FXCollections.observableArrayList(
                "Breakfast", "Lunch", "Dinner", "Snacks"
        );
        mealScheduleComboBox.setItems(mealOptions);

        // Initialize Beverages choices
        ObservableList<String> beverageOptions = FXCollections.observableArrayList(
                "Water", "Coffee", "Tea"
        );
        beveragesComboBox.setItems(beverageOptions);

        // Fetch available venue IDs dynamically
        try {
            int[] venueIDs = venueService.venueIDs();
            ObservableList<Integer> venueIdList = FXCollections.observableArrayList();
            for (int id : venueIDs) {
                venueIdList.add(id);
            }
            venueIdComboBox.setItems(venueIdList);
        } catch (Exception e) {
            System.out.println("Error fetching venue IDs: " + e.getMessage());
        }
    }

    @FXML
    public void handleUpdateCatering(ActionEvent event) {
        try {
            String menuType = menuTypeComboBox.getValue();
            String mealSchedule = mealScheduleComboBox.getValue();
            String beverages = beveragesComboBox.getValue();
            String nbrPlatesText = nbrPlatesField.getText().trim();
            String pricingText = pricingField.getText().trim();
            Integer venueId = venueIdComboBox.getValue();

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

            Catering catering = new Catering(
                    currentCatering.getCateringId(), // Preserve existing CateringId
                    menuType,
                    nbrPlates,
                    pricing,
                    mealSchedule,
                    beverages,
                    venueId
            );

            try {
                cateringService.update(catering);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Catering service updated successfully.");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Unexpected Error", "An error occurred while updating the catering service.");
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

    public void setCurrentCatering(Catering catering) {
        this.currentCatering = catering;
        menuTypeComboBox.setValue(currentCatering.getMenuType());
        nbrPlatesField.setText(String.valueOf(currentCatering.getNbrPlates()));
        pricingField.setText(String.valueOf(currentCatering.getPricing()));
        mealScheduleComboBox.setValue(currentCatering.getMealSchedule());
        beveragesComboBox.setValue(currentCatering.getBeverages());
        venueIdComboBox.setValue(currentCatering.getVenueId());
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}