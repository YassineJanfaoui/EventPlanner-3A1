package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import tn.esprit.entities.*;
import javafx.event.ActionEvent;


import java.io.IOException;

public class HomeController {
    @FXML
    private Button EditProfileHome;



    @FXML
    private TextField FullNameHome;

    @FXML
    private Button LogOutHome;

    @FXML
    private TextField RoleHome;


    @FXML
    public void initialize() {
        if (LoggedInUser.getInstance().getLoggedInUser() != null) {
            FullNameHome.setText(LoggedInUser.getInstance().getLoggedInUser().getUsername());
            RoleHome.setText(LoggedInUser.getInstance().getLoggedInUser().getRole().toString());
        } else {
            FullNameHome.setText("Unknown");
            RoleHome.setText("Unknown");
        }
    }


    @FXML
    void Clicked(ActionEvent event) {
        LoggedInUser.getInstance().setLoggedInUser(null);
        navigateToLogIn();
    }
    private void navigateToLogIn() {
        try {
            // Load the home.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) LogOutHome.getScene().getWindow();

            // Set the new scene with the home.fxml content
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
    @FXML
    void EditClicked(ActionEvent event) {
        try {
            // Load the home.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditProfile.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) EditProfileHome.getScene().getWindow();

            // Set the new scene with the home.fxml content
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}