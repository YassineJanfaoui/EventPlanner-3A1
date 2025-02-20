package tn.esprit.controller;

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
import tn.esprit.entities.Workshop;
import tn.esprit.services.WorkshopServices;

import java.io.IOException;
import java.sql.SQLException;

public class ShowWorkshopController {

    @FXML
    private TableView<Workshop> workshopTable;

    @FXML
    private TableColumn<Workshop, Integer> colWorkshopID;

    @FXML
    private TableColumn<Workshop, String> colTitle;

    @FXML
    private TableColumn<Workshop, String> colCoach;

    @FXML
    private TableColumn<Workshop, Integer> colDuration;

    @FXML
    private TableColumn<Workshop, String> colStartDate;

    @FXML
    private TableColumn<Workshop, String> colDescription;

    @FXML
    private TableColumn<Workshop, Integer> colPartnerID;

    @FXML
    private TableColumn<Workshop, Void> colUpdate;

    @FXML
    private TableColumn<Workshop, Void> colDelete;

    @FXML
    private Button addButton;

    @FXML
    void initialize() {
        WorkshopServices workshopServices = new WorkshopServices();
        try {
            ObservableList<Workshop> observableList = FXCollections.observableList(workshopServices.returnList());
            workshopTable.setItems(observableList);

            colWorkshopID.setCellValueFactory(new PropertyValueFactory<>("workshopId"));
            colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
            colCoach.setCellValueFactory(new PropertyValueFactory<>("coach"));
            colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
            colStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
            colPartnerID.setCellValueFactory(new PropertyValueFactory<>("partnerId"));


            setUpButtons();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void setUpButtons() {
        colUpdate.setCellFactory(param -> new TableCell<Workshop, Void>() {
            private final Button updateButton = new Button("Update");
            {
                updateButton.setOnAction(event -> {
                    Workshop workshop = getTableView().getItems().get(getIndex());
                    Stage stage = (Stage) updateButton.getScene().getWindow();
                    navigateToUpdateWorkshop(stage, workshop);
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

        colDelete.setCellFactory(param -> new TableCell<Workshop, Void>() {
            private final Button deleteButton = new Button("Delete");
            {
                deleteButton.setOnAction(event -> {
                    Workshop workshop = getTableView().getItems().get(getIndex());
                    handleDeleteButtonClick(workshop);
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

    private void handleDeleteButtonClick(Workshop workshop) {
        WorkshopServices workshopServices = new WorkshopServices();
        workshopServices.delete(workshop);
        refreshTable();
    }

    private void refreshTable() {
        WorkshopServices workshopServices = new WorkshopServices();
        try {
            ObservableList<Workshop> updatedList = FXCollections.observableList(workshopServices.returnList());
            workshopTable.setItems(updatedList);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void navigateToAddWorkshop(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddWorkshop.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Workshop");
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void navigateToUpdateWorkshop(Stage stage, Workshop workshop) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateWorkshop.fxml"));
            Parent root = loader.load();

            UpdateWorkshopController controller = loader.getController();
            controller.setCurrentWorkshop(workshop);

            stage.setScene(new Scene(root));
            stage.setTitle("Update Workshop");
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
