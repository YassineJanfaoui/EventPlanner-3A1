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
import tn.esprit.entities.Bill;
import tn.esprit.services.BillServices;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateBillController {

    @FXML
    private TextField amountField;
    @FXML
    private TextField dueDateField;
    @FXML
    private TextField descriptionField;
    @FXML
    private ComboBox<String> paymentStatusComboBox;
    @FXML
    private ComboBox<Integer> EventID;

    private final BillServices billService = new BillServices();
    public Bill currentBill;

    public void initialize() {
        try {
            int[] eventIDs = billService.eventIDs();
            System.out.println("Fetched event IDs: ");
            for(int eventID : eventIDs) {
                System.out.println(eventID);
            }
            ObservableList<Integer> eventIdList = FXCollections.observableArrayList();
            for (int id : eventIDs) {
                eventIdList.add(id);
            }
            EventID.setItems(eventIdList);

        } catch (Exception e) {
            System.out.println("Error fetching event IDs: " + e.getMessage());
        }
    }

    @FXML
    public void handleUpdateBill(ActionEvent event) {
        try {
            int amount = Integer.parseInt(amountField.getText().trim());
            String dueDateText = dueDateField.getText().trim();
            String description = descriptionField.getText().trim();
            String paymentStatus = paymentStatusComboBox.getValue();
            Integer eventId = EventID.getValue();

            if (description.isEmpty() || paymentStatus == null || eventId == null) {
                showAlert(Alert.AlertType.ERROR, "Form Error", "All fields must be completed.");
                return;
            }

            Date dueDate = new SimpleDateFormat("yyyy-MM-dd").parse(dueDateText);
            Bill bill = new Bill(currentBill.getBillId(), currentBill.getArchived(), amount, dueDate, description, paymentStatus, eventId);

            try {
                billService.update(bill);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Bill updated successfully.");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Amount must be a valid number.");
        } catch (ParseException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid date format. Use yyyy-MM-dd.");
        }
    }

    @FXML
    public void navigateToShowBill(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ShowBill.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Show Bills");
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setCurrentBill(Bill bill) {

        this.currentBill = bill;
        amountField.setText(String.valueOf(currentBill.getAmount()));
        dueDateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(currentBill.getDueDate()));
        descriptionField.setText(currentBill.getDescription());
        paymentStatusComboBox.setValue(currentBill.getPaymentStatus());
        EventID.getSelectionModel().selectFirst();

    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
