package tn.esprit.controller;



import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.MapView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.InetAddress;


public class Weather {

    @FXML
    private TextField mapView;
    @FXML
    private Label coordinatesLabel;
    @FXML
    private Label weatherLabel;
    @FXML
    private ImageView weatherIcon;
 // Input field for the city name
    @FXML
    private ImageView cityImageView; // ImageView to display the city image

    public void initialize() {
        System.out.println("Initialisation de la carte...");

        // Listener for the "Enter" key in the city input field
        mapView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String cityName = mapView.getText().trim();
                if (!cityName.isEmpty()) {
                    if (isInternetAvailable()) {
                        fetchCoordinates(cityName); // Fetches weather data
                        fetchCityImage(cityName); // Fetches and displays the city image
                    } else {
                        showAlert("Erreur de connexion", "Aucune connexion Internet disponible.");
                    }
                } else {
                    showAlert("Erreur de saisie", "Veuillez saisir un nom de ville valide.");
                }
            }
        });
    }
    private void fetchCityImage(String cityName) {
        try {
            // Your Pexels API key
            String apiKey = "LGHNvD49j5HBAaUPTUpqDNxLc7kv2p9zDFbinWe6MoBlI2elEAOwsBoW";

            // Construct the API URL
            String urlString = "https://api.pexels.com/v1/search?query=" + cityName + "&per_page=1";
            URL url = new URL(urlString);

            // Open connection and make the API request
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Authorization", apiKey);  // Use your Pexels API key
            connection.setRequestMethod("GET");

            // Read the response from the API
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Extract the image URL from the JSON response
            String imageUrl = parseImageUrlFromJson(response.toString());

            // Ensure the image is properly loaded and set to ImageView
            Platform.runLater(() -> {
                Image image = new Image(imageUrl);
                cityImageView.setImage(image);  // Set the image to the ImageView
                System.out.println("Image URL: " + imageUrl);  // For debugging
            });

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur de chargement de l'image", "Impossible de récupérer l'image de la ville.");
        }
    }


    private String parseImageUrlFromJson(String jsonResponse) {
        try {
            // Create a JSONObject from the JSON response
            JSONObject jsonObject = new JSONObject(jsonResponse);

            // Extract the URL of the first image in the results
            JSONArray photos = jsonObject.getJSONArray("photos");
            JSONObject firstPhoto = photos.getJSONObject(0);
            return firstPhoto.getJSONObject("src").getString("original");  // Extract the original image URL
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }



    private void fetchCoordinates(String cityName) {
        try {
            String API_KEY = "d8ef548a7e3fae997f8e23ba1957d7b1"; // Utiliser la même clé API OpenWeatherMap
            String url = "https://api.openweathermap.org/geo/1.0/direct?q=" + cityName + "&limit=1&appid=" + API_KEY;
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            JSONArray jsonArray = new JSONArray(response.toString());
            if (jsonArray.length() > 0) {
                JSONObject cityInfo = jsonArray.getJSONObject(0);
                double latitude = cityInfo.getDouble("lat");
                double longitude = cityInfo.getDouble("lon");

                coordinatesLabel.setText("Coordonnées : " + latitude + ", " + longitude);
                fetchWeather(latitude, longitude); // Appel à la méthode pour obtenir la météo
            } else {
                showAlert("Ville non trouvée", "Aucune correspondance pour la ville : " + cityName);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de récupérer les coordonnées de la ville.");
        }
    }


    public String getLocationName(double latitude, double longitude) {
        try {
            String url = "https://nominatim.openstreetmap.org/reverse?format=json&lat=" + latitude + "&lon=" + longitude;
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            InputStreamReader reader = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(reader);
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            JSONObject jsonResponse = new JSONObject(response.toString());
            return jsonResponse.optString("display_name", "Lieu inconnu");

        } catch (Exception e) {
            e.printStackTrace();
            return "Lieu inconnu";
        }
    }

    private void fetchWeather(double latitude, double longitude) {
        try {
            JSONObject jsonResponse = getJsonObject(latitude, longitude);
            double temp = jsonResponse.getJSONObject("main").getDouble("temp");
            String weather = jsonResponse.getJSONArray("weather").getJSONObject(0).getString("description");
            String iconCode = jsonResponse.getJSONArray("weather").getJSONObject(0).getString("icon");

            weatherLabel.setText("Météo: " + weather + " | Température: " + temp + "°C");
            weatherIcon.setImage(new Image("http://openweathermap.org/img/wn/" + iconCode + "@2x.png"));

        } catch (Exception e) {
            e.printStackTrace();
            weatherLabel.setText("Impossible de récupérer les données météo.");
        }
    }

    private static JSONObject getJsonObject(double latitude, double longitude) throws IOException {
        String API_KEY = "d8ef548a7e3fae997f8e23ba1957d7b1";
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=" + API_KEY + "&units=metric";
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            response.append(line);
        }

        return new JSONObject(response.toString());
    }
    private boolean isInternetAvailable() {
        try {
            InetAddress address = InetAddress.getByName("www.google.com");
            return address.isReachable(2000);
        } catch (IOException e) {
            return false;
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }


}
