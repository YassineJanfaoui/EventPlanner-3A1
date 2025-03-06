package tn.esprit.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import tn.esprit.entities.Team;
import tn.esprit.services.TeamServices;

import java.sql.SQLException;
import java.util.Comparator;

public class LeaderboardController {

    @FXML private TableView<Team> leaderboardTable;
    @FXML private TableColumn<Team, Void> colRank;  // Changed to Void
    @FXML private TableColumn<Team, String> colTeamName;
    @FXML private TableColumn<Team, Integer> colScore;
    @FXML private TableColumn<Team, Integer> colEvent;
    @FXML private ComboBox<String> sortComboBox;
    @FXML private TextField searchField;

    // Class-level variable must be initialized and used everywhere
    private ObservableList<Team> originalTeams;

    private FilteredList<Team> filteredTeams;
    private SortedList<Team> sortedTeams;

    public void initialize() {
        TeamServices teamServices = new TeamServices();
        try {
            // Initialize originalTeams (do not shadow the class variable)
            originalTeams = FXCollections.observableArrayList(teamServices.getLeaderboard());

            // Set up table columns
            colTeamName.setCellValueFactory(new PropertyValueFactory<>("teamName"));
            colScore.setCellValueFactory(new PropertyValueFactory<>("score"));
            colEvent.setCellValueFactory(new PropertyValueFactory<>("eventId"));

            // Dynamic ranking column
            colRank.setCellFactory(column -> new TableCell<Team, Void>() {
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                        setText(null);
                    } else {
                        setText(String.valueOf(getIndex() + 1));
                    }
                }
            });

            // Initially set the table items to the full list (will be overridden in loadData)
            leaderboardTable.setItems(originalTeams);

            setupSearch();
            setupSortComboBox();
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupSortComboBox() {
        sortComboBox.getItems().addAll("Sort by Name", "Sort by Score");
        sortComboBox.setValue("Sort by Score"); // Default selection

        sortComboBox.setOnAction(e -> {
            switch(sortComboBox.getValue()) {
                case "Sort by Name" -> sortByName();
                case "Sort by Score" -> sortByScore();
            }
        });
    }

    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredTeams.setPredicate(team -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return team.getTeamName().toLowerCase().contains(lowerCaseFilter) ||
                        String.valueOf(team.getScore()).contains(lowerCaseFilter);
            });
            updateRankColumn();
        });
    }

    private void loadData() {
        try {
            // Use the class-level originalTeams that was already initialized
            filteredTeams = new FilteredList<>(originalTeams);
            sortedTeams = new SortedList<>(filteredTeams);

            // Bind the sorted comparator to the table's comparator property
            sortedTeams.comparatorProperty().bind(leaderboardTable.comparatorProperty());

            // Set the table's items to the sorted list
            leaderboardTable.setItems(sortedTeams);

            // Apply the default sort
            sortByScore();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sortByName() {
        // Create a new SortedList using the filtered list so that sorting respects the search predicate
        SortedList<Team> sortedList = new SortedList<>(filteredTeams);
        sortedList.setComparator(Comparator.comparing(Team::getTeamName));
        leaderboardTable.setItems(sortedList);
        updateRankColumn();
    }

    private void sortByScore() {
        // Create a new SortedList using the filtered list so that sorting respects the search predicate
        SortedList<Team> sortedList = new SortedList<>(filteredTeams);
        sortedList.setComparator(Comparator.comparingInt(Team::getScore).reversed());
        leaderboardTable.setItems(sortedList);
        updateRankColumn();
    }

    private void updateRankColumn() {
        colRank.setCellFactory(column -> new TableCell<Team, Void>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                } else {
                    setText(String.valueOf(getIndex() + 1));
                }
            }
        });
    }

    @FXML
    public void navigateBack() {
        try {
            Stage stage = (Stage) leaderboardTable.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/ShowTeam.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("Teams List");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
