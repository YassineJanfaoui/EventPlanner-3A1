package tn.esprit.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.entities.Partner;
import tn.esprit.services.PartnerServices;

import java.io.IOException;
import java.sql.SQLException;

public class AddPartnerController {
    @FXML private TextField partner_name;
    @FXML private TextField category;
    @FXML private Button add;

    private final PartnerServices partnerService = new PartnerServices();

    @FXML
    private void initialize() {
        // Initialization if needed
    }

    @FXML
    private void handleAddPartner() {
        try {
            String partnerName = partner_name.getText().trim();
            String Category = category.getText().trim();

            if (partnerName.isEmpty() || Category.isEmpty()) {
                showAlert("Error", "Partner Name and Category are required!");
                return;
            }

            Partner partner = new Partner();
            partner.setName(partnerName);
            partner.setCategory(Category);

            partnerService.addP(partner);

            showAlert("Success", "Partner added successfully!");
            clearForm();

        } catch (SQLException e) {
            showAlert("Database Error", "Failed to add partner: " + e.getMessage());
        } catch (Exception e) {
            showAlert("Error", "An unexpected error occurred: " + e.getMessage());
        }
    }


    private void clearForm() {
        partner_name.clear();
        category.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void navigateToShowPartner(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ShowPartner.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Show Bills");
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}