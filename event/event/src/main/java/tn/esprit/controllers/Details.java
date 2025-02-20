package tn.esprit.controllers;

import tn.esprit.entities.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Details {

    @FXML
    private Label eventNameLabel;
    @FXML
    private Label eventStartDateLabel;
    @FXML
    private Label eventEndDateLabel;
    @FXML
    private Label eventMaxParticipantsLabel;
    @FXML
    private Label eventDescriptionLabel;
    @FXML
    private Label eventFeeLabel;
    @FXML
    private ImageView eventImageView;

    public void setEvent(Event event) {
        eventNameLabel.setText(event.getName());
        eventStartDateLabel.setText(event.getStartDate());
        eventEndDateLabel.setText(event.getEndDate());
        eventMaxParticipantsLabel.setText(String.valueOf(event.getMaxParticipants()));
        eventDescriptionLabel.setText(event.getDescription());
        eventFeeLabel.setText(String.valueOf(event.getFee()));

        String imagePath = event.getImage();
        if (imagePath != null && !imagePath.isEmpty()) {
            Image image = new Image("file:" + imagePath);
            eventImageView.setImage(image);
        } else {
            eventImageView.setImage(new Image("file:event.png")); // Placeholder image
        }
    }
 /*   public void setEvent(Event event) {
        //this.event = event;
        // Populate UI components with the event data
    }*/
}