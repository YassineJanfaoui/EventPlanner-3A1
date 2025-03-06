package tn.esprit.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.entities.Event;
import tn.esprit.entities.Participant;
import tn.esprit.entities.Team;
import tn.esprit.services.EventService;
import tn.esprit.services.ParticipantService;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class AddParticipantController {

    @FXML
    private TextField AffiliationField;

    @FXML
    private TextField Age;

    @FXML
    private TextField nameField;

    @FXML
    private ComboBox<Team> teamComboBox;

    @FXML
    private TextField skillsField; // For entering skills

    @FXML
    private TextArea recommendationArea; // For displaying recommendations

    private final ParticipantService participantService = new ParticipantService();
    private final EventService eventService = new EventService(); // Service to fetch events

    private WordVectors wordVectors; // Pre-trained word vectors

    public AddParticipantController() {
        // Load pre-trained word vectors (e.g., GloVe or Word2Vec)
        try {
            String filePath = getClass().getResource("/glove.6B.50d.txt").getFile();
            File gloveFile = new File(filePath);
            if (!gloveFile.exists()) {
                throw new FileNotFoundException("GloVe file not found at: " + filePath);
            }
            wordVectors = WordVectorSerializer.loadTxtVectors(gloveFile);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load GloVe vectors: " + e.getMessage());
        }
    }

    @FXML
    void navigateToShowParticipant(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ShowParticipant.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Show Participant");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        List<Team> teams = participantService.getTeams();
        teamComboBox.getItems().addAll(teams);

        teamComboBox.setCellFactory(param -> new ListCell<Team>() {
            @Override
            protected void updateItem(Team team, boolean empty) {
                super.updateItem(team, empty);
                if (empty || team == null) {
                    setText(null);
                } else {
                    setText(team.getTeamName());
                }
            }
        });

        teamComboBox.setButtonCell(new ListCell<Team>() {
            @Override
            protected void updateItem(Team team, boolean empty) {
                super.updateItem(team, empty);
                if (empty || team == null) {
                    setText(null);
                } else {
                    setText(team.getTeamName());
                }
            }
        });
    }

    @FXML
    public void handleAddParticipant(ActionEvent event) {
        String name = nameField.getText().trim();
        String affiliation = AffiliationField.getText().trim();
        Team selectedTeam = teamComboBox.getSelectionModel().getSelectedItem();
        String ageText = Age.getText().trim();

        if (name.isEmpty() || affiliation.isEmpty() || selectedTeam == null || ageText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please fill all fields.");
            return;
        }

        try {
            int age = Integer.parseInt(ageText);
            if (age < 0) {
                showAlert(Alert.AlertType.ERROR, "Form Error!", "Age must be a positive number.");
                return;
            }

            Participant updatedParticipant = new Participant(name, affiliation, age, selectedTeam.getId());
            participantService.add(updatedParticipant);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Participant updated successfully.");

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Age must be a valid number.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void handleRecommendEvents(ActionEvent event) {
        String skills = skillsField.getText().trim();
        if (skills.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter your skills.");
            return;
        }

        // Fetch all events from the database
        List<Event> events = eventService.getAllEvents();

        // Use NLP to recommend events
        String recommendations = recommendEvents(skills, events);

        // Display recommendations
        recommendationArea.setText(recommendations);
    }

    private String recommendEvents(String skills, List<Event> events) {
        if (wordVectors == null) {
            return "Error: Word vectors are not loaded.";
        }

        StringBuilder recommendations = new StringBuilder("Recommended Events:\n\n");

        // Convert skills to a vector
        RealVector skillsVector = getSentenceVector(skills);

        // Calculate similarity scores for each event
        Map<Event, Double> eventScores = new HashMap<>();
        for (Event event : events) {
            RealVector eventVector = getSentenceVector(event.getName() + " " + event.getDescription());
            double similarity = cosineSimilarity(skillsVector, eventVector);
            eventScores.put(event, similarity);

            // Debug: Print similarity scores
            System.out.println("Event: " + event.getName() + ", Similarity: " + similarity);
        }

        // Sort events by similarity score
        List<Map.Entry<Event, Double>> sortedEvents = new ArrayList<>(eventScores.entrySet());
        sortedEvents.sort((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()));

        // Display top 5 events with similarity above a threshold
        double similarityThreshold = 0.5; // Adjust this threshold as needed
        int count = 0;
        for (Map.Entry<Event, Double> entry : sortedEvents) {
            if (count >= 5) break; // Limit to top 5 recommendations
            if (entry.getValue() >= similarityThreshold) { // Filter by similarity threshold
                Event event = entry.getKey();
                recommendations.append("• ").append(event.getName()).append(": ").append(event.getDescription()).append("\n");

                // Fetch teams associated with this event
                List<Team> teams = eventService.getTeamsByEventId(event.getEventId());
                if (!teams.isEmpty()) {
                    recommendations.append("   Teams: ");
                    for (Team team : teams) {
                        recommendations.append(team.getTeamName()).append(", ");
                    }
                    recommendations.delete(recommendations.length() - 2, recommendations.length()); // Remove the last ", "
                    recommendations.append("\n");
                } else {
                    recommendations.append("   No teams associated with this event.\n");
                }

                count++;
            }
        }

        if (recommendations.toString().equals("Recommended Events:\n\n")) {
            return "No events match your skills.";
        }

        return recommendations.toString();
    }    private RealVector getSentenceVector(String text) {
        if (wordVectors == null) {
            throw new IllegalStateException("Word vectors are not loaded.");
        }

        String[] words = text.toLowerCase().split(" ");
        RealVector vector = new ArrayRealVector(wordVectors.getWordVector("the").length); // Initialize with zeros

        int count = 0;
        for (String word : words) {
            if (wordVectors.hasWord(word)) {
                vector = vector.add(new ArrayRealVector(wordVectors.getWordVector(word)));
                count++;
            }
        }

        if (count > 0) {
            vector = vector.mapDivide(count); // Average the vectors
        }

        return vector;
    }

    private double cosineSimilarity(RealVector v1, RealVector v2) {
        return v1.dotProduct(v2) / (v1.getNorm() * v2.getNorm());
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}