package tn.esprit.utils;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class XAiApiService {
    private static final String API_KEY = "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"; // Replace with your key
    private static final String API_URL = "https://openrouter.ai/api/v1/chat/completions"; // Adjust if needed

    public static String sendMessage(String prompt) {
        try {
            // Prepare request payload
            JSONObject json = new JSONObject();
            json.put("model", "google/gemini-2.0-flash-thinking-exp:free"); // Ensure this matches the available model
            json.put("messages", new JSONObject[]{
                    new JSONObject().put("role", "system").put("content", "You are an AI assistant."),
                    new JSONObject().put("role", "user").put("content", prompt)
            });

            // Build HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Authorization", "Bearer " + API_KEY)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                    .build();

            // Send request
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parse response
            JSONObject responseObject = new JSONObject(response.body());
            System.out.println(responseObject.toString());
            // Correct JSON parsing

            JSONArray choices = responseObject.getJSONArray("choices");
            String content = choices.getJSONObject(0).getJSONObject("message").getString("content");
            return content;


        } catch (Exception e) {
            e.printStackTrace();
            return "Error communicating with OpenRouter API.";
        }
    }

    public static void main(String[] args) {
        String response = sendMessage("Hello, how are you?");
        System.out.println("AI Response: " + response);
    }
}