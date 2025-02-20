package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import javafx.stage.Stage;
import tn.esprit.entities.Equipment;
import tn.esprit.services.EquipmentServices;

import java.io.IOException;

public class AddEquipmentController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField categoryField;

    @FXML
    private ComboBox<String> stateComboBox;

    @FXML
    private TextField quantity;

    @FXML
    private Button addEquipmentButton;

    @FXML
    public void handleAddEquipment(ActionEvent event) {
        EquipmentServices equipmentServices = new EquipmentServices();
        String name = nameField.getText().trim();
        String category = categoryField.getText().trim();
        String state = stateComboBox.getValue();
        String quantityText = quantity.getText().trim();

        if (name.isEmpty() || category.isEmpty() || state == null || quantityText.isEmpty()) {
            showAlert(AlertType.ERROR, "Form Error!", "Please fill all fields.");
            return;
        }

        try {
            int qty = Integer.parseInt(quantityText);
            if (qty < 0) {
                showAlert(AlertType.ERROR, "Form Error!", "Quantity must be a positive number.");
                return;
            }

            Equipment equipment = new Equipment(0,qty,name,category,state);
            try{
                equipmentServices.addP(equipment);
                showAlert(AlertType.INFORMATION, "Success", "Equipment added successfully.");
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }


        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Form Error!", "Quantity must be a valid number.");
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void navigateToShowEquipment(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ShowEquipment.fxml"));
            Parent root = loader.load();

            // Get the current stage from the button (if applicable)
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Show Equipment");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle exception if the FXML loading fails
        }
    }
}
