package tn.esprit.controller;

import javafx.stage.FileChooser;
import org.json.JSONObject;
import tn.esprit.entities.Reservation;
import tn.esprit.services.ReservationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.*;
import java.net.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.event.ActionEvent;
import javax.sound.sampled.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.sound.sampled.*;  // Java Sound API (Built into Java)
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
public class Afficherreservation {

    @FXML
    private TableView<Reservation> reservationTable;
    @FXML
    private TableColumn<Reservation, Integer> colEventVenueId;
    @FXML
    private TableColumn<Reservation, Integer> colVenueId;
    @FXML
    private TableColumn<Reservation, Integer> colEventId;
    @FXML
    private TableColumn<Reservation, String> colReservationDate;
    @FXML
    private TableColumn<Reservation, String> colReservationPrice;
    @FXML
    private Button btnModifyEvents;

    @FXML
    private Button btnModifyReservation;
        @FXML
        private Button recognizeButton;
         @FXML
       private TextArea transcriptArea;
        @FXML
        private Button choose;
    @FXML
    private Button startButton, stopButton;
    private final ObservableList<Reservation> data = FXCollections.observableArrayList();
    private final ReservationService reservationService = new ReservationService();

    @FXML
    public void initialize() {
        reservationTable.getStylesheets().add(getClass().getResource("/styles/button.css").toExternalForm());
        configureTableColumns();
        loadData();
        setupContextMenu();
        reservationTable.setOnMouseClicked(event -> handleRowDoubleClick(event));
    }

    private void configureTableColumns() {
        colEventVenueId.setCellValueFactory(new PropertyValueFactory<>("eventVenueId"));
        colVenueId.setCellValueFactory(new PropertyValueFactory<>("venueId"));
        colEventId.setCellValueFactory(new PropertyValueFactory<>("eventId"));
        colReservationDate.setCellValueFactory(new PropertyValueFactory<>("reservationDate"));
        colReservationPrice.setCellValueFactory(new PropertyValueFactory<>("reservationPrice"));
    }

    @FXML
    public void loadData() {
        data.setAll(reservationService.rechercher()); // Assuming `rechercher` returns a list of `Reservation` objects
        reservationTable.setItems(data);
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

    private void setupContextMenu() {
        reservationTable.setRowFactory(tv -> {
            TableRow<Reservation> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();

            // Option "Supprimer"
            MenuItem deleteItem = new MenuItem("Supprimer");
            deleteItem.setOnAction(event -> {
                Reservation selectedReservation = row.getItem();
                if (selectedReservation != null) {
                    confirmAndDeleteReservation(selectedReservation);
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

    private void confirmAndDeleteReservation(Reservation reservation) {
        if (reservation == null) {
            showAlert("Erreur", "Aucune réservation sélectionnée.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation de suppression");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Voulez-vous vraiment supprimer cette réservation ?");

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                reservationService.supprimer(reservation);
                reservationTable.getItems().remove(reservation);
                reservationTable.refresh();
                showAlert("Suppression réussie", "La réservation a été supprimée avec succès.");
            }
        });
    }

    @FXML
    private void handleModifyReservation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifyReservation.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnModifyReservation.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleRowDoubleClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Reservation selectedReservation = reservationTable.getSelectionModel().getSelectedItem();

            if (selectedReservation != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifyReservation.fxml"));
                    Parent root = loader.load();
                    Modifyreservation modifierController = loader.getController();
                    modifierController.initData(selectedReservation);

                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Modifier Réservation");
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert("Erreur", "Impossible d'ouvrir la fenêtre de modification.");
                }
            } else {
                showAlert("Erreur", "Veuillez sélectionner une réservation.");
            }
        }
    }@FXML
    private void goToAjouterEventss(ActionEvent event) {
        try {
            // Charger la nouvelle page (ajoutez le bon chemin FXML)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/event3.fxml"));
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
    private void handleModifyEvents(ActionEvent event) {
        try {
            // Load the event2.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addreservation.fxml"));
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
    private void goToAjouter(ActionEvent event) {
        try {
            // Charger la nouvelle page (ajoutez le bon chemin FXML)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherParticipant.fxml"));
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



    private static final String ACCESS_TOKEN = "YCFB3UKMP4B3VOAWQGZKS3GYKJZN3DIY"; // Replace with your Wit.ai server access token
    private static final String FILE_NAME = "recorded_audio.wav";
    private TargetDataLine microphone;
    private Thread recordingThread;
    private boolean isRecording = false;



    @FXML
    public void startRecording(ActionEvent event) {
        isRecording = true;

        // Définition du format audio
        AudioFormat format = new AudioFormat(16000, 16, 1, true, true);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        try {
            if (!AudioSystem.isLineSupported(info)) {
                transcriptArea.setText("Microphone non supporté !");
                return;
            }

            microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(format);
            microphone.start();

            // Création du thread d'enregistrement
            recordingThread = new Thread(() -> {
                File wavFile = new File(FILE_NAME);

                try (ByteArrayOutputStream out = new ByteArrayOutputStream();
                     AudioInputStream ais = new AudioInputStream(microphone)) {

                    byte[] buffer = new byte[1024];
                    while (isRecording) {
                        int bytesRead = ais.read(buffer, 0, buffer.length);
                        if (bytesRead > 0) {
                            out.write(buffer, 0, bytesRead);
                        }
                    }

                    // Arrêter et fermer le microphone correctement
                    microphone.stop();
                    microphone.close();

                    // Sauvegarde du fichier audio avec un en-tête WAV valide
                    byte[] audioData = out.toByteArray();
                    try (ByteArrayInputStream bais = new ByteArrayInputStream(audioData);
                         AudioInputStream audioInputStream = new AudioInputStream(bais, format, audioData.length / format.getFrameSize())) {

                        AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, wavFile);
                    }

                    // Vérification : afficher l'emplacement du fichier
                    System.out.println("✅ Fichier enregistré : " + wavFile.getAbsolutePath());

                    // Envoyer le fichier audio à Wit.ai
                    sendAudioToWit(FILE_NAME);

                } catch (IOException e) {
                    e.printStackTrace();
                    transcriptArea.setText("Erreur lors de l'enregistrement : " + e.getMessage());
                }
            });

            recordingThread.start();
            transcriptArea.setText("Enregistrement en cours...");

        } catch (LineUnavailableException e) {
            e.printStackTrace();
            transcriptArea.setText("Erreur: " + e.getMessage());
        }
    }


    @FXML
    public void stopRecording(ActionEvent event) {
        isRecording = false;
        if (microphone != null) {
            microphone.stop();
            microphone.close();
        }
        transcriptArea.setText("Enregistrement arrêté. Envoi au serveur...");
    }

    private void sendAudioToWit(String audioFilePath) {
        File audioFile = new File(audioFilePath);
        try {
            URL url = new URL("https://api.wit.ai/speech?v=20250228");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + ACCESS_TOKEN);
            connection.setRequestProperty("Content-Type", "audio/wav");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream();
                 FileInputStream fis = new FileInputStream(audioFile)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
            }

            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }

                // Convertir la réponse JSON en un objet utilisable
                JSONObject jsonResponse = new JSONObject(response.toString());

                // Extraire le texte uniquement
                String recognizedText = jsonResponse.optString("text", "Aucune transcription");

                // Afficher uniquement le texte
                transcriptArea.setText(recognizedText.trim());
            }

        } catch (IOException e) {
            e.printStackTrace();
            transcriptArea.setText("Erreur d'envoi : " + e.getMessage());
        }
    }

}