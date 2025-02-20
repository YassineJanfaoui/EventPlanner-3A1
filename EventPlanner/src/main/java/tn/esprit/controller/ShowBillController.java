package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import tn.esprit.entities.Bill;
import tn.esprit.services.BillServices;

import java.io.IOException;
import java.sql.SQLException;

public class ShowBillController {

    @FXML
    private TableView<Bill> billTable;

    @FXML
    private TableColumn<Bill, Integer> colBillId;

    @FXML
    private TableColumn<Bill, Double> colAmount;

    @FXML
    private TableColumn<Bill, String> colDueDate;

    @FXML
    private TableColumn<Bill, String> colDescription;

    @FXML
    private TableColumn<Bill, String> colPaymentStatus;

    @FXML
    private TableColumn<Bill, Integer> colEventID;

    @FXML
    private TableColumn<Bill, Void> colUpdate;

    @FXML
    private TableColumn<Bill, Void> colDelete;

    @FXML
    void initialize() {
        BillServices billServices = new BillServices();
        try {
            ObservableList<Bill> observableList = FXCollections.observableList(billServices.returnList());
            billTable.setItems(observableList);

            colBillId.setCellValueFactory(new PropertyValueFactory<>("billId"));
            colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
            colDueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
            colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
            colPaymentStatus.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
            colEventID.setCellValueFactory(new PropertyValueFactory<>("EventID"));
            colBillId.setVisible(false);

            setUpButtons();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void setUpButtons() {
        colUpdate.setCellFactory(param -> new TableCell<Bill, Void>() {
            private final Button updateButton = new Button("Update");
            {
                updateButton.setOnAction(event -> {
                    Bill bill = getTableView().getItems().get(getIndex());
                    Stage stage = (Stage) updateButton.getScene().getWindow();
                    navigateToUpdateBill(stage,bill);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(updateButton);
                }
            }
        });

        colDelete.setCellFactory(param -> new TableCell<Bill, Void>() {
            private final Button deleteButton = new Button("Delete");
            {
                deleteButton.setOnAction(event -> {
                    Bill bill = getTableView().getItems().get(getIndex());
                    handleDeleteButtonClick(bill);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
    }

    private void handleDeleteButtonClick(Bill bill) {
        BillServices billServices = new BillServices();
        billServices.delete(bill);
        refreshTable();
    }

    private void refreshTable() {
        BillServices billServices = new BillServices();
        try {
            ObservableList<Bill> updatedList = FXCollections.observableList(billServices.returnList());
            billTable.setItems(updatedList);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void navigateToAddBill(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddBill.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Bill");
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

  public void navigateToUpdateBill(Stage stage,Bill bill) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateBill.fxml"));
            Parent root = loader.load();

            UpdateBillController controller = loader.getController();
            controller.setCurrentBill(bill);

            stage.setScene(new Scene(root));
            stage.setTitle("Update Equipment");
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
