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
import tn.esprit.entities.ProjectSubmission;
import tn.esprit.services.ProjectSubmissionServices;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

public class ShowProjectController {

    @FXML
    private TableView<ProjectSubmission> projectTable;

    @FXML
    private TableColumn<ProjectSubmission, Integer> colSubmissionId;

    @FXML
    private TableColumn<ProjectSubmission, String> colTitle;

    @FXML
    private TableColumn<ProjectSubmission, String> colFileLink;
    @FXML
    private TableColumn<ProjectSubmission, Date> colSubmissionDate;

    @FXML
    private TableColumn<ProjectSubmission, Integer> colTeamId;

    @FXML
    private TableColumn<ProjectSubmission, Void> colUpdate;

    @FXML
    private TableColumn<ProjectSubmission, Void> colDelete;

    @FXML
    void initialize() {
        ProjectSubmissionServices projectServices = new ProjectSubmissionServices();
        try {
            ObservableList<ProjectSubmission> observableList = FXCollections.observableList(projectServices.returnList());
            projectTable.setItems(observableList);
            colSubmissionId.setCellValueFactory(new PropertyValueFactory<>("submissionId"));
            colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
            colFileLink.setCellValueFactory(new PropertyValueFactory<>("fileLink"));
            colSubmissionDate.setCellValueFactory(new PropertyValueFactory<>("submissionDate"));
            colTeamId.setCellValueFactory(new PropertyValueFactory<>("teamId"));
            colSubmissionId.setVisible(false);

            setUpButtons();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void setUpButtons() {
        colUpdate.setCellFactory(param -> new TableCell<ProjectSubmission, Void>() {
            private final Button updateButton = new Button("Update");
            {
                updateButton.setOnAction(event -> {
                    ProjectSubmission project = getTableView().getItems().get(getIndex());
                    Stage stage = (Stage) updateButton.getScene().getWindow();
                    navigateToUpdateProject(stage, project);
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

        colDelete.setCellFactory(param -> new TableCell<ProjectSubmission, Void>() {
            private final Button deleteButton = new Button("Delete");
            {
                deleteButton.setOnAction(event -> {
                    ProjectSubmission project = getTableView().getItems().get(getIndex());
                    handleDeleteButtonClick(project);
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

    private void handleDeleteButtonClick(ProjectSubmission project) {
        ProjectSubmissionServices projectServices = new ProjectSubmissionServices();
        projectServices.delete(project);
        refreshTable();
    }

    private void refreshTable() {
        ProjectSubmissionServices projectServices = new ProjectSubmissionServices();
        try {
            ObservableList<ProjectSubmission> updatedList = FXCollections.observableList(projectServices.returnList());
            projectTable.setItems(updatedList);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void navigateToAddProject(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddProject.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Project");
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void navigateToUpdateProject(Stage stage, ProjectSubmission project) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateProject.fxml"));
            Parent root = loader.load();

            UpdateProjectController controller = loader.getController();
            controller.setCurrentProject(project);

            stage.setScene(new Scene(root));
            stage.setTitle("Update Project");
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
