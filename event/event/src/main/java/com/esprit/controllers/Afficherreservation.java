package com.esprit.controllers;

import com.esprit.models.Reservation;
import com.esprit.services.ReservationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.IOException;

public class Afficherreservation {

    @FXML
    private TableView<Reservation> reservationTable;
    @FXML
    private TableColumn<Reservation, Integer> colEventVenueId;
    @FXML
    private TableColumn<Reservation, Integer> colVenueId;
    @FXML
    private TableColumn<Reservation, Integer> colEventId;
    @FXML
    private TableColumn<Reservation, String> colReservationDate;
    @FXML
    private TableColumn<Reservation, String> colReservationPrice;
    @FXML
    private Button btnModifyEvents;

    @FXML
    private Button btnModifyReservation;

    private final ObservableList<Reservation> data = FXCollections.observableArrayList();
    private final ReservationService reservationService = new ReservationService();

    @FXML
    public void initialize() {
        reservationTable.getStylesheets().add(getClass().getResource("/styles/button.css").toExternalForm());
        configureTableColumns();
        loadData();
        setupContextMenu();
        reservationTable.setOnMouseClicked(event -> handleRowDoubleClick(event));
    }

    private void configureTableColumns() {
        colEventVenueId.setCellValueFactory(new PropertyValueFactory<>("eventVenueId"));
        colVenueId.setCellValueFactory(new PropertyValueFactory<>("venueId"));
        colEventId.setCellValueFactory(new PropertyValueFactory<>("eventId"));
        colReservationDate.setCellValueFactory(new PropertyValueFactory<>("reservationDate"));
        colReservationPrice.setCellValueFactory(new PropertyValueFactory<>("reservationPrice"));
    }

    @FXML
    public void loadData() {
        data.setAll(reservationService.rechercher()); // Assuming `rechercher` returns a list of `Reservation` objects
        reservationTable.setItems(data);
    }

    @FXML
    private void refreshTable() {
        loadData();
        showAlert("Mise à jour", "Les données ont été rafraîchies avec succès.");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void setupContextMenu() {
        reservationTable.setRowFactory(tv -> {
            TableRow<Reservation> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();

            // Option "Supprimer"
            MenuItem deleteItem = new MenuItem("Supprimer");
            deleteItem.setOnAction(event -> {
                Reservation selectedReservation = row.getItem();
                if (selectedReservation != null) {
                    confirmAndDeleteReservation(selectedReservation);
                }
            });

            contextMenu.getItems().add(deleteItem);

            row.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY && !row.isEmpty()) {
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                } else {
                    contextMenu.hide();
                }
            });

            return row;
        });
    }

    private void confirmAndDeleteReservation(Reservation reservation) {
        if (reservation == null) {
            showAlert("Erreur", "Aucune réservation sélectionnée.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation de suppression");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Voulez-vous vraiment supprimer cette réservation ?");

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                reservationService.supprimer(reservation);
                reservationTable.getItems().remove(reservation);
                reservationTable.refresh();
                showAlert("Suppression réussie", "La réservation a été supprimée avec succès.");
            }
        });
    }

    @FXML
    private void handleModifyReservation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifyReservation.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnModifyReservation.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleRowDoubleClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Reservation selectedReservation = reservationTable.getSelectionModel().getSelectedItem();

            if (selectedReservation != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifyReservation.fxml"));
                    Parent root = loader.load();
                    Modifyreservation modifierController = loader.getController();
                    modifierController.initData(selectedReservation);

                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Modifier Réservation");
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert("Erreur", "Impossible d'ouvrir la fenêtre de modification.");
                }
            } else {
                showAlert("Erreur", "Veuillez sélectionner une réservation.");
            }
        }
    }@FXML
    private void goToAjouterEventss(ActionEvent event) {
        try {
            // Charger la nouvelle page (ajoutez le bon chemin FXML)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEvents.fxml"));
            Parent root = loader.load();

            // Récupérer la scène actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Changer la scène
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleModifyEvents(ActionEvent event) {
        try {
            // Load the event2.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addreservation.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) btnModifyEvents.getScene().getWindow();

            // Set the new scene with the event2.fxml content
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}