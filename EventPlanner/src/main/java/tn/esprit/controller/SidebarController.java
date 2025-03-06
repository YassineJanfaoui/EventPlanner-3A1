package tn.esprit.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class SidebarController {
    private Button feedbackButton;
    private Stage chatStage;
    @FXML
    private Button chatButton;
    @FXML
    public void navigateToFeedback(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ShowFeedback.fxml"));
            Parent root = loader.load();

            // Get the current stage from the button (if applicable)
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Show Feedback");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle exception if the FXML loading fails
        }
    }
    @FXML
    public void navigateToSubmission(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ShowProject.fxml"));
            Parent root = loader.load();

            // Get the current stage from the button (if applicable)
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Show Feedback");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle exception if the FXML loading fails
        }
    }
    @FXML
    public void navigateToTeam(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ShowTeam.fxml"));
            Parent root = loader.load();

            // Get the current stage from the button (if applicable)
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Show Feedback");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle exception if the FXML loading fails
        }
    }

    @FXML
    private void openChatWindow(ActionEvent event) throws IOException {
        if (chatStage != null && chatStage.isShowing()) {
            chatStage.toFront(); // Bring the existing chat window to the front
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Chat.fxml"));
        Parent root = loader.load();

        chatStage = new Stage();
        chatStage.initModality(Modality.NONE);
        chatStage.setTitle("Chat Bot");
        chatStage.setScene(new Scene(root));

        Stage mainStage = (Stage) chatButton.getScene().getWindow();
        chatStage.setX(mainStage.getX() + mainStage.getWidth() - 350);
        chatStage.setY(mainStage.getY() + mainStage.getHeight() - 550);

        chatStage.show();
    }

}
