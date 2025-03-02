package tn.esprit.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import tn.esprit.entities.Partner;
import tn.esprit.services.PartnerServices;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.net.HttpURLConnection;
import java.net.URL;

public class ShowPartnerController {

    @FXML
    private TableView<Partner> partnerTable;

    @FXML
    private TableColumn<Partner, Integer> colPartnerId;

    @FXML
    private TableColumn<Partner, String> colName;

    @FXML
    private TableColumn<Partner, String> colCategory;

    @FXML
    private TableColumn<Partner, Void> colUpdate;

    @FXML
    private TableColumn<Partner, Void> colDelete;

    private final PartnerServices partnerServices = new PartnerServices();

    @FXML
    void initialize() {
        try {
            ObservableList<Partner> observableList = FXCollections.observableList(partnerServices.returnList());
            partnerTable.setItems(observableList);

            colPartnerId.setCellValueFactory(new PropertyValueFactory<>("partnerId"));
            colName.setCellValueFactory(new PropertyValueFactory<>("name"));
            colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));

            setUpButtons();
            addContextMenuToTable();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void setUpButtons() {
        colUpdate.setCellFactory(param -> new TableCell<>() {
            private final Button updateButton = new Button("Update");

            {
                updateButton.setOnAction(event -> {
                    Partner partner = getTableView().getItems().get(getIndex());
                    Stage stage = (Stage) updateButton.getScene().getWindow();
                    navigateToUpdatePartner(stage, partner);
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
                    Partner partner = getTableView().getItems().get(getIndex());
                    handleDeleteButtonClick(partner);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        });
    }

    private void handleDeleteButtonClick(Partner partner) {
        partnerServices.delete(partner);
        refreshTable();
    }

    private void refreshTable() {
        try {
            ObservableList<Partner> updatedList = FXCollections.observableList(partnerServices.returnList());
            partnerTable.setItems(updatedList);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void navigateToAddPartner(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddPartner.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Partner");
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void navigateToUpdatePartner(Stage stage, Partner partner) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdatePartner.fxml"));
            Parent root = loader.load();

            UpdatePartnerController controller = loader.getController();
            controller.setCurrentPartner(partner);

            stage.setScene(new Scene(root));
            stage.setTitle("Update Partner");
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void showCategoryStatistics() {
        try {
            List<Partner> partners = partnerServices.returnList();
            Map<String, Integer> categoryCount = new HashMap<>();

            for (Partner partner : partners) {
                categoryCount.put(partner.getCategory(), categoryCount.getOrDefault(partner.getCategory(), 0) + 1);
            }

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            for (Map.Entry<String, Integer> entry : categoryCount.entrySet()) {
                pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
            }

            PieChart pieChart = new PieChart(pieChartData);
            pieChart.setTitle("Partner Category Distribution");

            Alert chartDialog = new Alert(Alert.AlertType.INFORMATION);
            chartDialog.setTitle("Category Statistics");
            chartDialog.setHeaderText("Distribution of Partners by Category");
            chartDialog.getDialogPane().setContent(pieChart);
            chartDialog.showAndWait();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addContextMenuToTable() {
        partnerTable.setRowFactory(tableView -> {
            TableRow<Partner> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();

            MenuItem searchYouTube = new MenuItem("Search YouTube Video");
            searchYouTube.setOnAction(event -> {
                Partner selectedPartner = row.getItem();
                if (selectedPartner != null) {
                    searchYouTubeVideo(selectedPartner.getName());
                }
            });

            contextMenu.getItems().add(searchYouTube);

            row.setOnContextMenuRequested(event -> {
                if (!row.isEmpty()) {
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                }
            });

            row.setOnMouseClicked(event -> contextMenu.hide());

            return row;
        });
    }

    private void searchYouTubeVideo(String query) {
        try {
            String apiKey = "AIzaSyCttn8eZle2j6EBKBCJO_JKDhchwdT4YBA";  // Replace with your real API key
            String encodedQuery = URLEncoder.encode(query, "UTF-8");
            String apiUrl = "https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&q=" + encodedQuery + "&key=" + apiKey;

            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("GET");

            Scanner scanner = new Scanner(connection.getInputStream());
            StringBuilder response = new StringBuilder();

            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
            scanner.close();

            String videoId = extractFirstVideoId(response.toString());

            if (videoId != null) {
                openWebpage("https://www.youtube.com/watch?v=" + videoId);
            } else {
                showAlert("No Video Found", "No video found for: " + query);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to search YouTube.");
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
}
