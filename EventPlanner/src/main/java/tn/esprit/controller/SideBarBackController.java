package tn.esprit.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tn.esprit.entities.LoggedInUser;

import java.io.IOException;

public class SideBarBackController {

    @FXML
    private Button EventsBack;

    @FXML
    private Button UsersBack;

    @FXML
    private Button LogOutBack;

    @FXML
    private TextField UserNameLoggedIn;

    @FXML
    public void initialize() {
        if (LoggedInUser.getInstance().getLoggedInUser() != null) {
            UserNameLoggedIn.setText(LoggedInUser.getInstance().getLoggedInUser().getUsername());
        } else {
            UserNameLoggedIn.setText("Unknown");
        }
    }
    public void setUserNameLoggedInback(String s)
    {

    }
    @FXML
    void NavigateToLogIn(ActionEvent event) {
        LoggedInUser.getInstance().setLoggedInUser(null);
        try {
            // Load the home.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) LogOutBack.getScene().getWindow();

            // Set the new scene with the home.fxml content
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    @FXML
    void EventsChangeStyleEntered(MouseEvent event) {
        EventsBack.setStyle("-fx-background-color: #F3CE5D; -fx-text-fill: #2A2A72; -fx-pref-width: 210px; -fx-pref-height: 55px; -fx-background-radius: 15px; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 10, 0, 0, 5);");

    }

    @FXML
    void EventsChangeStyleExited(MouseEvent event) {
        EventsBack.setStyle("-fx-background-color: #B0A295; -fx-text-fill: #F2F2F5; -fx-font-size: 16px; -fx-pref-width: 210px; -fx-pref-height: 55px; -fx-background-radius: 15px; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 4);");

    }

    @FXML
    void UsersChangeStyleEntered(MouseEvent event) {
        UsersBack.setStyle("-fx-background-color: #F3CE5D; -fx-text-fill: #2A2A72; -fx-pref-width: 210px; -fx-pref-height: 55px; -fx-background-radius: 15px; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 10, 0, 0, 5);");
    }

    @FXML
    void UsersChangeStyleExited(MouseEvent event) {
        UsersBack.setStyle("-fx-background-color: #B0A295; -fx-text-fill: #F2F2F5; -fx-font-size: 16px; -fx-pref-width: 210px; -fx-pref-height: 55px; -fx-background-radius: 15px; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 4);");
    }

    @FXML
    void NavigateToEvents(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventsBack.fxml"));
            Parent root = loader.load();

            // Get the current stage from the button (if applicable)
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Show Users");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle exception if the FXML loading fails
        }
    }

    @FXML
    void NavigateToUsers(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/BackAdmin.fxml"));
            Parent root = loader.load();

            // Get the current stage from the button (if applicable)
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Users");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle exception if the FXML loading fails
        }
    }
    @FXML
    void LogOutChangeStyleEntered(MouseEvent event) {
        LogOutBack.setStyle("-fx-background-color: #F3CE5D; -fx-text-fill: #2A2A72; -fx-pref-width: 210px; -fx-pref-height: 55px; -fx-background-radius: 15px; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 10, 0, 0, 5);");
    }

    @FXML
    void LogOutChangeStyleExited(MouseEvent event) {
        LogOutBack.setStyle("-fx-background-color: #B0A295; -fx-text-fill: #F2F2F5; -fx-font-size: 16px; -fx-pref-width: 210px; -fx-pref-height: 55px; -fx-background-radius: 15px; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 4);");

    }

}
