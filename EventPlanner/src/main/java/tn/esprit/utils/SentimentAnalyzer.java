package tn.esprit.utils;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

public class SentimentAnalyzer {
    private static final String API_URL = "https://api-inference.huggingface.co/models/distilbert-base-uncased-finetuned-sst-2-english";
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String API_KEY = "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";

    public static String analyze(String text) {
        if (text == null || text.trim().isEmpty()) return "neutral";
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(API_URL);
            post.setHeader("Authorization", "Bearer " + API_KEY);
            post.setHeader("Content-Type", "application/json");

            ObjectNode payload = mapper.createObjectNode();
            payload.put("inputs", text);
            post.setEntity(new StringEntity(mapper.writeValueAsString(payload)));

            try (CloseableHttpResponse response = client.execute(post)) {
                String responseBody = EntityUtils.toString(response.getEntity());

                if (response.getStatusLine().getStatusCode() != 200) {
                    System.err.println("API Error Details: " + responseBody);
                    return "neutral";
                }

                JsonNode json = mapper.readTree(responseBody);
                return json.get(0).get(0).get("label").asText().toLowerCase();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "neutral";
        }
    }
}