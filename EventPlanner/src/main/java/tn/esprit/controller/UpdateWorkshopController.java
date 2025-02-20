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
import tn.esprit.entities.Workshop;
import tn.esprit.services.WorkshopServices;

import java.io.IOException;
import java.util.List;

public class UpdateWorkshopController {

    @FXML
    private TextField titleField;
    @FXML
    private TextField coachField;
    @FXML
    private TextField durationField;
    @FXML
    private TextField startDateField;
    @FXML
    private TextField descriptionField;
    @FXML
    private ComboBox<Integer> partnerIdComboBox;

    private final WorkshopServices workshopService = new WorkshopServices();
    private Workshop currentWorkshop;

    public void initialize() {
        try {
            List<Integer> partnerIDs = workshopService.getPartnerIDs();
            ObservableList<Integer> partnerIdList = FXCollections.observableArrayList(partnerIDs);
            partnerIdComboBox.setItems(partnerIdList);
        } catch (Exception e) {
            System.out.println("Error fetching partner IDs: " + e.getMessage());
        }
    }

    public void setCurrentWorkshop(Workshop workshop) {
        this.currentWorkshop = workshop;
        titleField.setText(workshop.getTitle());
        coachField.setText(workshop.getCoach());
        durationField.setText(String.valueOf(workshop.getDuration()));
        startDateField.setText(workshop.getStartDate().toString());
        descriptionField.setText(workshop.getDescription());
        partnerIdComboBox.setValue(workshop.getPartnerId());
    }

    @FXML
    public void handleUpdateWorkshop(ActionEvent event) {
        if (currentWorkshop == null) {
            showAlert(Alert.AlertType.ERROR, "Update Error", "No workshop selected.");
            return;
        }

        try {
            currentWorkshop.setTitle(titleField.getText().trim());
            currentWorkshop.setCoach(coachField.getText().trim());
            currentWorkshop.setDuration(Integer.parseInt(durationField.getText().trim()));
            currentWorkshop.setStartDate(java.sql.Date.valueOf(startDateField.getText().trim()));
            currentWorkshop.setDescription(descriptionField.getText().trim());
            currentWorkshop.setPartnerId(partnerIdComboBox.getValue());

            workshopService.update(currentWorkshop);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Workshop updated successfully.");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Duration must be a valid number.");
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid date format. Use yyyy-MM-dd.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Update Error", "Error updating workshop: " + e.getMessage());
        }
    }

    @FXML
    public void navigateToShowWorkshop(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ShowWorkshop.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Show Workshops");
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
