package tn.esprit.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFX extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root= FXMLLoader.load(getClass().getResource("/ShowVenue.fxml"));

        Scene scene=new Scene(root);

        primaryStage.setScene(scene);

        primaryStage.show();

        primaryStage.setTitle("first Scene");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
