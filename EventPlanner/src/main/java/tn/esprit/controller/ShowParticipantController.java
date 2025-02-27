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
import tn.esprit.entities.*;
import tn.esprit.services.*;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.MapValueFactory;

import java.io.IOException;

public class ShowParticipantController {

    @FXML
    private TableView<Participant> ParticipantTable;

    @FXML
    private TableColumn<Participant, String> colAffiliation;

    @FXML
    private TableColumn<Participant, Integer> colAge;

    @FXML
    private TableColumn<Participant, String> colName;

    @FXML
    private TableColumn<Participant, Integer> colParticipantId;

    @FXML
    private TableColumn<Participant, String> colTeamName;

    @FXML
    private TableColumn<Participant, Void> colUpdate;

    @FXML
    private TableColumn<Participant, Void> colDelete;

    private final ParticipantService participantService = new ParticipantService();

    @FXML
    void initialize() {
        // Fetch data from the service
        ObservableList<Participant> observableList = FXCollections.observableList(participantService.returnList());
        ParticipantTable.setItems(observableList);

        // Bind columns to Participant properties
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAffiliation.setCellValueFactory(new PropertyValueFactory<>("affiliation"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        colParticipantId.setCellValueFactory(new PropertyValueFactory<>("participantId"));

        // Hide the participantId column (optional)
        colParticipantId.setVisible(false);

        // Custom cell value factory for team name
        colTeamName.setCellValueFactory(cellData -> {
            int teamId = cellData.getValue().getTeamid();
            String teamName = participantService.getTeamNameById(teamId); // Fetch team name by teamId
            return javafx.beans.binding.Bindings.createStringBinding(() -> teamName);
        });

        // Set up buttons for update and delete
        setUpButtons();
    }

    private void setUpButtons() {
        // Update Button
        colUpdate.setCellFactory(param -> new TableCell<Participant, Void>() {
            private final Button updateButton = new Button("Update");

            {
                updateButton.setOnAction(event -> {
                    Participant participant = getTableView().getItems().get(getIndex());
                    Stage stage = (Stage) updateButton.getScene().getWindow();
                    navigateToUpdateParticipant(stage, participant);
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
        colDelete.setCellFactory(param -> new TableCell<Participant, Void>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Participant participant = getTableView().getItems().get(getIndex());
                    handleDeleteButtonClick(participant);
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

    private void handleDeleteButtonClick(Participant participant) {
        System.out.println("Delete participant: " + participant);
        participantService.delete(participant);
        refreshTable();
    }

    private void refreshTable() {
        ObservableList<Participant> updatedList = FXCollections.observableList(participantService.returnList());
        ParticipantTable.setItems(updatedList);
    }

    @FXML
    public void navigateToAddParticipant(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddParticipant.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Add Participant");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void navigateToUpdateParticipant(Stage stage, Participant participant) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateParticipant.fxml"));
            Parent root = loader.load();

            UpdateParticipantController controller = loader.getController();
            controller.setParticipantData(participant);

            stage.setScene(new Scene(root));
            stage.setTitle("Update Participant");
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}