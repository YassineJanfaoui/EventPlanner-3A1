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

public class UpdatePartnerController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField categoryField;

    private final PartnerServices partnerService = new PartnerServices();
    private Partner currentPartner;

    @FXML
    public void handleUpdatePartner(ActionEvent event) {
        try {
            String name = nameField.getText().trim();
            String category = categoryField.getText().trim();

            if (name.isEmpty() || category.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Form Error", "All fields must be completed.");
                return;
            }

            Partner partner = new Partner(currentPartner.getPartnerId(), name, category);

            try {
                partnerService.update(partner);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Partner updated successfully.");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid input.");
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

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
}
