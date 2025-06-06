package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import tn.esprit.services.EventService;
import tn.esprit.entities.Event;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ModifierEvent {

    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private DatePicker startDateField;
    @FXML
    private DatePicker endDateField;
    @FXML
    private TextField maxParticipantsField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField feeField;
    @FXML
    private TextField imageField;
    @FXML
    private ChoiceBox<Integer> userIdField;
    @FXML
    private TextField pdpPathField;
    @FXML
    private ImageView pdpImageView;
    @FXML
    private Label pdpIconLabel;

    // Validate form and update event
    @FXML
    void validate(ActionEvent event) throws IOException {
        try {
            int eventId = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            String startDate = startDateField.getValue().toString();
            String endDate = endDateField.getValue().toString();
            int maxParticipants = Integer.parseInt(maxParticipantsField.getText());
            String description = descriptionField.getText();
            int fee = Integer.parseInt(feeField.getText());
            int userId = userIdField.getValue();
            String image = pdpPathField.getText();

            Event updatedEvent = new Event(eventId, name, startDate, endDate, maxParticipants, description, fee, userId, image);

            EventService eventService = new EventService();
            eventService.modifier(updatedEvent);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmation");
            alert.setContentText("Événement modifié avec succès !");
            alert.show();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des valeurs valides pour les champs numériques (ID, Participants Max, Prix, User ID).");
        } catch (NullPointerException e) {
            showAlert("Erreur", "Tous les champs doivent être remplis.");
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur s'est produite : " + e.getMessage());
        }
    }

    public TextField getIdField() {
        return idField;
    }

    public void setIdField(TextField idField) {
        this.idField = idField;
    }

    public TextField getNameField() {
        return nameField;
    }

    public void setNameField(TextField nameField) {
        this.nameField = nameField;
    }

    public DatePicker getStartDateField() {
        return startDateField;
    }

    public void setStartDateField(DatePicker startDateField) {
        this.startDateField = startDateField;
    }

    public DatePicker getEndDateField() {
        return endDateField;
    }

    public void setEndDateField(DatePicker endDateField) {
        this.endDateField = endDateField;
    }

    public TextField getDescriptionField() {
        return descriptionField;
    }

    public void setDescriptionField(TextField descriptionField) {
        this.descriptionField = descriptionField;
    }

    public TextField getMaxParticipantsField() {
        return maxParticipantsField;
    }

    public void setMaxParticipantsField(TextField maxParticipantsField) {
        this.maxParticipantsField = maxParticipantsField;
    }

    public TextField getFeeField() {
        return feeField;
    }

    public void setFeeField(TextField feeField) {
        this.feeField = feeField;
    }

    public ChoiceBox<Integer> getUserIdField() {
        return userIdField;
    }

    public void setUserIdField(ChoiceBox<Integer> userIdField) {
        this.userIdField = userIdField;
    }

    public TextField getImageField() {
        return imageField;
    }

    public void setImageField(TextField imageField) {
        this.imageField = imageField;
    }

    public TextField getPdpPathField() {
        return pdpPathField;
    }

    public void setPdpPathField(TextField pdpPathField) {
        this.pdpPathField = pdpPathField;
    }

    public ImageView getPdpImageView() {
        return pdpImageView;
    }

    public void setPdpImageView(ImageView pdpImageView) {
        this.pdpImageView = pdpImageView;
    }

    public Label getPdpIconLabel() {
        return pdpIconLabel;
    }

    public void setPdpIconLabel(Label pdpIconLabel) {
        this.pdpIconLabel = pdpIconLabel;
    }

    public void initData(Event selectedEvent) {
        if (selectedEvent == null) {
            showAlert("Erreur", "Aucun événement sélectionné.");
            return;
        }

        // Initialize fields with selected event data
        idField.setText(String.valueOf(selectedEvent.getEventId()));
        idField.setDisable(true); // Prevent ID modification

        nameField.setText(selectedEvent.getName());
        startDateField.setValue(parseDate(selectedEvent.getStartDate()));
        endDateField.setValue(parseDate(selectedEvent.getEndDate()));
        maxParticipantsField.setText(String.valueOf(selectedEvent.getMaxParticipants()));
        descriptionField.setText(selectedEvent.getDescription());
        feeField.setText(String.valueOf(selectedEvent.getFee()));

        // Handle user ID (assuming userIdField is a ChoiceBox)
        userIdField.setValue(selectedEvent.getUserId());

        // Handle image path
        pdpPathField.setText(selectedEvent.getImage());

        // Display image if available
        if (selectedEvent.getImage() != null && !selectedEvent.getImage().isEmpty()) {
            File imageFile = new File(selectedEvent.getImage());
            if (imageFile.exists()) {
                Image image = new Image(imageFile.toURI().toString());
                pdpImageView.setImage(image);
            } else {
                showAlert("Erreur", "L'image de l'événement n'existe pas.");
            }
        }
    }


    // Méthode utilitaire pour parser la date en LocalDate
    private LocalDate parseDate(String dateString) {
        try {
            DateTimeFormatter formatter;
            if (dateString.contains("-")) {
                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            } else {
                formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
            }
            return LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Erreur de parsing de la date : " + e.getMessage());
            return null;
        }
    }

    // Initialize form with existing event data
    @FXML
    private void initialize() {
        EventService service = new EventService();
        userIdField.getItems().addAll(service.getAvailableIds("user", "userid"));

        if (!userIdField.getItems().isEmpty()) userIdField.setValue(userIdField.getItems().get(0));

        int eventId = getEventId(); // Replace with actual method to get event ID
        loadEventData(eventId);
    }

    // Load existing event data into the form
    public void loadEventData(int eventId) {
        EventService service = new EventService();
        Event event = service.getEventById(eventId); // Assuming this method exists

        if (event != null) {
            idField.setText(String.valueOf(event.getEventId()));
            nameField.setText(event.getName());
            startDateField.setValue(LocalDate.parse(event.getStartDate()));
            endDateField.setValue(LocalDate.parse(event.getEndDate()));
            maxParticipantsField.setText(String.valueOf(event.getMaxParticipants()));
            descriptionField.setText(event.getDescription());
            feeField.setText(String.valueOf(event.getFee()));
            userIdField.setValue(event.getUserId());
            pdpPathField.setText(event.getImage());
            pdpImageView.setImage(new Image(event.getImage()));
            pdpIconLabel.setVisible(false);
        }
    }

    // Choose an image for the event
    @FXML
    public void choisirImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            String imagePath = selectedFile.toURI().toString();
            pdpPathField.setText(selectedFile.getAbsolutePath());
            pdpImageView.setImage(new Image(imagePath));
            pdpIconLabel.setVisible(false);
        }
    }

    // Placeholder for getting the event ID
    private int getEventId() {
        // Implement how to retrieve the event ID
        return 0; // Replace with actual logic
    }

    // Show alert helper method
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }

    @FXML
    private void goToAjouterEvents(ActionEvent event) {
        loadScene(event, "/event.fxml");
    }

    @FXML
    private void goToAjouterEventss(ActionEvent event) {
        loadScene(event, "/addreservation.fxml");
    }

    private void loadScene(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}