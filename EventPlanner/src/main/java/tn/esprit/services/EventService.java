package tn.esprit.services;

import tn.esprit.entities.Event;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

}
