

package tn.esprit.controllers;

import tn.esprit.entities.Event;


import tn.esprit.services.EventService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.io.IOException;
import java.util.Optional;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Eventback {

    public TextField txtSearch;

    @FXML
    private GridPane usersGrid;
    @FXML
    private ScrollPane scrollPane;

    public void initialize() {
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);  // Barre de défilement verticale toujours visible
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);   // Pas de barre de défilement horizontale
        scrollPane.setFitToWidth(true);  // Ajuste automatiquement la largeur du contenu à celle du ScrollPane
        scrollPane.setFitToHeight(false);  // Ne pas ajuster la hauteur automatiquement (permet de scroller)

        EventService eventService = new EventService(); // Création d'une instance
        ArrayList<Event> users = eventService.rechercher(); // Appel via l'instance
        usersGrid.getChildren().clear();
        populateUsersGrid(users);
    }

    private void populateUsersGrid(ArrayList<Event> users) {
        int columns = 4;  // Nombre de colonnes souhaitées
        int column = 0;

        int row = 0; // ✅ Start from the first row (was previously row = 2); // Commencer à la ligne 2 pour laisser de la place aux labels

        usersGrid.getChildren().clear();
        usersGrid.getColumnConstraints().clear();
        usersGrid.getRowConstraints().clear();


        // 🔹 Ajouter des contraintes de colonnes
        for (int i = 0; i < columns; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setHgrow(Priority.ALWAYS);
            usersGrid.getColumnConstraints().add(colConstraints);
        }

        // 🔹 Ajouter des contraintes de lignes
        int numRows = (int) Math.ceil(users.size() / (double) columns) + 2; // +2 pour les labels
        for (int i = 0; i < numRows; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setVgrow(Priority.ALWAYS);
            usersGrid.getRowConstraints().add(rowConstraints);
        }

        // 🔹 Ajouter les cartes des événements
        for (Event user : users) {
            VBox userCard = createUserCard(user);
            usersGrid.add(userCard, column, row);

            column++;
            if (column == columns) { // Passer à la ligne suivante si on atteint la limite
                column = 0;
                row++;
            }
        }

        // 🔹 Modifier la hauteur minimum pour que la GridPane s'adapte bien avec le contenu
        usersGrid.setMinHeight(600); // Cette ligne peut être ajustée pour ajuster la hauteur par défaut
        usersGrid.setPrefHeight(Region.USE_COMPUTED_SIZE); // Permet de s'ajuster à la taille du contenu

        // 🔹 Ajouter un ScrollPane (si ce n'est pas déjà fait dans votre FXML)
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(usersGrid);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);

        // Si vous avez déjà un ScrollPane, vous n'avez peut-être pas besoin de cette partie.
    }


    private VBox createUserCard(Event user) {
        EventService eventService = new EventService(); // Création d'une instance

        VBox card = new VBox(10); // Espacement vertical entre les éléments
        card.setAlignment(Pos.CENTER);
        card.getStyleClass().add("user-card");
        card.setUserData(user);

        // 🎨 Ajout de l'effet d'ombre pour un meilleur design
        DropShadow shadow = new DropShadow();
        shadow.setRadius(10);
        shadow.setOffsetX(5);
        shadow.setOffsetY(5);
        shadow.setColor(Color.rgb(0, 0, 0, 0.5));
        card.setEffect(shadow);

        // 🔹 Forcer la taille de la carte
        card.setMinSize(250, 400);
        card.setPrefSize(250, 400);
        card.setMaxSize(250, 400);

        // 🎨 En-tête en vert (date)
        StackPane header = new StackPane();
        header.getStyleClass().add("card-header");
        header.setPadding(new Insets(0, 0, 15, 0));

        // 📅 Date
        TextField dateField = new TextField("📅 Date: " + (user.getStartDate() != null ? user.getStartDate() : "N/A"));
        dateField.setFont(new Font(18));
        dateField.setEditable(false);
        dateField.setStyle("-fx-text-fill: white;" +
                "-fx-background-color: transparent;" +
                "-fx-border-color: transparent;" +
                "-fx-border-width: 0;" +
                "-fx-alignment: center;" +
                "-fx-font-size: 24px;");
        header.getChildren().add(dateField);
        StackPane.setAlignment(dateField, Pos.CENTER);

        // 🖼️ Image
        ImageView imageView = new ImageView();
        if (user.getImage() != null && !user.getImage().isEmpty()) {
            imageView.setImage(new Image("file:" + user.getImage()));
        }
        imageView.setFitWidth(250);
        imageView.setFitHeight(210);
        imageView.setPreserveRatio(true);

        // 🏷️ Label des informations
        Label infoLabel = new Label("🕒 Horaire: " + (user.getEndDate() != null ? user.getEndDate() : "N/A") +
                "\n📍 Lieu: " + (user.getDescription() != null ? user.getDescription() : "N/A"));
        infoLabel.getStyleClass().add("event-label");
        infoLabel.setWrapText(true);
        infoLabel.setFont(new Font(16));

        // 🎯 Gestion du clic gauche pour afficher l'événement
        card.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventsBack.fxml"));
                    Parent root = loader.load();
                    card.getScene().setRoot(root);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // 🗑️ Ajout du menu contextuel pour la suppression
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("🗑️ Supprimer");
        deleteItem.setOnAction(event -> {
            deleteEvent(user, card); // Appel de la méthode deleteEvent() pour la suppression de l'événement

            // 🔄 After deletion, refresh the event list
            Parent root = card.getScene().getRoot();
            if (root instanceof Pane) {
                ((Pane) root).getChildren().clear(); // Clear the UI to prepare for reload
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventsBack.fxml"));
                    Parent newRoot = loader.load();
                    card.getScene().setRoot(newRoot); // Reload the event list
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        contextMenu.getItems().add(deleteItem);

// 🎯 Gestion du clic droit pour afficher le menu contextuel
        card.setOnContextMenuRequested(event -> contextMenu.show(card, event.getScreenX(), event.getScreenY()));

// ✅ Ajouter un espaceur pour forcer l'expansion de la carte
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        VBox.setMargin(imageView, new Insets(0, 0, -10, 0));

// 📦 Ajout des éléments avec un bon espacement
        card.getChildren().addAll(header, imageView, spacer, infoLabel);

        return card;

    }

    /**
     * 🗑️ Méthode pour supprimer un événement
     */
    private void deleteEvent(Event user, VBox card) {
        EventService eventService = new EventService(); // Création d'une instance

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Voulez-vous vraiment supprimer cet événement ?");
        alert.setContentText(user.getDescription());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Suppression de la base de données
            eventService.supprimer(user);
            refreshGrid(); // ✅ Refresh the GridPane directly

            // Suppression de la carte visuellement
            VBox parentVBox = (VBox) card.getParent();
            parentVBox.getChildren().remove(card);

            // 🔄 Refresh the UI after deletion
            Parent root = card.getScene().getRoot();
            if (root instanceof Pane) {
                ((Pane) root).getChildren().clear();
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventsBack.fxml"));
                    Parent newRoot = loader.load();
                    card.getScene().setRoot(newRoot);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void refreshGrid() {
        EventService eventService = new EventService();
        ArrayList<Event> updatedEvents = eventService.rechercher();

        // Clear grid content and constraints
        usersGrid.getChildren().clear();
        usersGrid.getColumnConstraints().clear();
        usersGrid.getRowConstraints().clear();

        // Reset ScrollPane content to force refresh
        scrollPane.setContent(null); // Detach grid
        populateUsersGrid(updatedEvents); // Repopulate with row=0
        scrollPane.setContent(usersGrid); // Re-attach grid

        // Force UI update (critical for JavaFX rendering)
        usersGrid.requestLayout();
        Platform.runLater(() -> {
            usersGrid.requestLayout();
            scrollPane.requestLayout();
        });
    }




    private void createGridWithUserCards(ArrayList<Event> users) {
        // Créer un GridPane
        GridPane gridPane = new GridPane();
        gridPane.setHgap(20);  // Espacement horizontal entre les éléments
        gridPane.setVgap(10);  // Espacement vertical entre les lignes de cartes
        gridPane.setPadding(new Insets(10)); // Padding autour du GridPane

        // Nombre de colonnes souhaitées
        int columns = 2; // Ajustez le nombre de colonnes ici

        // Ajouter des contraintes de colonnes au GridPane (si nécessaire)
        for (int i = 0; i < columns; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setHgrow(Priority.ALWAYS); // La colonne doit grandir en fonction de l'espace disponible
            gridPane.getColumnConstraints().add(colConstraints);
        }

        // Ajouter des cartes utilisateurs au GridPane
        for (int i = 0; i < users.size(); i++) {
            Event user = users.get(i);
            VBox userCard = createUserCard(user);  // Créer la carte utilisateur

            // Calculer la ligne et la colonne où la carte doit être ajoutée
            int row = i / columns;  // Calcul de la ligne
            int col = i % columns;  // Calcul de la colonne

            // Si la ligne dépasse la taille du GridPane, on ajoute une nouvelle ligne
            if (gridPane.getRowConstraints().size() <= row) {
                RowConstraints rowConstraints = new RowConstraints();
                rowConstraints.setVgrow(Priority.ALWAYS); // Laisser la ligne grandir selon l'espace disponible
                gridPane.getRowConstraints().add(rowConstraints);
            }

            // Ajouter la carte dans la cellule correspondante
            gridPane.add(userCard, col, row);
        }

        // Ajoutez gridPane à un conteneur parent si nécessaire (ex. root.getChildren().add(gridPane));
    }

}