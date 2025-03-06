package tn.esprit.api;

import javafx.concurrent.Worker;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RecaptchaUtil {
    private WebView recaptchaWebView;
    private String recaptchaResponse = null;

    public RecaptchaUtil() {
        // No need for WebView in reCAPTCHA v3
    }

    public void executeRecaptcha(String siteKey, RecaptchaCallback callback) {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);

        String htmlContent = "<html>"
                + "<head>"
                + "<script src='https://www.google.com/recaptcha/api.js'></script>"
                + "</head>"
                + "<body>"
                + "<div class='g-recaptcha' data-sitekey='" + siteKey + "'></div>"
                + "<script>"
                + "function onRecaptchaSubmit(token) {"
                + "    window.java.onRecaptchaResponse(token);"
                + "}"
                + "</script>"
                + "</body>"
                + "</html>";

        webEngine.loadContent(htmlContent);

        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("java", new RecaptchaBridge(callback));
            }
        });
    }
    public boolean verifyRecaptcha(String secretKey, String token) {
        try {
            String url = "https://www.google.com/recaptcha/api/siteverify?secret=" + secretKey + "&response=" + token;
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            connection.disconnect();

            JSONObject jsonResponse = new JSONObject(content.toString());
            return jsonResponse.getBoolean("success");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public interface RecaptchaCallback {
        void onRecaptchaResponse(String token);
    }

    private class RecaptchaBridge {
        private RecaptchaCallback callback;

        public RecaptchaBridge(RecaptchaCallback callback) {
            this.callback = callback;
        }

        public void onRecaptchaResponse(String token) {
            callback.onRecaptchaResponse(token);
        }
    }
}