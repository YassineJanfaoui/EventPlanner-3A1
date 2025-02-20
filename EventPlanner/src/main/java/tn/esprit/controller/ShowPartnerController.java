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
import tn.esprit.entities.Partner;
import tn.esprit.services.PartnerServices;

import java.io.IOException;
import java.sql.SQLException;

public class ShowPartnerController {

    @FXML
    private TableView<Partner> partnerTable;

    @FXML
    private TableColumn<Partner, Integer> colPartnerId;

    @FXML
    private TableColumn<Partner, String> colName;

    @FXML
    private TableColumn<Partner, String> colCategory;

    @FXML
    private TableColumn<Partner, Void> colUpdate;

    @FXML
    private TableColumn<Partner, Void> colDelete;

    @FXML
    void initialize() {
        PartnerServices partnerServices = new PartnerServices();
        try {
            ObservableList<Partner> observableList = FXCollections.observableList(partnerServices.returnList());
            partnerTable.setItems(observableList);

            colPartnerId.setCellValueFactory(new PropertyValueFactory<>("partnerId"));
            colName.setCellValueFactory(new PropertyValueFactory<>("name"));
            colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));

            setUpButtons();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void setUpButtons() {
        colUpdate.setCellFactory(param -> new TableCell<Partner, Void>() {
            private final Button updateButton = new Button("Update");
            {
                updateButton.setOnAction(event -> {
                    Partner partner = getTableView().getItems().get(getIndex());
                    Stage stage = (Stage) updateButton.getScene().getWindow();
                    navigateToUpdatePartner(stage, partner);
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

        colDelete.setCellFactory(param -> new TableCell<Partner, Void>() {
            private final Button deleteButton = new Button("Delete");
            {
                deleteButton.setOnAction(event -> {
                    Partner partner = getTableView().getItems().get(getIndex());
                    handleDeleteButtonClick(partner);
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

    private void handleDeleteButtonClick(Partner partner) {
        PartnerServices partnerServices = new PartnerServices();
        partnerServices.delete(partner);
        refreshTable();
    }

    private void refreshTable() {
        PartnerServices partnerServices = new PartnerServices();
        try {
            ObservableList<Partner> updatedList = FXCollections.observableList(partnerServices.returnList());
            partnerTable.setItems(updatedList);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void navigateToAddPartner(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddPartner.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Partner");
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void navigateToUpdatePartner(Stage stage, Partner partner) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdatePartner.fxml"));
            Parent root = loader.load();

            UpdatePartnerController controller = loader.getController();
            controller.setCurrentPartner(partner);

            stage.setScene(new Scene(root));
            stage.setTitle("Update Equipment");
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
