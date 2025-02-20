package com.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.esprit.models.Event;
import com.esprit.services.EventService;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class Afficherfront {

    @FXML
    private VBox eventContainer; // Container for event cards

    @FXML
    private HBox eventCard; // Example event card
    @FXML
    private ImageView pdpImageView; // ImageView for displaying the event image
    @FXML
    private TextField eventname; // Text field for event name
    @FXML
    private Button showdetails; // Button to show event details
    @FXML
    private Button btnAddEvent;
    private EventService eventService = new EventService();
    @FXML
    private Button btnModifyEvents;// Service to manage events
    private JColorChooser eventTable;

    @FXML
    public void loadData() {
        eventContainer.getChildren().clear();  // Clear existing event cards
        List<Event> events = eventService.rechercher();  // Load events from the service

        for (Event event : events) {
            addEventCard(event);  // Add each event as a card
        }
    }
    @FXML
    private void initialize() {
        loadData();
    }

    private void addEventCard(Event event) {
        HBox newEventCard = new HBox(15);
        newEventCard.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #7C81AB; -fx-border-radius: 10; -fx-padding: 10;");

        // Load image from the file system
        ImageView imageView = new ImageView();
        String imagePath = event.getImage(); // Assuming this returns the correct image path

        if (imagePath != null && !imagePath.isEmpty()) {
            Image image = new Image("file:" + imagePath); // Load image from file system
            imageView.setImage(image);
        } else {
            // Set a placeholder image if the path is empty or null
            imageView.setImage(new Image("file:event.png")); // Replace with your placeholder image path
        }

        imageView.setFitHeight(150);
        imageView.setFitWidth(200);
        imageView.setPreserveRatio(true);

        VBox detailsBox = new VBox(5);
        detailsBox.setAlignment(javafx.geometry.Pos.CENTER);

        TextField nameField = new TextField(event.getName()); // Assuming getName() returns the event name
        nameField.setEditable(false); // Make it read-only
        nameField.setStyle("-fx-font-size: 18px; -fx-text-fill: #2A2A72; -fx-background-color: transparent; -fx-border: none;");

        Button detailsButton = new Button("Show Details");
        detailsButton.setStyle("-fx-background-color: #7C81AB; -fx-text-fill: white;");
        detailsButton.setOnAction(e -> showEventDetails(event)); // Add action to show event details

        detailsBox.getChildren().addAll(nameField, detailsButton);
        newEventCard.getChildren().addAll(imageView, detailsBox);
        eventContainer.getChildren().add(newEventCard);

        // Set up right-click menu for the event card
        setupContextMenu(newEventCard, event);
    }

    private void showEventDetails(Event event) {
       /* try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addfront.fxml")); // Path to your event details FXML
            Parent root = loader.load();
            Details detailsController = loader.getController();
            detailsController.setEvent(event); // Assuming you have a method to set the event details

            Stage stage = (Stage) eventContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    @FXML
    private void handleaddEvents(ActionEvent event) {
        try {
            // Load the event2.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Addfront.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) btnAddEvent.getScene().getWindow();

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
    private void handleEventButton(ActionEvent event) {
        try {
            // Load the addfront.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addfront.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene with the addfront.fxml content
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupContextMenu(HBox eventCard, Event event) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem deleteItem = new MenuItem("Supprimer");
        deleteItem.setOnAction(e -> {
            if (event != null) {
                confirmAndDeleteEvent(event);
            }
        });

        contextMenu.getItems().add(deleteItem);

        eventCard.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(eventCard, e.getScreenX(), e.getScreenY());
            } else {
                contextMenu.hide();
            }
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
                loadData(); // Reload the data after deletion
                showAlert("Suppression réussie", "L'événement a été supprimé avec succès.");
            }
        });
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }


    public void handlemodifyevents(ActionEvent actionEvent) {
        try {
            // Load the event2.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Modifyfront.fxml"));
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
