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
import tn.esprit.entities.Venue;
import tn.esprit.services.VenueServices;

import java.io.IOException;
import java.sql.SQLException;

public class ShowVenueController {

    @FXML
    private TableView<Venue> venueTable;

    @FXML
    private TableColumn<Venue, Integer> colVenueId;

    @FXML
    private TableColumn<Venue, String> colVenueName;

    @FXML
    private TableColumn<Venue, String> colLocation;

    @FXML
    private TableColumn<Venue, Integer> colNbrPlaces;

    @FXML
    private TableColumn<Venue, String> colAvailability;

    @FXML
    private TableColumn<Venue, Float> colCost;

    @FXML
    private TableColumn<Venue, String> colParking;

    @FXML
    private TableColumn<Venue, Void> colUpdate;

    @FXML
    private TableColumn<Venue, Void> colDelete;

    @FXML
    void initialize() throws SQLException {
        VenueServices venueServices = new VenueServices();
        try {
            ObservableList<Venue> observableList = FXCollections.observableList(venueServices.returnList());
            venueTable.setItems(observableList);

            colVenueId.setCellValueFactory(new PropertyValueFactory<>("venueId"));
            colVenueName.setCellValueFactory(new PropertyValueFactory<>("venueName"));
            colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
            colNbrPlaces.setCellValueFactory(new PropertyValueFactory<>("nbrPlaces"));
            colAvailability.setCellValueFactory(new PropertyValueFactory<>("availability"));
            colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
            colParking.setCellValueFactory(new PropertyValueFactory<>("parking"));

            setUpButtons();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void setUpButtons() {
        colUpdate.setCellFactory(param -> new TableCell<Venue, Void>() {
            private final Button updateButton = new Button("Update");
            {
                updateButton.setOnAction(event -> {
                    Venue venue = getTableView().getItems().get(getIndex());
                    Stage stage = (Stage) updateButton.getScene().getWindow();
                    navigateToUpdateVenue(stage, venue);
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

        colDelete.setCellFactory(param -> new TableCell<Venue, Void>() {
            private final Button deleteButton = new Button("Delete");
            {
                deleteButton.setOnAction(event -> {
                    Venue venue = getTableView().getItems().get(getIndex());
                    try {
                        handleDeleteButtonClick(venue);
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

    private void handleDeleteButtonClick(Venue venue) throws SQLException {
        VenueServices venueServices = new VenueServices();
        venueServices.delete(venue);
        refreshTable();
    }

    private void refreshTable() throws SQLException {
        VenueServices venueServices = new VenueServices();
        try {
            ObservableList<Venue> updatedList = FXCollections.observableList(venueServices.returnList());
            venueTable.setItems(updatedList);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void navigateToAddVenue(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddVenue.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Venue");
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void navigateToUpdateVenue(Stage stage, Venue venue) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateVenue.fxml"));
            Parent root = loader.load();

            UpdateVenueController controller = loader.getController();
            controller.setCurrentVenue(venue);

            stage.setScene(new Scene(root));
            stage.setTitle("Update Venue");
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}