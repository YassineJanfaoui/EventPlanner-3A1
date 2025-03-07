package tn.esprit.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import tn.esprit.services.UserService;
import tn.esprit.entities.*;

import java.io.IOException;
import java.sql.SQLException;

public class SignUpController {

    @FXML
    private PasswordField ConPwd;

    @FXML
    private TextField Email;

    @FXML
    private TextField PhonrNumber;

    @FXML
    private TextField UserName;

    @FXML
    private TextField fullname;

    @FXML
    private PasswordField pwd;

    @FXML
    private ChoiceBox<String> roleChoiceBox;

    @FXML
    private Button signUpButton;

    private UserService userService = new UserService();

    @FXML
    public void initialize() {
        // Populate the ChoiceBox with roles
        roleChoiceBox.setItems(FXCollections.observableArrayList("TEAM_LEADER", "EVENT_PLANNER", "SIMPLE_USER"));
    }

    @FXML
    void Submit(ActionEvent event) throws SQLException {
        // Collect data from input fields
        String username = UserName.getText();
        String password = pwd.getText();
        String confirmPassword = ConPwd.getText();
        String email = Email.getText();
        String name = fullname.getText();
        String phoneNumber = PhonrNumber.getText();
        String role = roleChoiceBox.getValue();

        // Validate input fields
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty() || name.isEmpty() || phoneNumber.isEmpty() || role == null) {
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

        if (!password.equals(confirmPassword)) {
            showAlert("Error", "Passwords do not match.");
            return;
        }

        if (!isStrongPassword(password)) {
            showAlert("Error", "Password must be at least 8 characters long and include a mix of letters, numbers, and special characters.");
            return;
        }

        // Create a new User object
        User newUser = new User(username, password, email, name, phoneNumber, Status.ACTIVE, Role.valueOf(role));

        // Use UserService to create the user
        userService.addP(newUser);

        // Show success message
        showAlert("Success", "User created successfully.");

        navigateToLogin();
    }

    private boolean isValidEmail(String email) {
        // Simple email validation regex
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // Simple phone number validation regex (adjust based on your requirements)
        String phoneRegex = "^[0-9]{8}$"; // Assumes a 10-digit phone number
        return phoneNumber.matches(phoneRegex);
    }

    private boolean isStrongPassword(String password) {
        // Password strength validation: at least 8 characters, including letters, numbers, and special characters
        String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
        return password.matches(passwordRegex);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearForm() {
        UserName.clear();
        pwd.clear();
        ConPwd.clear();
        Email.clear();
        fullname.clear();
        PhonrNumber.clear();
        roleChoiceBox.getSelectionModel().clearSelection();
    }
    private void navigateToLogin() {
        try {
            // Load the home.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) signUpButton.getScene().getWindow();

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