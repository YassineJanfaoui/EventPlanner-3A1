package tn.esprit.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import tn.esprit.entities.Feedback;
import tn.esprit.services.FeedbackServices;

import java.io.IOException;
import java.sql.SQLException;

public class ShowFeedbackController {

    @FXML
    private TableView<Feedback> feedbackTable;

    @FXML
    private TableColumn<Feedback, Integer> colFeedbackId;

    @FXML
    private TableColumn<Feedback, String> colComment;

    @FXML
    private TableColumn<Feedback, Integer> colRate;

    @FXML
    private TableColumn<Feedback, Integer> colTeamId;

    @FXML
    private TableColumn<Feedback, Integer> colEventId;

    @FXML
    private TableColumn<Feedback, Void> colUpdate;

    @FXML
    private TableColumn<Feedback, Void> colDelete;

    @FXML
    void initialize() {
        FeedbackServices feedbackServices = new FeedbackServices();
        try {
            ObservableList<Feedback> observableList = FXCollections.observableList(feedbackServices.returnList());
            feedbackTable.setItems(observableList);
            colFeedbackId.setCellValueFactory(new PropertyValueFactory<>("feedbackId"));
            colComment.setCellValueFactory(new PropertyValueFactory<>("comment"));
            colRate.setCellValueFactory(new PropertyValueFactory<>("rate"));
            colTeamId.setCellValueFactory(new PropertyValueFactory<>("teamId"));
            colEventId.setCellValueFactory(new PropertyValueFactory<>("eventId"));
            colFeedbackId.setVisible(false);

            setUpButtons();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void setUpButtons() {
        colUpdate.setCellFactory(param -> new TableCell<Feedback, Void>() {
            private final Button updateButton = new Button("Update");
            {
                updateButton.setOnAction(event -> {
                    Feedback feedback = getTableView().getItems().get(getIndex());
                    Stage stage = (Stage) updateButton.getScene().getWindow();
                    navigateToUpdateFeedback(stage, feedback);
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

        colDelete.setCellFactory(param -> new TableCell<Feedback, Void>() {
            private final Button deleteButton = new Button("Delete");
            {
                deleteButton.setOnAction(event -> {
                    Feedback feedback = getTableView().getItems().get(getIndex());
                    handleDeleteButtonClick(feedback);
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

    private void handleDeleteButtonClick(Feedback feedback) {
        FeedbackServices feedbackServices = new FeedbackServices();
        feedbackServices.delete(feedback);
        refreshTable();
    }

    private void refreshTable() {
        FeedbackServices feedbackServices = new FeedbackServices();
        try {
            ObservableList<Feedback> updatedList = FXCollections.observableList(feedbackServices.returnList());
            feedbackTable.setItems(updatedList);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void navigateToAddFeedback(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddFeedback.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Log the error
            showAlert("Error", "Failed to load Add Feedback form: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void navigateToUpdateFeedback(Stage stage, Feedback feedback) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateFeedback.fxml"));
            Parent root = loader.load();

            UpdateFeedbackController controller = loader.getController();
            controller.setCurrentFeedback(feedback);

            stage.setScene(new Scene(root));
            stage.setTitle("Update Feedback");
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}