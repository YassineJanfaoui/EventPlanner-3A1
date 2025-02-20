package tn.esprit.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SidebarController {

    @FXML
    public void navigateToPartner(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ShowPartner.fxml"));
            Parent root = loader.load();

            // Get the current stage from the button (if applicable)
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Show Partners");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle exception if the FXML loading fails
        }
    }


    @FXML
    public void navigateToWorkshop(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ShowWorkshop.fxml"));
            Parent root = loader.load();

            // Get the current stage from the button (if applicable)
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Show Workshops");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle exception if the FXML loading fails
        }
    }
}
