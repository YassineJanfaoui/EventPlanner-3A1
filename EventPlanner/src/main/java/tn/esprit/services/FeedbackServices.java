package tn.esprit.services;

import tn.esprit.entities.Feedback;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedbackServices implements IService<Feedback> {
    private Connection con;

    public FeedbackServices() {
        con = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(Feedback feedback) throws SQLException {
        String query = "INSERT INTO feedback (comment, rate,sentiment, teamId, eventId) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, feedback.getComment());
            ps.setInt(2, feedback.getRate());
            ps.setString(3, feedback.getSentiment());
            ps.setInt(4, feedback.getTeamId());
            ps.setInt(5, feedback.getEventId());

            int addedLines = ps.executeUpdate();
            if (addedLines > 0) {
                System.out.println("Feedback added successfully");
            } else {
                System.out.println("Feedback not added");
            }
        }
    }

    public void addP(Feedback feedback) throws SQLException {
        String query = "INSERT INTO feedback (comment, rate, teamId, eventId) VALUES (?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, feedback.getComment());
            ps.setInt(2, feedback.getRate());
            ps.setInt(3, feedback.getTeamId());
            ps.setInt(4, feedback.getEventId());

            int addedLines = ps.executeUpdate();
            if (addedLines > 0) {
                System.out.println("Feedback added successfully");
            } else {
                System.out.println("Feedback not added");
            }
        }
    }

    @Override
    public List<Feedback> returnList() throws SQLException {
        List<Feedback> feedbacks = new ArrayList<>();
        String query = "SELECT * FROM feedback";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Feedback f = new Feedback();
                f.setFeedbackId(rs.getInt("feedbackId"));
                f.setComment(rs.getString("comment"));
                f.setRate(rs.getInt("rate"));
                f.setTeamId(rs.getInt("teamId"));
                f.setEventId(rs.getInt("eventId"));

                feedbacks.add(f);
            }
        }
        return feedbacks;
    }

    @Override
    public void delete(Feedback feedback) {
        String query = "DELETE FROM feedback WHERE feedbackId = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, feedback.getFeedbackId());
            int rowsDeleted = ps.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Feedback deleted successfully");
            } else {
                System.out.println("No feedback found with the given ID");
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
    }

    @Override
    public void update(Feedback feedback) {
        String query = "UPDATE feedback SET comment = ?, rate = ?, teamId = ?, eventId = ? WHERE feedbackId = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, feedback.getComment());
            ps.setInt(2, feedback.getRate());
            ps.setInt(3, feedback.getTeamId());
            ps.setInt(4, feedback.getEventId());
            ps.setInt(5, feedback.getFeedbackId());

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated == 1) {
                System.out.println("Feedback updated successfully");
            } else {
                System.out.println("No feedback found with the given ID");
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
    }

    public Map<String, Integer> teamNames() throws SQLException {
        Map<String, Integer> teamNames = new HashMap<>();
        String query = "SELECT teamName, teamId FROM team";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                teamNames.put(rs.getString("teamName"), rs.getInt("teamId"));
            }
        }
        return teamNames;
    }


    public Map<String, Integer> eventNames() throws SQLException {
        Map<String, Integer> eventNames = new HashMap<>();
        String query = "SELECT name, eventId FROM event";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                eventNames.put(rs.getString("name"), rs.getInt("eventId"));
            }
        }
        return eventNames;
    }



}