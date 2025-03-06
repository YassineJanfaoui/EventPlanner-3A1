package tn.esprit.controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import tn.esprit.utils.XAiApiService;
import tn.esprit.utils.XAiApiService;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class ChatController {
    @FXML private TextField messageInput;
    @FXML private TextArea chatArea;

    @FXML
    private void initialize() {
        // Listen for Enter key press in the TextField
        messageInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                sendMessage(); // Call sendMessage when Enter is pressed
                event.consume(); // Prevent new line in TextField
            }
        });
    }

    @FXML
    private void sendMessage() {
        String userInput = messageInput.getText();
        if (!userInput.isEmpty()) {
            chatArea.appendText("You: " + userInput + "\n\n");
            String response = XAiApiService.sendMessage(userInput);
            chatArea.appendText("AI: " + response + "\n");
            messageInput.clear();
        }
    }


}