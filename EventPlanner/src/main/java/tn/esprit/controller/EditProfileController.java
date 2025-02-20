package tn.esprit.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.entities.*;
import tn.esprit.services.UserService;

import java.io.IOException;

public class EditProfileController {

    @FXML
    private PasswordField ConPwd;

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
    private PasswordField pwd;


    public UserService userService = new UserService();
    public User CurrentUser=LoggedInUser.getInstance().getLoggedInUser();
    @FXML
    public void initialize() {
        if (LoggedInUser.getInstance().getLoggedInUser() != null) {
            UserName.setText(LoggedInUser.getInstance().getLoggedInUser().getUsername());
            Email.setText(LoggedInUser.getInstance().getLoggedInUser().getEmail());
            pwd.setText(LoggedInUser.getInstance().getLoggedInUser().getPassword());
            ConPwd.setText(LoggedInUser.getInstance().getLoggedInUser().getPassword());
            fullname.setText(LoggedInUser.getInstance().getLoggedInUser().getName());
            PhonrNumber.setText(LoggedInUser.getInstance().getLoggedInUser().getPhoneNumber());
        } else {
            UserName.setText("Unknown");
            Email.setText("Unknown");
            pwd.setText("Unknown");
            ConPwd.setText("Unknown");
            fullname.setText("Unknown");
            PhonrNumber.setText("Unknown");
        }
    }
    @FXML
    void Svae(ActionEvent event) {
        // Collect data from input fields
        String username = UserName.getText();
        String password = pwd.getText();
        String confirmPassword = ConPwd.getText();
        String email = Email.getText();
        String name = fullname.getText();
        String phoneNumber = PhonrNumber.getText();
        String role = CurrentUser.getRole().toString();
        String status = CurrentUser.getStatus().toString();

        // Validate input fields
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty() || name.isEmpty() || phoneNumber.isEmpty()) {
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
        CurrentUser.setUsername(username);
        CurrentUser.setPassword(password);
        CurrentUser.setEmail(email);
        CurrentUser.setName(name);
        CurrentUser.setPhoneNumber(phoneNumber);
        userService.update(CurrentUser);

        // Show success message
        showAlert("Success", "User update successfully.");

        navigateToHome();
    }
    private void navigateToHome() {
        try {
            // Load the home.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/event3.fxml"));
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

}
