package tn.esprit.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tn.esprit.entities.LoggedInUser;
import tn.esprit.services.UserService;
import tn.esprit.entities.User;

import java.io.IOException;

public class LoginController {

    @FXML
    private Button loginBtn;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    private UserService userService = new UserService();

    @FXML
    private Label navsignup;


    @FXML
    void Submit(ActionEvent event) {
        // Retrieve email and password from the input fields
        String email = usernameField.getText();
        String password = passwordField.getText();

        // Validate inputs
        if (email.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.ERROR, "Input Error", "Please fill in all fields.");
            return;
        }

        // Authenticate the user
        User user = userService.getUserByEmailAndPass(email, password);

        if (user != null) {
            // Authentication successful
            showAlert(AlertType.INFORMATION, "Login Successful", "Welcome, " + user.getName() + "!");

            LoggedInUser.getInstance().setLoggedInUser(user);
            if(user.getRole().toString().equals("ADMIN")){
                navigateToBackAdmin();
            }
            else {
                navigateToHome();
            }
        } else {
            // Authentication failed
            showAlert(AlertType.ERROR, "Login Failed", "Invalid email or password.");
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void navigateToHome() {
        try {
            // Load the home.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/event3.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) loginBtn.getScene().getWindow();

            // Set the new scene with the home.fxml content
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Navigation Error", "Unable to load the home screen.");
        }
    }

    private void navigateToBackAdmin() {
        try {
            // Load the home.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/BackAdmin.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) loginBtn.getScene().getWindow();

            // Set the new scene with the home.fxml content
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Navigation Error", "Unable to load the home screen.");
        }
    }

    public void navigateToSignUp(MouseEvent mouseEvent) {
        try {
            // Load the SignUpView.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SignUpView.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) navsignup.getScene().getWindow();

            // Set the new scene with the SignUpView.fxml content
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Navigation Error", "Unable to load the sign-up screen. Please check the file path.");
        } catch (NullPointerException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Navigation Error", "The SignUpView.fxml file was not found. Please check the file path.");
        }
    }
}