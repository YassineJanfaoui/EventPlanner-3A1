package tn.esprit.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tn.esprit.entities.*;
import tn.esprit.services.UserService;

import java.io.IOException;
import java.util.List;

public class ShowUsersController {

    @FXML
    private Button AddAdmin;

    @FXML
    private TableView<User> UsersTable;

    @FXML
    private TableColumn<User, String> colEmail;

    @FXML
    private TableColumn<User, String> colFullName;

    @FXML
    private TableColumn<User, Integer> colId;

    @FXML
    private TableColumn<User, String> colName;

    @FXML
    private TableColumn<User, String> colPhoneNumber;

    @FXML
    private TableColumn<User, String> colPwd;

    @FXML
    private TableColumn<User, String> colRole;
    @FXML
    private TableColumn<User, String> colStatus;
    @FXML
    private TableColumn<User, Void> colDelete;


    private UserService userService;

    public ShowUsersController() {
        userService = new UserService();
    }

    @FXML
    public void initialize() {
        // Initialize the columns
        colId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("username"));
        colFullName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colPwd.setCellValueFactory(new PropertyValueFactory<>("password"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Load the users into the table
        loadUsers();
        setUpButtons();

        // Set up row factory for double-click action
        UsersTable.setRowFactory(tv -> {
            TableRow<User> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    SelectedUser.getInstance().setSelectedUser(row.getItem());
                    System.out.println(SelectedUser.getInstance().getSelectedUserr());

                    try {
                        // Load the UserDetails.fxml file
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserDetails.fxml"));
                        Parent root = loader.load();

                        // Create a new stage for the UserDetails window
                        Stage userDetailsStage = new Stage();
                        userDetailsStage.setTitle("User Details");
                        userDetailsStage.setScene(new Scene(root));
                        userDetailsStage.initModality(Modality.APPLICATION_MODAL);

                        // Get the current stage (window) and close it
                        Stage currentStage = (Stage) UsersTable.getScene().getWindow();
                        currentStage.close();

                        // Show the UserDetails window
                        userDetailsStage.showAndWait();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });
    }
    public void loadUsers() {
        List<User> users = userService.returnList();
        UsersTable.getItems().setAll(users);
    }
    private void setUpButtons() {
        // Delete Button
        colDelete.setCellFactory(param -> new TableCell<User, Void>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    handleDeleteButtonClick(user);
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
    private void handleDeleteButtonClick(User user) {
        System.out.println("Delete user: " + user);
        UserService userService = new UserService();
        try {
            userService.delete(user); // Assuming you have a delete method in UserService
            loadUsers();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while deleting the user: " + e.getMessage());
        }
    }
    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    void NavigateToAddAdmin(ActionEvent event) {
        try {
            // Load the home.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddAdmin.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) AddAdmin.getScene().getWindow();

            // Set the new scene with the home.fxml content
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Unable to load the home screen.");
        }
    }
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}