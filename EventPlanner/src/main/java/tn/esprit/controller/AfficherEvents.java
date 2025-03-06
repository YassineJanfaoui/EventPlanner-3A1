package tn.esprit.controller;

import com.google.protobuf.compiler.PluginProtos;
import com.google.zxing.WriterException;
import com.itextpdf.text.DocumentException;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import tn.esprit.entities.Event;
import tn.esprit.entities.PDFEVENT;
import tn.esprit.services.EventService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import java.io.File; // Correct import
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.event.ActionEvent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class AfficherEvents {

    @FXML
    private TableView<Event> eventTable;
    @FXML
    private TableColumn<Event, Integer> colId;
    @FXML
    private TableColumn<Event, String> colName;
    @FXML
    private TableColumn<Event, String> colStartDate;
    @FXML
    private TableColumn<Event, String> colEndDate;
    @FXML
    private TableColumn<Event, Integer> colMaxParticipants;
    @FXML
    private TableColumn<Event, String> colDescription;
    @FXML
    private TableColumn<Event, Integer> colFee;
    @FXML
    private TableColumn<Event, Integer> colUserId;
    @FXML
    private TableColumn<Event, String> colImage;
    @FXML
    private Button btnModifyEvent; // Button to navigate to Modify Event
    @FXML
    private Button btnModifyEvents;
    @FXML
    private Button btnExporttopdf;
    @FXML
    private Button btnexcel;
    @FXML
    private Button btnplay;
    @FXML
    private Button btnplayy;
    @FXML
    private Button btncsv;

    private ObservableList<Event> data = FXCollections.observableArrayList();
    private EventService eventService = new EventService();



    @FXML
    public void initialize() {
        loadData();
        setupContextMenu();
        eventTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                handleRowDoubleClick(event);
            }
        });
    }

    @FXML
    public void loadData() {
        data.clear();  // Clear the existing data
        data.addAll(eventService.rechercher());  // Load data from the service

        colId.setCellValueFactory(new PropertyValueFactory<>("eventId"));
        colId.setVisible(false); // Hide the column
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        colMaxParticipants.setCellValueFactory(new PropertyValueFactory<>("maxParticipants"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colFee.setCellValueFactory(new PropertyValueFactory<>("fee"));
        colUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colImage.setCellValueFactory(new PropertyValueFactory<>("image"));  // Get the image path

        // Set custom cell factory for image column
        colImage.setCellFactory(param -> new TableCell<Event, String>() {
            private final ImageView imageView = new ImageView();
            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);

                if (empty || imagePath == null || imagePath.isEmpty()) {
                    setGraphic(null);
                } else {
                    Image image = new Image("file:" + imagePath);  // Load image from file system
                    imageView.setImage(image);
                    imageView.setFitWidth(100);
                    imageView.setFitHeight(100);
                    imageView.setPreserveRatio(true);
                    setGraphic(imageView);
                }
            }
        });

        eventTable.setItems(data);  // Bind data to TableView
    }

    @FXML
    private void refreshTable() {
        loadData();
        showAlert("Mise à jour", "Les données ont été rafraîchies avec succès.");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void goToAjouterEvent(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/event.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupContextMenu() {
        eventTable.setRowFactory(tv -> {
            TableRow<Event> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();

            MenuItem deleteItem = new MenuItem("Supprimer");
            deleteItem.setOnAction(event -> {
                Event selectedEvent = row.getItem();
                if (selectedEvent != null) {
                    confirmAndDeleteEvent(selectedEvent);
                }
            });

            contextMenu.getItems().add(deleteItem);

            row.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY && !row.isEmpty()) {
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                } else {
                    contextMenu.hide();
                }
            });

            return row;
        });
    }

    private void confirmAndDeleteEvent(Event event) {
        if (event == null) {
            showAlert("Erreur", "Aucun événement sélectionné.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation de suppression");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Voulez-vous vraiment supprimer cet événement ?");

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                eventService.supprimer(event);
                data.remove(event);
                eventTable.getItems().remove(event);
                eventTable.refresh();
                showAlert("Suppression réussie", "L'événement a été supprimé avec succès.");
            }
        });
    }

    @FXML
    public void handleRowDoubleClick(MouseEvent event) {
        if (event.getClickCount() == 2) { // Ensure it's a double-click event
            Event selectedEvent = eventTable.getSelectionModel().getSelectedItem();

            if (selectedEvent != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/event2.fxml"));
                    Parent root = loader.load();

                    // Get controller and pass the selected event data
                    ModifierEvent modifierController = loader.getController();
                    modifierController.initData(selectedEvent);

                    // Open modification window
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Modifier Événement");
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert("Erreur", "Impossible d'ouvrir la fenêtre de modification.");
                }
            } else {
                showAlert("Erreur", "Veuillez sélectionner un événement.");
            }
        }
    }

    @FXML
    private void handleModifyEvent(ActionEvent event) {
        try {
            // Load the event2.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/event2.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) btnModifyEvent.getScene().getWindow();

            // Set the new scene with the event2.fxml content
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleModifyEvents(ActionEvent event) {
        try {
            // Load the event2.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/event.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) btnModifyEvents.getScene().getWindow();

            // Set the new scene with the event2.fxml content
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToAjouterEventsss(ActionEvent event) {
        try {
            // Charger la nouvelle page (ajoutez le bon chemin FXML)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherreservation.fxml"));
            Parent root = loader.load();

            // Récupérer la scène actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Changer la scène
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void NavToStat(ActionEvent event) {
        try {
            // Charger la nouvelle page (ajoutez le bon chemin FXML)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Stat.fxml"));
            Parent root = loader.load();

            // Récupérer la scène actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Changer la scène
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void exportToPdf() {
        EventService eventService = new EventService();
        try {
            ArrayList<Event> events = eventService.rechercher();
            // Récupérer les événements
            PDFEVENT pdfGenerator = new PDFEVENT();
            ArrayList<Event> eventsAvecRevenu = eventService.rechercher();
            pdfGenerator.generatePdf("Liste_Events", eventsAvecRevenu, eventService);
            System.out.println("✅ PDF généré avec succès !");
        } catch (DocumentException | IOException e) {
            System.err.println("❌ Erreur lors de l'exportation du PDF: " + e.getMessage());
        } catch (com.itextpdf.text.pdf.qrcode.WriterException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void excelfile(ActionEvent event) {
        ArrayList<Event> data = new ArrayList<Event>(); // Change to Event

        try {
            // Creating an instance of HSSFWorkbook class
            String filename = "D:\\Desktop\\branchSemah\\EventPlanner-3A1\\EventPlanner\\src\\main\\java\\tn\\esprit\\excel\\DonnéeÉvénements.XLS";
            HSSFWorkbook workbook = new HSSFWorkbook();
//invoking creatSheet() method and passing the name of the sheet to be created
            HSSFSheet sheet = workbook.createSheet("Event Details");
//creating the 0th row using the createRow() method
            HSSFRow rowhead = sheet.createRow((short)0);

            // Creating the header row
            rowhead.createCell(0).setCellValue("Event ID");
            rowhead.createCell(1).setCellValue("Name");
            rowhead.createCell(2).setCellValue("Start Date");
            rowhead.createCell(3).setCellValue("End Date");
            rowhead.createCell(4).setCellValue("Max Participants");
            rowhead.createCell(5).setCellValue("Description");
            rowhead.createCell(6).setCellValue("Fee");
            rowhead.createCell(7).setCellValue("User ID");
            rowhead.createCell(8).setCellValue("Image");

            // Assuming you have a method to get the list of events
            EventService eventService = new EventService(); // Create an instance of EventService
            ObservableList<Event> eventList = FXCollections.observableArrayList(eventService.rechercher()); // Use the rechercher method to get events
            int rownum = 1;
            for (Event eventItem : eventList) {
                HSSFRow row = sheet.createRow(rownum++);
                row.createCell(0).setCellValue(eventItem.getEventId());
                row.createCell(1).setCellValue(eventItem.getName());
                row.createCell(2).setCellValue(eventItem.getStartDate());
                row.createCell(3).setCellValue(eventItem.getEndDate());
                row.createCell(4).setCellValue(eventItem.getMaxParticipants());
                row.createCell(5).setCellValue(eventItem.getDescription());
                row.createCell(6).setCellValue(eventItem.getFee());
                row.createCell(7).setCellValue(eventItem.getUserId());
                row.createCell(8).setCellValue(eventItem.getImage());
            }

            FileOutputStream fileOut = new FileOutputStream(filename);
            workbook.write(fileOut);

            //closing the Stream
            fileOut.close();
            //closing the workbook
            workbook.close();
            //prints the message on the console
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Excel File Has Been Generated Successfully", ButtonType.OK);
            a.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    String path = "D:/telechargement/event/event/src/main/java/tn/esprit/media/event.mp3";    Media media = new Media(new File(path).toURI().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(media);

    @FXML
    private void pause(ActionEvent event) {

        mediaPlayer.pause();
        // Image img = new Image("fllogo.png");
        Notifications notificationBuilder = Notifications.create()
                .title("Musique")
                .text("      Musique Arrêtée").hideAfter(Duration.seconds(5))
                .position(Pos.BOTTOM_RIGHT);
        notificationBuilder.darkStyle();
        notificationBuilder.show();
    }

    @FXML
    private void play(ActionEvent event) {

        mediaPlayer.play();
        //  Image img = new Image("C:\\Users\\Saleh\\Desktop\\GenesisTeam_MaktabtiApp\\DesktopApp\\src\\edu\\esprit\\gui\\images\\ticket.png");
        Notifications notificationBuilder = Notifications.create()
                .title("Musique")
                .text("      Musique Jouée").hideAfter(Duration.seconds(5))
                .position(Pos.BOTTOM_RIGHT);
        notificationBuilder.darkStyle();
        notificationBuilder.show();
    }


        // Initialize the eventList in the constructor
    @FXML
    private void generateCsvFile(ActionEvent event) {
        EventService eventService = new EventService(); // Create an instance of EventService
        ObservableList<Event> eventList = FXCollections.observableArrayList(eventService.rechercher());
        String filename = "D:\\Desktop\\branchSemah\\EventPlanner-3A1\\EventPlanner\\src\\main\\java\\tn\\esprit\\csv\\events.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filename)))) {
            // Write CSV header
            writer.write("Event ID,Name,Start Date,End Date,Max Participants,Description,Fee,User ID,Image");
            writer.newLine();

            // Write event data to CSV
            for (Event eventItem : eventList) {
                writer.write(eventItem.getEventId() + "," +
                        eventItem.getName() + "," +
                        eventItem.getStartDate() + "," +
                        eventItem.getEndDate() + "," +
                        eventItem.getMaxParticipants() + "," +
                        eventItem.getDescription() + "," +
                        eventItem.getFee() + "," +
                        eventItem.getUserId() + "," +
                        eventItem.getImage());
                writer.newLine();
            }

            // Show success alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "CSV File Has Been Generated Successfully", ButtonType.OK);
            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error Generating CSV File: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }
}



