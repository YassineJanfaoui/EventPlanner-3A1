package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SidebarController {
    private Button equipmentButton;

    // This method will be called when the "Equipment" button is clicked
    @FXML
    public void navigateToEquipment(ActionEvent event) {
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
