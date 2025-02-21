package tn.esprit.services;

import tn.esprit.entities.Reservation;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationService implements IService<Reservation> {

    private Connection connection = MyDatabase.getInstance().getConnection();


    public void ajouter(Reservation reservation) {
        String req = "INSERT INTO reservation (venueId, eventId, reservationDate, reservationPrice) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setObject(1, reservation.getVenueId());
            pst.setObject(2, reservation.getEventId());
            pst.setString(3, reservation.getReservationDate());
            pst.setString(4, reservation.getReservationPrice());
            pst.executeUpdate();
            System.out.println("Reservation ajoutée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void modifier(Reservation reservation) {
        String req = "UPDATE reservation SET venueId=?, eventId=?, reservationDate=?, reservationPrice=? WHERE eventVenueId=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setObject(1, reservation.getVenueId());
            pst.setObject(2, reservation.getEventId());
            pst.setString(3, reservation.getReservationDate());
            pst.setString(4, reservation.getReservationPrice());
            pst.setInt(5, reservation.getEventVenueId());
            pst.executeUpdate();
            System.out.println("Reservation modifiée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void supprimer(Reservation reservation) {
        String req = "DELETE FROM reservation WHERE eventVenueId=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, reservation.getEventVenueId());
            pst.executeUpdate();
            System.out.println("Reservation supprimée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
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

    public ArrayList<Reservation> rechercher() {
        ArrayList<Reservation> reservations = new ArrayList<>();
        String req = "SELECT * FROM reservation";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                reservations.add(new Reservation(
                        rs.getInt("eventVenueId"),
                        rs.getObject("venueId") != null ? (Integer) rs.getObject("venueId") : null,
                        rs.getObject("eventId") != null ? (Integer) rs.getObject("eventId") : null,
                        rs.getString("reservationDate"),
                        rs.getString("reservationPrice")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return reservations;
    }
    public Reservation getReservationById(int reservationId) {
        Reservation reservation = null;
        String req = "SELECT * FROM reservation WHERE eventVenueId = ?"; // Use correct primary key
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, reservationId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                reservation = new Reservation(
                        rs.getInt("eventVenueId"), // Assuming eventVenueId is the primary key
                        rs.getInt("venueId"),      // Foreign key, can be null
                        rs.getInt("eventId"),      // Foreign key, can be null
                        rs.getString("reservationDate"),
                        rs.getString("reservationPrice")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return reservation;
    }public ArrayList<Integer> getAvailableIdss(String table, String column) {
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

    @Override
    public void add(Reservation reservation) throws SQLException {

    }

    @Override
    public void addP(Reservation reservation) throws SQLException {

    }

    @Override
    public List<Reservation> returnList() throws SQLException {
        return List.of();
    }

    @Override
    public void delete(Reservation reservation) {

    }

    @Override
    public void update(Reservation reservation) {

    }
}