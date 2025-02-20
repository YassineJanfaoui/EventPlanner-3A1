package com.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import com.esprit.models.Event;
import com.esprit.services.EventService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class AfficherEvents {

    @FXML
    private TableView<Event> eventTable;
    @FXML
    private TableColumn<Event, Integer> colId;
    @FXML
    private TableColumn<Event, String> colName;
    @FXML
    private TableColumn<Event, String> colStartDate;
    @FXML
    private TableColumn<Event, String> colEndDate;
    @FXML
    private TableColumn<Event, Integer> colMaxParticipants;
    @FXML
    private TableColumn<Event, String> colDescription;
    @FXML
    private TableColumn<Event, Integer> colFee;
    @FXML
    private TableColumn<Event, Integer> colUserId;
    @FXML
    private TableColumn<Event, String> colImage;
    @FXML
    private Button btnModifyEvent; // Button to navigate to Modify Event
    @FXML
    private Button btnModifyEvents;

    private ObservableList<Event> data = FXCollections.observableArrayList();
    private EventService eventService = new EventService();

    @FXML
    public void initialize() {
        loadData();
        setupContextMenu();
        eventTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                handleRowDoubleClick(event);
            }
        });
    }

    @FXML
    public void loadData() {
        data.clear();  // Clear the existing data
        data.addAll(eventService.rechercher());  // Load data from the service

        colId.setCellValueFactory(new PropertyValueFactory<>("eventId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        colMaxParticipants.setCellValueFactory(new PropertyValueFactory<>("maxParticipants"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colFee.setCellValueFactory(new PropertyValueFactory<>("fee"));
        colUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colImage.setCellValueFactory(new PropertyValueFactory<>("image"));  // Get the image path

        // Set custom cell factory for image column
        colImage.setCellFactory(param -> new TableCell<Event, String>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);

                if (empty || imagePath == null || imagePath.isEmpty()) {
                    setGraphic(null);
                } else {
                    Image image = new Image("file:" + imagePath);  // Load image from file system
                    imageView.setImage(image);
                    imageView.setFitWidth(100);
                    imageView.setFitHeight(100);
                    imageView.setPreserveRatio(true);
                    setGraphic(imageView);
                }
            }
        });

        eventTable.setItems(data);  // Bind data to TableView
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

    @FXML
    private void goToAjouterEvent(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/event.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupContextMenu() {
        eventTable.setRowFactory(tv -> {
            TableRow<Event> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();

            MenuItem deleteItem = new MenuItem("Supprimer");
            deleteItem.setOnAction(event -> {
                Event selectedEvent = row.getItem();
                if (selectedEvent != null) {
                    confirmAndDeleteEvent(selectedEvent);
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

    private void confirmAndDeleteEvent(Event event) {
        if (event == null) {
            showAlert("Erreur", "Aucun événement sélectionné.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation de suppression");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Voulez-vous vraiment supprimer cet événement ?");

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                eventService.supprimer(event);
                data.remove(event);
                eventTable.getItems().remove(event);
                eventTable.refresh();
                showAlert("Suppression réussie", "L'événement a été supprimé avec succès.");
            }
        });
    }

    @FXML
    public void handleRowDoubleClick(MouseEvent event) {
        if (event.getClickCount() == 2) { // Ensure it's a double-click event
            Event selectedEvent = eventTable.getSelectionModel().getSelectedItem();

            if (selectedEvent != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/event2.fxml"));
                    Parent root = loader.load();

                    // Get controller and pass the selected event data
                    ModifierEvent modifierController = loader.getController();
                    modifierController.initData(selectedEvent);

                    // Open modification window
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Modifier Événement");
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert("Erreur", "Impossible d'ouvrir la fenêtre de modification.");
                }
            } else {
                showAlert("Erreur", "Veuillez sélectionner un événement.");
            }
        }
    }

    @FXML
    private void handleModifyEvent(ActionEvent event) {
        try {
            // Load the event2.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/event2.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) btnModifyEvent.getScene().getWindow();

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
    @FXML
    private void handleModifyEvents(ActionEvent event) {
        try {
            // Load the event2.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/event.fxml"));
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
    @FXML
    private void goToAjouterEventsss(ActionEvent event) {
        try {
            // Charger la nouvelle page (ajoutez le bon chemin FXML)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherreservation.fxml"));
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
    }



