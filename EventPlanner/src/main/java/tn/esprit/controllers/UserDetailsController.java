package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.entities.*;
import tn.esprit.services.UserService;

import java.io.IOException;

public class UserDetailsController {

    @FXML
    private TextField ConPwd;

    @FXML
    private TextField Email;

    @FXML
    private TextField PhonrNumber;

    @FXML
    private Button SaveBtn;

    @FXML
    private TextField UserName;

    @FXML
    private TextField fullname;

    @FXML
    private TextField pwd;

    public User CurrentUser = SelectedUser.getInstance().getSelectedUserr();
    private Stage stage;
    private UserService userService = new UserService();
    private ShowUsersController showUsersController; // Reference to ShowUsersController

    @FXML
    public void initialize() {
        UserName.setText(CurrentUser.getUsername());
        fullname.setText(CurrentUser.getName());
        Email.setText(CurrentUser.getEmail());
        PhonrNumber.setText(CurrentUser.getPhoneNumber());
        pwd.setText(CurrentUser.getPassword());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setShowUsersController(ShowUsersController showUsersController) {
        this.showUsersController = showUsersController;
    }

    @FXML
    void Save(ActionEvent event) {
        String username = UserName.getText();
        String password = pwd.getText();
        String email = Email.getText();
        String name = fullname.getText();
        String phoneNumber = PhonrNumber.getText();

        // Validate input fields
        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || name.isEmpty() || phoneNumber.isEmpty()) {
            showAlert("Error", "All fields are required.");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert("Error", "Invalid email format.");
            return;
        }

        if (!isValidPhoneNumber(phoneNumber)) {
            showAlert("Error", "Invalid phone number format.");
            return;
        }

        if (!isStrongPassword(password)) {
            showAlert("Error", "Password must be at least 8 characters long and include a mix of letters, numbers, and special characters.");
            return;
        }

        // Update the selected user
        User updatedUser = SelectedUser.getInstance().getSelectedUserr();
        updatedUser.setUsername(username);
        updatedUser.setPassword(password);
        updatedUser.setEmail(email);
        updatedUser.setName(name);
        updatedUser.setPhoneNumber(phoneNumber);
        userService.update(updatedUser);

        // Update the logged-in user if it's the same user
        if (LoggedInUser.getInstance().getLoggedInUser() != null &&
                LoggedInUser.getInstance().getLoggedInUser().getUserId() == updatedUser.getUserId()) {
            LoggedInUser.getInstance().setLoggedInUser(updatedUser);
        }
        try {
            // Load the home.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/BackAdmin.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) SaveBtn.getScene().getWindow();

            // Set the new scene with the home.fxml content
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        String phoneRegex = "^[0-9]{8}$"; // Assumes an 8-digit phone number
        return phoneNumber.matches(phoneRegex);
    }

    private boolean isStrongPassword(String password) {
        String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
        return password.matches(passwordRegex);
    }
}