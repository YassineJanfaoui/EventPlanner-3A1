package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import tn.esprit.entities.Partner;
import tn.esprit.services.PartnerServices;
import java.io.IOException;
import java.sql.SQLException;

public class UpdatePartnerController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField categoryField;

    private final PartnerServices partnerService = new PartnerServices();
    private Partner currentPartner;

    @FXML
    private void handleUpdatePartner() {
        try {
            String partnerName = nameField.getText().trim();
            String categoryName = categoryField.getText().trim();

            if (partnerName.isEmpty() || categoryName.isEmpty()) {
                showAlert("Error", "Partner Name and Category are required!");
                return;
            }

            // Validate that partnerName and category only contain letters and spaces
            if (!partnerName.matches("[a-zA-Z\\s]+")) {
                showAlert("Error", "Partner Name must only contain letters and spaces!");
                return;
            }

            if (!categoryName.matches("[a-zA-Z\\s]+")) {
                showAlert("Error", "Category must only contain letters and spaces!");
                return;
            }

            Partner partner = new Partner(currentPartner.getPartnerId(), partnerName, categoryName);

            partnerService.update(partner);

            showAlert("Success", "Partner updated successfully!");


        } catch (Exception e) {
            showAlert("Error", "An unexpected error occurred: " + e.getMessage());
        }
    }

    @FXML
    public void navigateToShowPartner(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ShowPartner.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Show Partners");
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setCurrentPartner(Partner partner) {
        this.currentPartner = partner;
        nameField.setText(currentPartner.getName());
        categoryField.setText(currentPartner.getCategory());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
}
