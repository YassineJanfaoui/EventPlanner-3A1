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
import tn.esprit.entities.Team;
import tn.esprit.services.TeamServices;

import java.io.IOException;
import java.sql.SQLException;

public class ShowTeamController {

    @FXML
    private TableView<Team> teamTable;

    @FXML
    private TableColumn<Team, Integer> colTeamId;

    @FXML
    private TableColumn<Team, String> colTeamName;
    @FXML
    private TableColumn<Team, Integer> colScore;
    @FXML
    private TableColumn<Team, Integer> colRank;
    @FXML
    private TableColumn<Team, Integer> colEventId;

    @FXML
    private TableColumn<Team, Void> colUpdate;

    @FXML
    private TableColumn<Team, Void> colDelete;

    @FXML
    void initialize() {
        TeamServices teamServices = new TeamServices();
        try {
            ObservableList<Team> observableList = FXCollections.observableList(teamServices.returnList());
            teamTable.setItems(observableList);
            colScore.setCellValueFactory(new PropertyValueFactory<>("score"));

            colTeamName.setCellValueFactory(new PropertyValueFactory<>("teamName"));
            colEventId.setCellValueFactory(new PropertyValueFactory<>("eventId"));

            setUpButtons();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void setUpButtons() {
        colUpdate.setCellFactory(param -> new TableCell<Team, Void>() {
            private final Button updateButton = new Button("Update");
            {
                updateButton.setOnAction(event -> {
                    Team team = getTableView().getItems().get(getIndex());
                    Stage stage = (Stage) updateButton.getScene().getWindow();
                    navigateToUpdateTeam(stage, team);
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

        colDelete.setCellFactory(param -> new TableCell<Team, Void>() {
            private final Button deleteButton = new Button("Delete");
            {
                deleteButton.setOnAction(event -> {
                    Team team = getTableView().getItems().get(getIndex());
                    handleDeleteButtonClick(team);
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

    private void handleDeleteButtonClick(Team team) {
        TeamServices teamServices = new TeamServices();
        teamServices.delete(team);
        refreshTable();
    }

    private void refreshTable() {
        TeamServices teamServices = new TeamServices();
        try {
            ObservableList<Team> updatedList = FXCollections.observableList(teamServices.returnList());
            teamTable.setItems(updatedList);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void navigateToAddTeam(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddTeam.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Team");
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void navigateToUpdateTeam(Stage stage, Team team) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateTeam.fxml"));
            Parent root = loader.load();

            tn.esprit.controller.UpdateTeamController controller = loader.getController();
            controller.setCurrentTeam(team);

            stage.setScene(new Scene(root));
            stage.setTitle("Update Team");
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void navigateToMangeTeam(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ManageTeam.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Team");
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


}

    public void navigateToLeaderboard(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Leaderboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Leaderboard");
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    }

