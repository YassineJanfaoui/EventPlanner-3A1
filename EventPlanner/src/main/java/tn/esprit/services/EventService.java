package tn.esprit.services;

import tn.esprit.entities.Event;
import tn.esprit.entities.Team;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class EventService implements IService<Event> {

    private Connection connection = MyDatabase.getInstance().getConnection();
    public void ajouter(Event event) {
        String req = "INSERT INTO event (name, startDate, endDate, maxParticipants, description, fee, userId, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, event.getName());
            pst.setString(2, event.getStartDate());
            pst.setString(3, event.getEndDate());
            pst.setInt(4, event.getMaxParticipants());
            pst.setString(5, event.getDescription());
            pst.setInt(6, event.getFee());
            pst.setInt(7, event.getUserId());
            pst.setString(8, event.getImage()); // Set image
            pst.executeUpdate();
            System.out.println("Événement ajouté");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void modifier(Event event) {
        String req = "UPDATE event SET name=?, startDate=?, endDate=?, maxParticipants=?, description=?, fee=?, userId=?, image=? WHERE eventId=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, event.getName());
            pst.setString(2, event.getStartDate());
            pst.setString(3, event.getEndDate());
            pst.setInt(4, event.getMaxParticipants());
            pst.setString(5, event.getDescription());
            pst.setInt(6, event.getFee());
            pst.setInt(7, event.getUserId());
            pst.setString(8, event.getImage()); // Set image
            pst.setInt(9, event.getEventId());
            pst.executeUpdate();
            System.out.println("Événement modifié");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void supprimer(Event event) {
        String req = "DELETE FROM event WHERE eventId=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, event.getEventId());
            pst.executeUpdate();
            System.out.println("Événement supprimé");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public ArrayList<Event> rechercher() {
        ArrayList<Event> events = new ArrayList<>();
        String req = "SELECT * FROM event";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                events.add(new Event(
                        rs.getInt("eventId"),
                        rs.getString("name"),
                        rs.getString("startDate"),
                        rs.getString("endDate"),
                        rs.getInt("maxParticipants"),
                        rs.getString("description"),
                        rs.getInt("fee"),
                        rs.getInt("userId"),
                        rs.getString("image") // Retrieve image
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return events;
    }

        public ArrayList<Integer> getAvailableIds(String table, String column) {
            ArrayList<Integer> ids = new ArrayList<>();

            // Construire dynamiquement la requête SQL
            String query = "SELECT " + column + " FROM " + table;

            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    ids.add(rs.getInt(column)); // Récupérer les IDs
                }

            } catch (SQLException e) {
                System.out.println("Erreur SQL : " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println("Autre erreur : " + e.getMessage());
                e.printStackTrace();
            }

            return ids;
        }
    public Event getEventById(int eventId) {
        Event event = null;
        String req = "SELECT * FROM event WHERE eventId = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, eventId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                event = new Event(
                        rs.getInt("eventId"),
                        rs.getString("name"),
                        rs.getString("startDate"),
                        rs.getString("endDate"),
                        rs.getInt("maxParticipants"),
                        rs.getString("description"),
                        rs.getInt("fee"),
                        rs.getInt("userId"),
                        rs.getString("image") // Retrieve image
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return event;
    }

    @Override
    public void add(Event event) throws SQLException {
        String req = "INSERT INTO event (name, startDate, endDate, maxParticipants, description, fee, userId, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, event.getName());
            pst.setString(2, event.getStartDate());
            pst.setString(3, event.getEndDate());
            pst.setInt(4, event.getMaxParticipants());
            pst.setString(5, event.getDescription());
            pst.setInt(6, event.getFee());
            pst.setInt(7, event.getUserId());
            pst.setString(8, event.getImage()); // Set image
            pst.executeUpdate();
            System.out.println("Événement ajouté");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addP(Event event) throws SQLException {

    }

    @Override
    public List<Event> returnList() throws SQLException {
        return List.of();
    }

    @Override
    public void delete(Event event) {

    }

    @Override
    public void update(Event event) {

    }
    public ArrayList<Event> rechercherParNom(String chaine) {
        ArrayList<Event> events = new ArrayList<>();

        String req = "SELECT * FROM event WHERE " +
                "name LIKE ? OR startDate LIKE ? OR description LIKE ?";

        try {
            PreparedStatement pst = connection.prepareStatement(req);

            // Appliquer le filtre sur les champs pertinents
            for (int i = 1; i <= 3; i++) {
                pst.setString(i, "%" + chaine + "%");
            }

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                events.add(new Event(
                        rs.getInt("eventId"),         // eventId as id
                        rs.getString("name"),         // name as name
                        rs.getString("startDate"),    // startDate as startDate (String)
                        rs.getString("endDate"),      // endDate as endDate (String)
                        rs.getInt("maxParticipants"),// maxParticipants
                        rs.getString("description"), // description as description
                        rs.getInt("fee"),             // fee as fee
                        rs.getInt("userId"),          // userId as userId (foreign key)
                        rs.getString("image")         // image as image
                ));
            }



        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }

        return events;
    }


    public ArrayList<Event> affichageAvecTri(String critere) {
        ArrayList<Event> events = new ArrayList<>();

        // Liste des critères autorisés pour éviter l'injection SQL
        List<String> criteresAutorises = Arrays.asList("startDate", "fee", "description", "maxParticipants");

        // Vérification si le critère est valide, sinon utiliser un critère par défaut
        if (!criteresAutorises.contains(critere)) {
            System.out.println("Critère de tri invalide, utilisation du tri par défaut (date).");
            critere = "startDate"; // Tri par défaut
        }

        // Construction sécurisée de la requête SQL
        String req = "SELECT * FROM event ORDER BY " + critere;

        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                events.add(new Event(
                        rs.getInt("eventId"),         // eventId as id
                        rs.getString("name"),         // name as name
                        rs.getString("startDate"),    // startDate as startDate (String)
                        rs.getString("endDate"),      // endDate as endDate (String)
                        rs.getInt("maxParticipants"),// maxParticipants
                        rs.getString("description"), // description as description
                        rs.getInt("fee"),             // fee as fee
                        rs.getInt("userId"),          // userId as userId (foreign key)
                        rs.getString("image")         // image as image
                ));
            }

        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }

        return events;
    }
    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        String query = "SELECT * FROM event";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Event event = new Event(
                        resultSet.getInt("eventId"),
                        resultSet.getString("name"),
                        resultSet.getString("startDate"),
                        resultSet.getString("endDate"),
                        resultSet.getInt("maxParticipants"),
                        resultSet.getString("description"),
                        resultSet.getInt("fee"),
                        resultSet.getInt("userid"),
                        resultSet.getString("image")
                );
                events.add(event);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return events;
    }
    public List<Team> getTeamsByEventId(int eventId) {
        List<Team> teams = new ArrayList<>();
        String query = "SELECT * FROM team WHERE eventId = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, eventId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Team team = new Team(
                        resultSet.getInt("teamid"),
                        resultSet.getString("TeamName"),
                        resultSet.getInt("Score"),
                        resultSet.getInt("Rank"),
                        resultSet.getInt("eventId")
                );
                teams.add(team);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return teams;
    }
}
