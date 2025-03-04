package tn.esprit.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.entities.Bill;
import tn.esprit.services.BillServices;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
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
    private TextField searchBill;
    private String currentSearchQuery = "";

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
            searchBill.textProperty().addListener((observable, oldValue, newValue) -> handleSearchBill(newValue));
            applyRowColoring();
            showNotification();
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
        handleSearchBill(currentSearchQuery);
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
    @FXML
    public void exportToTextFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            saveBillsToFile(file);
        }
    }

    private void saveBillsToFile(File file) {
        BillServices billServices = new BillServices();
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("Bill ID | Amount | Due Date | Description | Payment Status | Event ID\n");
            writer.write("---------------------------------------------------------------------\n");

            ObservableList<Bill> bills = FXCollections.observableList(billServices.returnList());
            for (Bill bill : bills) {
                writer.write(String.format("%d | %.2f | %s | %s | %s | %d\n",
                        bill.getBillId(),
                        Double.valueOf(bill.getAmount()), // Convert to double
                        bill.getDueDate(),
                        bill.getDescription(),
                        bill.getPaymentStatus(),
                        bill.getEventID()));
            }

            System.out.println("Exported successfully!");
        } catch (IOException e) {
            System.out.println("Error exporting: " + e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void handleSearchBill(String searchQuery) {
        BillServices billServices = new BillServices();
        currentSearchQuery = searchQuery;
        try {
            ObservableList<Bill> allBills = FXCollections.observableList(billServices.returnList());
            ObservableList<Bill> filteredBills = FXCollections.observableArrayList(billServices.searchBillsByDescription(searchQuery));
            if (searchQuery == null || searchQuery.isEmpty()) {
                billTable.setItems(allBills);
                applyRowColoring();
            } else {
                billTable.setItems(filteredBills);
                applyRowColoring();
            }


        } catch (SQLException e) {
            System.out.println("Error fetching bills: " + e.getMessage());
        }
    }
    private void applyRowColoring() {
        billTable.setRowFactory(tv -> {
            TableRow<Bill> row = new TableRow<>();
            row.itemProperty().addListener((observable, oldBill, newBill) -> {
                if (newBill != null) {
                    // Apply coloring based on the payment status
                    String paymentStatus = newBill.getPaymentStatus();
                    if ("Pending".equals(paymentStatus)) {
                        row.setStyle("-fx-background-color: lightcoral;");
                    } else if ("Paid".equals(paymentStatus)) {
                        row.setStyle("-fx-background-color: lightgreen;");
                    } else {
                        row.setStyle("");  // Reset style if the status doesn't match
                    }
                } else {
                    // Reset the style when the row is empty (no item in the row)
                    row.setStyle("");
                }
            });
            return row;
        });
    }
    public void showNotification(){
        BillServices billServices = new BillServices();
        try {
            int billsDue = billServices.getBillsDueInAWeek();
            String billsDueString = String.valueOf(billsDue);
            String message;
            if(billsDue>1)
                message = "You have "+billsDueString+" bills due next week or sooner.";
            else if(billsDue==1)
                message = "You have "+billsDueString+" bill due next week";
            else  {
                message="You have no upcoming bills.";
            }
            if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().createImage("icon.png");

            TrayIcon trayIcon = new TrayIcon(image, "Java Notification");
            trayIcon.setImageAutoSize(true);
            trayIcon.setToolTip("Bills Due");

            try {
                tray.add(trayIcon);
                trayIcon.displayMessage("Reminder", message, TrayIcon.MessageType.INFO);
            } catch (AWTException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("System tray not supported!");
        }} catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
