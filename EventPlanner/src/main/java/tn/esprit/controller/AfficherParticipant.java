package tn.esprit.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import tn.esprit.entities.Participant;
import tn.esprit.services.ParticipantService;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class AfficherParticipant {

    @FXML
    private TableView<Participant> participantTable;
    @FXML
    private TableColumn<Participant, Integer> colId;
    @FXML
    private TableColumn<Participant, String> colName;
    @FXML
    private TableColumn<Participant, String> colAffiliation;
    @FXML
    private TableColumn<Participant, Integer> colAge;
    @FXML
    private TableColumn<Participant, Integer> colTeamId;
    @FXML
    private Button btnadd;
    @FXML
    private Button btnModifyEvent;
    private final ObservableList<Participant> data = FXCollections.observableArrayList();
    private final ParticipantService participantService = new ParticipantService();

    @FXML
    public void initialize() {
        participantTable.getStylesheets().add(getClass().getResource("/styles/button.css").toExternalForm());
        configureTableColumns();
        loadData();
        setupContextMenu();
        participantTable.setOnMouseClicked(event -> handleRowDoubleClick(event));

    }

    private void configureTableColumns() {
        // Adjusting PropertyValueFactory to match the field names in the Participant class
        colId.setCellValueFactory(new PropertyValueFactory<>("participantId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAffiliation.setCellValueFactory(new PropertyValueFactory<>("affiliation"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        colTeamId.setCellValueFactory(new PropertyValueFactory<>("teamid"));
    }

    @FXML
    public void loadData() {
        data.setAll(participantService.rechercher()); // Assuming `rechercher` returns a list of `Participant` objects
        participantTable.setItems(data);
    }

    @FXML
    private void refreshTable(ActionEvent event) {
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

    // Setting up context menu with delete functionality
    private void setupContextMenu() {
        participantTable.setRowFactory(tv -> {
            TableRow<Participant> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();

            // Option "Supprimer"
            MenuItem deleteItem = new MenuItem("Supprimer");
            deleteItem.setOnAction(event -> {
                Participant selectedParticipant = row.getItem();
                if (selectedParticipant != null) {
                    confirmAndDeleteParticipant(selectedParticipant);
                }
            });

            // Ajouter l'option au menu contextuel
            contextMenu.getItems().add(deleteItem);

            // Afficher le menu contextuel uniquement au clic droit
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

    // Confirming and deleting the participant
    private void confirmAndDeleteParticipant(Participant participant) {
        if (participant == null) {
            showAlert("Erreur", "Aucun participant sélectionné.");
            return;
        }

        // Displaying confirmation dialog
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation de suppression");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Voulez-vous vraiment supprimer ce participant ?");

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Perform the deletion in the service layer
                participantService.supprimer(participant);

                // Update the participant table
                participantTable.getItems().remove(participant);
                participantTable.refresh();

                showAlert("Suppression réussie", "Le participant a été supprimé avec succès.");
            }
        });
    }


    /*@FXML
    private void goToAjouterParticipant(ActionEvent event) {
        try {
            // Load the add participant page (adjust the path accordingly)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/participant.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Change the scene
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
    @FXML
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
    private void handleModify(ActionEvent event) {
        try {
            // Load the event2.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/viewparticipant.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) btnadd.getScene().getWindow();

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
    public void handleRowDoubleClick(MouseEvent event) {
        if (event.getClickCount() == 2) { // Ensure it's a double-click event
            Participant selectedEvent = participantTable.getSelectionModel().getSelectedItem();

            if (selectedEvent != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifyparticipant.fxml"));
                    Parent root = loader.load();

                    // Get controller and pass the selected event data
                    ModifyParticipant modifierController = loader.getController();
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
    private void handleModifyEven(ActionEvent event) {
        try {
            // Load the event2.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/event3.fxml"));
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
    private void goToAjout(ActionEvent event) {
        try {
            // Charger la nouvelle page (ajoutez le bon chemin FXML)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/event3.fxml"));
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
