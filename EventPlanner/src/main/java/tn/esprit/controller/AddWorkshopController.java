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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AddWorkshopController {

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

    public void initialize() {
        try {
            List<Integer> partnerIDs = workshopService.getPartnerIDs();
            ObservableList<Integer> partnerIdList = FXCollections.observableArrayList();
            for (int id : partnerIDs) {
                partnerIdList.add(id);
            }
            partnerIdComboBox.setItems(partnerIdList);
            if (!partnerIdList.isEmpty()) {
                partnerIdComboBox.getSelectionModel().selectFirst();
            }
        } catch (Exception e) {
            System.out.println("Error fetching partner IDs: " + e.getMessage());
        }
    }

    @FXML
    public void handleAddWorkshop(ActionEvent event) {
        try {
            String title = titleField.getText().trim();
            String coach = coachField.getText().trim();
            String durationText = durationField.getText().trim();
            String startDateText = startDateField.getText().trim();
            String description = descriptionField.getText().trim();
            Integer partnerId = partnerIdComboBox.getValue();

            // Check if all fields are filled
            if (title.isEmpty() || coach.isEmpty() || description.isEmpty() || partnerId == null || durationText.isEmpty() || startDateText.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Form Error", "All fields must be completed.");
                return;
            }

            // Validate title and coach (only letters)
            if (!title.matches("[a-zA-Z\\s]+")) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Title must only contain letters.");
                return;
            }
            if (!coach.matches("[a-zA-Z\\s]+")) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Coach name must only contain letters.");
                return;
            }

            // Parse and validate duration (must be a positive number)
            int duration;
            try {
                duration = Integer.parseInt(durationText);
                if (duration <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "Duration must be a positive number.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Duration must be a valid number.");
                return;
            }

            // Parse and validate date (yyyy-MM-dd)
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            Date startDate;
            try {
                startDate = dateFormat.parse(startDateText);
            } catch (ParseException e) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid date format. Use yyyy-MM-dd.");
                return;
            }

            // Get today's date without time (to compare only dates, ignoring time of day)
            Date today = new Date();
            SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date todayWithoutTime = dateOnlyFormat.parse(dateOnlyFormat.format(today));
            Date startDateWithoutTime = dateOnlyFormat.parse(dateOnlyFormat.format(startDate));

            // Check if the start date is before today
            if (startDateWithoutTime.before(todayWithoutTime)) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Start date cannot be in the past.");
                return;
            }

            // Create and save the workshop
            Workshop workshop = new Workshop(0, title, coach, duration, startDate, description, partnerId);
            workshopService.addP(workshop);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Workshop added successfully.");

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred: " + e.getMessage());
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
