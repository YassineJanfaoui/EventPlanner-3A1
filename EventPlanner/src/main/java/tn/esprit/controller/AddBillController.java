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
import javafx.util.Callback;
import tn.esprit.entities.Bill;
import tn.esprit.services.BillServices;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class AddBillController {

    @FXML
    private TextField amountField;
    @FXML
    private DatePicker dueDateField;
    @FXML
    private TextField descriptionField;
    @FXML
    private ComboBox<String> paymentStatusComboBox;
    @FXML
    private ComboBox<String> EventID;

    private final BillServices billService = new BillServices();

    public void initialize() {
        try {
            String[] eventNames = billService.eventNames();
            System.out.println("Fetched event IDs: ");
            ObservableList<String> eventIdList = FXCollections.observableArrayList();

            for (String id : eventNames) {
                eventIdList.add(id);
            }
            EventID.setItems(eventIdList);
            if (!eventIdList.isEmpty()) {
                EventID.getSelectionModel().selectFirst();
            }
        }
     catch(Exception e)
    {
        System.out.println("Error fetching event IDs: " + e.getMessage());
    }
        LocalDate currentDate = LocalDate.now();
        dueDateField.setValue(currentDate);
        dueDateField.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        // Disable past dates
                        if (date.isBefore(currentDate)) {
                            setDisable(true); // Disable past dates
                            setStyle("-fx-background-color: #f0f0f0;"); // Optional: grey out the disabled dates
                        }
                    }
                };
            }
        });
}

    @FXML
    public void handleAddBill(ActionEvent event) {
        try {
            int amount = Integer.parseInt(amountField.getText().trim());
            String dueDateText = dueDateField.getValue().toString();
            String description = descriptionField.getText().trim();
            String paymentStatus = paymentStatusComboBox.getValue();
            int eventId = billService.getEventIDByName(EventID.getValue());

            if (description.isEmpty() || paymentStatus == null || eventId == -1) {
                showAlert(Alert.AlertType.ERROR, "Form Error", "All fields must be completed.");
                return;
            }
            Date dueDate = new SimpleDateFormat("yyyy-MM-dd").parse(dueDateText);
            Bill bill = new Bill(0,0,amount, dueDate, description, paymentStatus, eventId);
            try{
                billService.addP(bill);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Bill added successfully.");
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Amount must be a valid number.");
        } catch (ParseException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid date format. Use yyyy-MM-dd.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
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

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
