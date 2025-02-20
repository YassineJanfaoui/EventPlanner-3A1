package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class HomeFrontController {

    @FXML
    private StackPane contentStackPane;

    @FXML
    private Button homeButton;

    @FXML
    private Button aboutButton;

    @FXML
    private Button servicesButton;

    @FXML
    private Button mapButton;

    @FXML
    private Button contactButton;

    @FXML
    private Button signInButton;

    @FXML
    private AnchorPane homePane;

    @FXML
    private AnchorPane aboutPane;

    @FXML
    private AnchorPane servicesPane;

    @FXML
    private AnchorPane mapPane;

    @FXML
    private AnchorPane contactPane;

    @FXML
    private AnchorPane signInPane;

    @FXML
    public void initialize() {
        // Set the default view to Home
        contentStackPane.getChildren().setAll(homePane);
    }

    @FXML
    private void handleButtonClick(javafx.event.ActionEvent event) {
        // Reset all buttons to default style
        resetButtonStyles();

        // Get the clicked button
        Button clickedButton = (Button) event.getSource();

        // Set the selected button style
        clickedButton.setStyle("-fx-text-fill: #F3CE5D; -fx-background-color: #2A2A72; -fx-font-size: 14px;");

        // Update the StackPane content based on the clicked button
        if (clickedButton == homeButton) {
            contentStackPane.getChildren().setAll(homePane);
        } else if (clickedButton == aboutButton) {
            contentStackPane.getChildren().setAll(aboutPane);
        } else if (clickedButton == servicesButton) {
            contentStackPane.getChildren().setAll(servicesPane);
        } else if (clickedButton == mapButton) {
            contentStackPane.getChildren().setAll(mapPane);
        } else if (clickedButton == contactButton) {
            contentStackPane.getChildren().setAll(contactPane);
        } else if (clickedButton == signInButton) {
            contentStackPane.getChildren().setAll(signInPane);
        }
    }

    private void resetButtonStyles() {
        homeButton.setStyle("-fx-text-fill: white; -fx-background-color: transparent; -fx-font-size: 14px;");
        aboutButton.setStyle("-fx-text-fill: white; -fx-background-color: transparent; -fx-font-size: 14px;");
        servicesButton.setStyle("-fx-text-fill: white; -fx-background-color: transparent; -fx-font-size: 14px;");
        mapButton.setStyle("-fx-text-fill: white; -fx-background-color: transparent; -fx-font-size: 14px;");
        contactButton.setStyle("-fx-text-fill: white; -fx-background-color: transparent; -fx-font-size: 14px;");
        signInButton.setStyle("-fx-text-fill: white; -fx-background-color: #3498db; -fx-font-size: 14px; -fx-padding: 5 15 5 15;");
    }
}