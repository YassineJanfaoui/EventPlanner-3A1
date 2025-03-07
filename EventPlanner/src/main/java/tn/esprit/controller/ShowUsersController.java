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
import tn.esprit.api.mailerActivate;
import tn.esprit.entities.Role;
import tn.esprit.entities.Status;
import tn.esprit.entities.User;
import tn.esprit.services.UserService;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class ShowUsersController {

    @FXML
    private Button AddAdmin, searchButton;

    @FXML
    private TableView<User> UsersTable;

    @FXML
    private TableColumn<User, String> colEmail, colFullName, colName, colPhoneNumber, colPwd, colRole, colStatus;
    @FXML
    private TableColumn<User, Integer> colId;
    @FXML
    private TableColumn<User, Void> colDelete, colEnable, colDisable, colBan;

    @FXML
    private TextField searchField;

    private UserService userService;
    private List<User> allUsers; // Store all users for filtering

    public ShowUsersController() {
        userService = new UserService();
    }

    @FXML
    public void initialize() {
        URL cssResource = getClass().getResource("/ShowUserStyle.css");
        if (cssResource != null) {
            UsersTable.getStylesheets().add(cssResource.toExternalForm());
        }

        // Initialize columns
        colId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("username"));
        colFullName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colPwd.setCellValueFactory(new PropertyValueFactory<>("password"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadUsers();
        setUpButtons();
    }

    private void loadUsers() {
        allUsers = userService.returnList();
        UsersTable.getItems().setAll(allUsers);
    }

    @FXML
    private void searchUsersByName() {
        String searchText = searchField.getText().trim().toLowerCase();
        if (searchText.isEmpty()) {
            UsersTable.getItems().setAll(allUsers);
        } else {
            List<User> filteredUsers = allUsers.stream()
                    .filter(user -> user.getUsername().toLowerCase().contains(searchText))
                    .collect(Collectors.toList());
            UsersTable.getItems().setAll(filteredUsers);
        }
    }

    private void setUpButtons() {
        colDelete.setCellFactory(param -> new TableCell<>() {
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
                setGraphic(empty ? null : deleteButton);
            }
        });

        colEnable.setCellFactory(param -> new TableCell<>() {
            private final Button enableButton = new Button("Enable");

            {
                enableButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    handleStatusUpdate(user, Status.ACTIVE);
                    mailerActivate.sendEmail(user);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    User user = getTableView().getItems().get(getIndex());
                    setGraphic(user.getRole() == Role.ADMIN ? null : enableButton);
                } else {
                    setGraphic(null);
                }
            }
        });

        colDisable.setCellFactory(param -> new TableCell<>() {
            private final Button disableButton = new Button("Disable");

            {
                disableButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    handleStatusUpdate(user, Status.INACTIVE);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    User user = getTableView().getItems().get(getIndex());
                    setGraphic(user.getRole() == Role.ADMIN ? null : disableButton);
                } else {
                    setGraphic(null);
                }
            }
        });

        colBan.setCellFactory(param -> new TableCell<>() {
            private final Button banButton = new Button("Ban");

            {
                banButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    handleStatusUpdate(user, Status.BANNED);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    User user = getTableView().getItems().get(getIndex());
                    setGraphic(user.getRole() == Role.ADMIN ? null : banButton);
                } else {
                    setGraphic(null);
                }
            }
        });
    }

    private void handleStatusUpdate(User user, Status newStatus) {
        if (user.getRole() == Role.ADMIN) {
            showAlert("Action Denied", "Admins cannot be modified.");
            return;
        }
        user.setStatus(newStatus);
        userService.update(user);
        loadUsers();
    }

    private void handleDeleteButtonClick(User user) {
        try {
            userService.delete(user);
            loadUsers();
        } catch (Exception e) {
            showAlert("Error", "An error occurred while deleting the user: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void NavigateToAddAdmin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddAdmin.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) AddAdmin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Navigation Error", "Unable to load the admin screen.");
        }
    }
}
