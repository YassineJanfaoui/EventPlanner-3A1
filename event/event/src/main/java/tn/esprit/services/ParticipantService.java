package tn.esprit.services;

import tn.esprit.entities.Participant;
import tn.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;

public class ParticipantService implements iService<Participant> {

    private Connection connection = DataSource.getInstance().getConnection();

    @Override
    public void ajouter(Participant participant) {
        String req = "INSERT INTO participant (name, affiliation, age, teamid) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, participant.getName());
            pst.setString(2, participant.getAffiliation());
            pst.setInt(3, participant.getAge());
            pst.setInt(4, participant.getTeamid());
            pst.executeUpdate();
            System.out.println("Participant ajouté");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(Participant participant) {
        String req = "UPDATE participant SET name=?, affiliation=?, age=?, teamid=? WHERE participantId=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, participant.getName());
            pst.setString(2, participant.getAffiliation());
            pst.setInt(3, participant.getAge());
            pst.setInt(4, participant.getTeamid());
            pst.setInt(5, participant.getParticipantId());
            pst.executeUpdate();
            System.out.println("Participant modifié");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void supprimer(Participant participant) {
        String req = "DELETE FROM participant WHERE participantId=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, participant.getParticipantId());
            pst.executeUpdate();
            System.out.println("Participant supprimé");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public ArrayList<Participant> rechercher() {
        ArrayList<Participant> participants = new ArrayList<>();
        String req = "SELECT * FROM participant";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                participants.add(new Participant(
                        rs.getInt("participantId"),
                        rs.getString("name"),
                        rs.getString("affiliation"),
                        rs.getInt("age"),
                        rs.getInt("teamid")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return participants;
    }
    public ArrayList<Integer> getAvailableIdss(String table, String column) {
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
    public Participant getParticipantById(int participantId) {
        Participant participant = null;
        String req = "SELECT * FROM participant WHERE participantId = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, participantId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                participant = new Participant(
                        rs.getInt("participantId"),
                        rs.getString("name"),
                        rs.getString("affiliation"),
                        rs.getInt("age"),
                        rs.getInt("teamid")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return participant;
    }
}
