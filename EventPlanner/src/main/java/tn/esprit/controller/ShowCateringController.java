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
import tn.esprit.entities.Catering;
import tn.esprit.services.CateringServices;

import java.io.IOException;
import java.sql.SQLException;

public class ShowCateringController {

    @FXML
    private TableView<Catering> cateringTable;

    @FXML
    private TableColumn<Catering, Integer> colCateringId;

    @FXML
    private TableColumn<Catering, String> colMenuType;

    @FXML
    private TableColumn<Catering, Integer> colNbrPlates;

    @FXML
    private TableColumn<Catering, Float> colPricing;

    @FXML
    private TableColumn<Catering, String> colMealSchedule;

    @FXML
    private TableColumn<Catering, String> colBeverages;

    @FXML
    private TableColumn<Catering, Integer> colVenueId;

    @FXML
    private TableColumn<Catering, Void> colUpdate;

    @FXML
    private TableColumn<Catering, Void> colDelete;

    @FXML
    void initialize() throws SQLException {
        CateringServices cateringServices = new CateringServices();
        try {
            ObservableList<Catering> observableList = FXCollections.observableList(cateringServices.returnList());
            cateringTable.setItems(observableList);

            colCateringId.setCellValueFactory(new PropertyValueFactory<>("cateringId"));
            colMenuType.setCellValueFactory(new PropertyValueFactory<>("menuType"));
            colNbrPlates.setCellValueFactory(new PropertyValueFactory<>("nbrPlates"));
            colPricing.setCellValueFactory(new PropertyValueFactory<>("pricing"));
            colMealSchedule.setCellValueFactory(new PropertyValueFactory<>("mealSchedule"));
            colBeverages.setCellValueFactory(new PropertyValueFactory<>("beverages"));
            colVenueId.setCellValueFactory(new PropertyValueFactory<>("venueId"));

            setUpButtons();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void setUpButtons() {
        colUpdate.setCellFactory(param -> new TableCell<Catering, Void>() {
            private final Button updateButton = new Button("Update");
            {
                updateButton.setOnAction(event -> {
                    Catering catering = getTableView().getItems().get(getIndex());
                    Stage stage = (Stage) updateButton.getScene().getWindow();
                    navigateToUpdateCatering(stage, catering);
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

        colDelete.setCellFactory(param -> new TableCell<Catering, Void>() {
            private final Button deleteButton = new Button("Delete");
            {
                deleteButton.setOnAction(event -> {
                    Catering catering = getTableView().getItems().get(getIndex());
                    try {
                        handleDeleteButtonClick(catering);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
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

    private void handleDeleteButtonClick(Catering catering) throws SQLException {
        CateringServices cateringServices = new CateringServices();
        cateringServices.delete(catering);
        refreshTable();
    }

    private void refreshTable() throws SQLException {
        CateringServices cateringServices = new CateringServices();
        try {
            ObservableList<Catering> updatedList = FXCollections.observableList(cateringServices.returnList());
            cateringTable.setItems(updatedList);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void navigateToAddCatering(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddCatering.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Catering");
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void navigateToUpdateCatering(Stage stage, Catering catering) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateCatering.fxml"));
            Parent root = loader.load();

            UpdateCateringController controller = loader.getController();
            controller.setCurrentCatering(catering);

            stage.setScene(new Scene(root));
            stage.setTitle("Update Catering");
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
