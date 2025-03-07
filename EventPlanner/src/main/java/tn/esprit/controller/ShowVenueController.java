package tn.esprit.controller;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import tn.esprit.entities.Venue;
import tn.esprit.services.VenueServices;
import java.awt.Desktop;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


import java.sql.SQLException;
import java.util.Scanner;

public class ShowVenueController {

    @FXML
    private TableView<Venue> venueTable;

    @FXML
    private TableColumn<Venue, Integer> colVenueId;

    @FXML
    private TableColumn<Venue, String> colVenueName;

    @FXML
    private TableColumn<Venue, String> colLocation;

    @FXML
    private TableColumn<Venue, Integer> colNbrPlaces;

    @FXML
    private TableColumn<Venue, String> colAvailability;

    @FXML
    private TableColumn<Venue, Float> colCost;

    @FXML
    private TableColumn<Venue, String> colParking;

    @FXML
    private TableColumn<Venue, Void> colUpdate;

    @FXML
    private TableColumn<Venue, Void> colDelete;

    @FXML
    private Label bestVenueLabel;


    @FXML
    void initialize() throws SQLException {
        VenueServices venueServices = new VenueServices();
        try {
            ObservableList<Venue> observableList = FXCollections.observableList(venueServices.returnList());
            venueTable.setItems(observableList);

            colVenueId.setCellValueFactory(new PropertyValueFactory<>("venueId"));
            colVenueName.setCellValueFactory(new PropertyValueFactory<>("venueName"));
            colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
            colNbrPlaces.setCellValueFactory(new PropertyValueFactory<>("nbrPlaces"));
            colAvailability.setCellValueFactory(new PropertyValueFactory<>("availability"));
            colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
            colParking.setCellValueFactory(new PropertyValueFactory<>("parking"));

            loadBestValueVenue();
            setUpContextMenu();
            setUpButtons();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void setUpButtons() {
        colUpdate.setCellFactory(param -> new TableCell<Venue, Void>() {
            private final Button updateButton = new Button("Update");
            {
                updateButton.setOnAction(event -> {
                    Venue venue = getTableView().getItems().get(getIndex());
                    Stage stage = (Stage) updateButton.getScene().getWindow();
                    navigateToUpdateVenue(stage, venue);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(updateButton);
                }
            }
        });
        colDelete.setCellFactory(param -> new TableCell<Venue, Void>() {
            private final Button deleteButton = new Button("Delete");
            {
                deleteButton.setOnAction(event -> {
                    Venue venue = getTableView().getItems().get(getIndex());
                    try {
                        handleDeleteButtonClick(venue);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
    }

    private void handleDeleteButtonClick(Venue venue) throws SQLException {
        VenueServices venueServices = new VenueServices();
        venueServices.delete(venue);
        refreshTable();
    }

    private void refreshTable() throws SQLException {
        VenueServices venueServices = new VenueServices();
        try {
            ObservableList<Venue> updatedList = FXCollections.observableList(venueServices.returnList());
            venueTable.setItems(updatedList);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    @FXML
    private void handleSyncCalendarButtonClick() {
        try {
            System.out.println("🔄 Syncing all reservations to Google Calendar...");
            VenueServices venueServices = new VenueServices();
            venueServices.syncAllReservationsToGoogleCalendar();
            System.out.println("✅ All reservations synced to Google Calendar!");
        } catch (SQLException e) {
            System.out.println("❌ Error syncing to Google Calendar: " + e.getMessage());
        }
    }
    @FXML
    private void openGoogleCalendar() {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                String googleCalendarUrl = "https://calendar.google.com/calendar/u/0/r";
                Desktop.getDesktop().browse(new URI(googleCalendarUrl));
            } else {
                System.out.println("❌ Desktop browsing is not supported on this system.");
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void setUpContextMenu() {
        venueTable.setRowFactory(tableView -> {
            TableRow<Venue> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();

            MenuItem searchYouTube = new MenuItem("Search YouTube Video");
            searchYouTube.setOnAction(event -> {
                Venue selectedVenue = row.getItem();
                if (selectedVenue != null) {
                    searchYouTubeVideo(selectedVenue.getVenueName());
                }
            });

            contextMenu.getItems().add(searchYouTube);

            row.setOnContextMenuRequested(event -> {
                System.out.println("in");
                if (!row.isEmpty()) {
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                    System.out.println("not empty");
                }
            });
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(contextMenu)
            );
            row.setOnMouseClicked(event -> contextMenu.hide());

            return row;
        });
    }

    private void searchYouTubeVideo(String query) {
        System.out.println("🔍 Searching YouTube for: " + query);
        try {
            String apiKey = "AIzaSyAMuobwTtKJu_Cq83jHlxTP6xexx9PMWGY"; // Replace with a valid YouTube API key
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
            String apiUrl = "https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&q="
                    + encodedQuery + "&key=" + apiKey;

            System.out.println("🔹 Searching YouTube for: " + query);
            System.out.println("🔹 API Request URL: " + apiUrl);

            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            System.out.println("🔹 Response Code: " + responseCode);

            if (responseCode != 200) {
                System.out.println("🔴 ERROR: API request failed with response code: " + responseCode);
                showAlert("Error", "Failed to fetch YouTube videos. Response code: " + responseCode);
                return;
            }

            Scanner scanner = new Scanner(connection.getInputStream());
            StringBuilder response = new StringBuilder();

            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
            scanner.close();

            System.out.println("🔹 API Response: " + response.toString());

            String videoId = extractFirstVideoId(response.toString());

            if (videoId != null) {
                String videoUrl = "https://www.youtube.com/watch?v=" + videoId;
                System.out.println("✅ Opening YouTube Video: " + videoUrl);
                openWebpage(videoUrl);
            } else {
                System.out.println("⚠️ No video found for: " + query);
                showAlert("No Video Found", "No video found for: " + query);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred: " + e.getMessage());
        }
    }
    private String extractFirstVideoId(String jsonResponse) {
        int videoIdIndex = jsonResponse.indexOf("\"videoId\":");
        if (videoIdIndex != -1) {
            int start = jsonResponse.indexOf("\"", videoIdIndex + 10) + 1;
            int end = jsonResponse.indexOf("\"", start);
            return jsonResponse.substring(start, end);
        }
        return null;
    }

    private void openWebpage(String url) {
        System.out.println("Opening webpage: " + url); // Debugging
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open link.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void loadBestValueVenue() throws SQLException {
        VenueServices venueServices = new VenueServices();

        try {
            Venue bestVenue = venueServices.bestValueVenue();

            if (bestVenue != null) {
                bestVenueLabel.setText(bestVenue.getVenueName() + " (Cost per Seat: " +
                        String.format("%.2f", bestVenue.getCost() / bestVenue.getNbrPlaces()) + " TND)");
            } else {
                bestVenueLabel.setText("No suitable venue found.");
            }

        } catch (SQLException e) {
            bestVenueLabel.setText("Error fetching best venue.");
            System.out.println(e.getMessage());
        }
    }


    @FXML
    public void navigateToAddVenue(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddVenue.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Venue");
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void navigateToUpdateVenue(Stage stage, Venue venue) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateVenue.fxml"));
            Parent root = loader.load();

            UpdateVenueController controller = loader.getController();
            controller.setCurrentVenue(venue);

            stage.setScene(new Scene(root));
            stage.setTitle("Update Venue");
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}