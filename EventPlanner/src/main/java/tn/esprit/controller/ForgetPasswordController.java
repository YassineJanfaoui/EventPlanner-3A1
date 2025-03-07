package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.api.mailer;
import tn.esprit.entities.User;
import tn.esprit.services.UserService;

import java.io.IOException;

public class ForgetPasswordController {

    @FXML
    private TextField Email;

    @FXML
    private Button Send;

    private UserService userService = new UserService();

    @FXML
    public void initialize() {
        // Set an action event handler for the Send button
        Send.setOnAction(event -> handleSendButtonAction());
    }

    private void handleSendButtonAction() {
        // Retrieve the email address from the TextField
        String emailAddress = Email.getText().trim();
        User user = null;
        if (!emailAddress.isEmpty()) {
            // Create a User object (assuming User class has a constructor that accepts email)
            user = userService.getUserByEmail(emailAddress);
            if (user != null) {
                try {
                    // Send the email using the mailer API
                    mailer.sendEmail(user);

                    // Show a success message to the user
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Email sent successfully to: " + emailAddress);

                    // Navigate to /login.fxml
                    navigateToLogin();

                } catch (Exception e) {
                    // Show an error message if sending the email fails
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to send email: " + e.getMessage());
                }
            } else {
                // Show an error message if the email does not exist
                showAlert(Alert.AlertType.ERROR, "Error", "Email does not exist.");
            }
        } else {
            // Show an error message if the email field is empty
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter an email address.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void navigateToLogin() {
        try {
            // Load the /login.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) Send.getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            // Show an error message if navigation fails
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to navigate to login page: " + e.getMessage());
        }
    }
}