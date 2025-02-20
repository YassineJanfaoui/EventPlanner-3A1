package tn.esprit.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import tn.esprit.entities.Equipment;
import tn.esprit.services.EquipmentServices;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;

import java.io.IOException;
import java.sql.SQLException;

public class ShowEquipmentController {

    @FXML
    private TableView<Equipment> equipmentTable;

    @FXML
    private TableColumn<Equipment, String> colName;

    @FXML
    private TableColumn<Equipment, String> colState;

    @FXML
    private TableColumn<Equipment, String> colCategory;

    @FXML
    private TableColumn<Equipment, Integer> colQuantity;

    @FXML
    private TableColumn<Equipment, Void> colUpdate;

    @FXML
    private TableColumn<Equipment, Void> colDelete;

    @FXML
    private TableColumn<Equipment, Integer> colEquipmentId; // Equipment ID column

    @FXML
    void initialize() {
        EquipmentServices equipmentServices = new EquipmentServices();
        try {
            ObservableList<Equipment> observableList = FXCollections.observableList(equipmentServices.returnList());

            equipmentTable.setItems(observableList);
            colName.setCellValueFactory(new PropertyValueFactory<>("name"));
            colState.setCellValueFactory(new PropertyValueFactory<>("state"));
            colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
            colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            colEquipmentId.setCellValueFactory(new PropertyValueFactory<>("EquipmentId"));

            colEquipmentId.setVisible(false);

            setUpButtons();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error loading equipment data", e);
        }
    }

    private void setUpButtons() {
        colUpdate.setCellFactory(param -> new TableCell<Equipment, Void>() {
            private final Button updateButton = new Button("Update");
            {
                updateButton.setOnAction(event -> {
                    Equipment equipment = getTableView().getItems().get(getIndex());
                    Stage stage = (Stage) updateButton.getScene().getWindow();
                    navigateToUpdateEquipment(stage, equipment);
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

        // Delete Button
        colDelete.setCellFactory(param -> new TableCell<Equipment, Void>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Equipment equipment = getTableView().getItems().get(getIndex());
                    handleDeleteButtonClick(equipment);
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

    private void handleDeleteButtonClick(Equipment equipment) {
        System.out.println("Delete equipment: " + equipment);
        EquipmentServices equipmentServices = new EquipmentServices();
        equipmentServices.delete(equipment);
        refreshTable();
    }

    private void refreshTable() {
        EquipmentServices equipmentServices = new EquipmentServices();
        try {
            ObservableList<Equipment> updatedList = FXCollections.observableList(equipmentServices.returnList());
            equipmentTable.setItems(updatedList);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error refreshing equipment data", e);
        }
    }

    @FXML
    public void navigateToAddEquipment(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddEquipment.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Add Equipment");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void navigateToUpdateEquipment(Stage stage, Equipment equipment) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateEquipment.fxml"));
            Parent root = loader.load();

            UpdateEquipmentController controller = loader.getController();
            controller.setEquipmentData(equipment);

            stage.setScene(new Scene(root));
            stage.setTitle("Update Equipment");
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
