package tn.esprit.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;
import tn.esprit.entities.Workshop;
import tn.esprit.services.WorkshopServices;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ShowWorkshopController {

    @FXML
    private TableView<Workshop> workshopTable;

    @FXML
    private TableColumn<Workshop, Integer> colWorkshopID;

    @FXML
    private TableColumn<Workshop, String> colTitle;

    @FXML
    private TableColumn<Workshop, String> colCoach;

    @FXML
    private TableColumn<Workshop, Integer> colDuration;

    @FXML
    private TableColumn<Workshop, String> colStartDate;

    @FXML
    private TableColumn<Workshop, String> colDescription;

    @FXML
    private TableColumn<Workshop, Integer> colPartnerID;

    @FXML
    private TableColumn<Workshop, Void> colUpdate;

    @FXML
    private TableColumn<Workshop, Void> colDelete;

    @FXML
    private Button addButton;

    @FXML
    private Label weatherLabel;

    @FXML
    private TextField searchField;

    @FXML
    void initialize() {
        loadWorkshopTable();
        setUpButtons();
        setUpRowClickListener();
    }

    private void loadWorkshopTable() {
        WorkshopServices workshopServices = new WorkshopServices();
        try {
            ObservableList<Workshop> observableList = FXCollections.observableList(workshopServices.returnList());
            workshopTable.setItems(observableList);

            colWorkshopID.setCellValueFactory(new PropertyValueFactory<>("workshopId"));
            colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
            colCoach.setCellValueFactory(new PropertyValueFactory<>("coach"));
            colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
            colStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
            colPartnerID.setCellValueFactory(new PropertyValueFactory<>("partnerId"));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void setUpButtons() {
        colUpdate.setCellFactory(param -> new TableCell<>() {
            private final Button updateButton = new Button("Update");
            {
                updateButton.setOnAction(event -> {
                    Workshop workshop = getTableView().getItems().get(getIndex());
                    Stage stage = (Stage) updateButton.getScene().getWindow();
                    navigateToUpdateWorkshop(stage, workshop);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : updateButton);
            }
        });

        colDelete.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");
            {
                deleteButton.setOnAction(event -> {
                    Workshop workshop = getTableView().getItems().get(getIndex());
                    handleDeleteButtonClick(workshop);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        });
    }

    @FXML
    public void searchWorkshops() {
        String keyword = searchField.getText().toLowerCase();
        WorkshopServices workshopServices = new WorkshopServices();
        try {
            List<Workshop> allWorkshops = workshopServices.returnList();
            List<Workshop> filtered = new ArrayList<>();

            for (Workshop w : allWorkshops) {
                if (w.getTitle().toLowerCase().contains(keyword)) {
                    filtered.add(w);
                }
            }

            ObservableList<Workshop> observableList = FXCollections.observableList(filtered);
            workshopTable.setItems(observableList);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void sortByDateRecentToLatest() {
        sortWorkshops(Comparator.comparing(Workshop::getStartDate));
    }

    @FXML
    public void sortByDateLatestToRecent() {
        sortWorkshops((w1, w2) -> w2.getStartDate().compareTo(w1.getStartDate()));
    }

    private void sortWorkshops(Comparator<Workshop> comparator) {
        WorkshopServices workshopServices = new WorkshopServices();
        try {
            List<Workshop> workshopList = workshopServices.returnList();
            workshopList.sort(comparator);
            ObservableList<Workshop> observableList = FXCollections.observableList(workshopList);
            workshopTable.setItems(observableList);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleDeleteButtonClick(Workshop workshop) {
        WorkshopServices workshopServices = new WorkshopServices();
        workshopServices.delete(workshop);
        loadWorkshopTable();
    }

    @FXML
    public void navigateToAddWorkshop(ActionEvent event) {
        navigateTo(event, "/AddWorkshop.fxml");
    }

    public void navigateToUpdateWorkshop(Stage stage, Workshop workshop) {
        navigateTo(stage, "/UpdateWorkshop.fxml", workshop);
    }

    private void navigateTo(Object source, String fxml, Workshop... workshop) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            if (workshop.length > 0) loader.<UpdateWorkshopController>getController().setCurrentWorkshop(workshop[0]);
            Stage stage = source instanceof ActionEvent ? (Stage) ((Button) ((ActionEvent) source).getSource()).getScene().getWindow() : (Stage) source;
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Fetch and show weather based on the date of the workshop


    private void fetchAndShowWeather(Date date) {
        String apiKey = "a8b4422f1b9e4d508fa154403252802"; // Replace with your actual WeatherAPI key
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(date); // Convert date to string in 'yyyy-MM-dd' format
        String city = "Tunis"; // City for which you want the weather
        String urlString = "https://api.weatherapi.com/v1/forecast.json?key=" + apiKey + "&q=" + city + "&dt=" + formattedDate + "&aqi=no";


        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Process the response (display weather info)
            String jsonResponse = response.toString();
            System.out.println("Weather data: " + jsonResponse);  // You can display or process the JSON here

            // Parse JSON response
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONObject forecast = jsonObject.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0);
            JSONObject day = forecast.getJSONObject("day");

            // Get the temperature (avgtemp_c is a BigDecimal)
            BigDecimal avgTempC = day.getBigDecimal("avgtemp_c");

            // Display the temperature
            System.out.println("Average Temperature in Celsius: " + avgTempC.doubleValue());  // Convert BigDecimal to double

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }



    // Set up row click listener to fetch weather on workshop row click
    public void setUpRowClickListener() {
        workshopTable.setRowFactory(tv -> {
            TableRow<Workshop> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    Workshop clickedWorkshop = row.getItem();
                    // Fetch weather for the clicked workshop's date
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date workshopDate = sdf.parse(String.valueOf(clickedWorkshop.getStartDate()));
                        fetchAndShowWeather(workshopDate); // Call the method to fetch and display weather
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });
    }

}
