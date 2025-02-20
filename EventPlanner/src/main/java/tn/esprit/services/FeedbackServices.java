package tn.esprit.services;

import tn.esprit.entities.Feedback;
import tn.esprit.entities.Team;
import tn.esprit.utils.MyDatabase;
import tn.esprit.services.IService;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeedbackServices implements IService<Feedback> {
    private Connection con;
    public FeedbackServices() {
        con = MyDatabase.getInstance().getConnection();
    }
    @Override
    public void add(Feedback feedback) throws SQLException {
        String query = "INSERT INTO `feedback`(`comment`, `rate`, `teamId`, `eventId`) VALUES " +
                "(?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1,feedback.getComment() );
        ps.setInt(2, feedback.getRate());
        ps.setInt(3,feedback.getTeamId());
        ps.setInt(4,feedback.getEventId());
        ps.executeUpdate();
        System.out.println("feedback added");
    }

    @Override
    public void addP(Feedback feedback) throws SQLException {

    }

    @Override
    public void update(Feedback feedback)  {
        String query = "UPDATE feedback SET comment = ?, rate = ?, teamId = ?, eventId = ? WHERE feedbackId = ?";
        PreparedStatement ps = null;
        try {

            ps = con.prepareStatement(query);
            ps.setString(1, feedback.getComment());
            ps.setInt(2, feedback.getRate());
            ps.setInt(3, feedback.getTeamId());
            ps.setInt(4, feedback.getEventId());
            ps.setInt(5, feedback.getFeedbackId());  // Set the feedbackId for the WHERE clause

            // Execute the update
            int rows = ps.executeUpdate();
            System.out.println("Rows updated: " + rows);  // Log how many rows were updated
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Set the new values for the feedback object

    }




    @Override
    public void delete(Feedback feedbackid)  {
        int a = feedbackid.getFeedbackId();
        String query = "DELETE FROM feedback WHERE feedbackId = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, a);
            int rowsDeleted = ps.executeUpdate();} catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Feedback> returnList() throws SQLException{

        String query = "SELECT * FROM feedback";
        Statement stmt = con.createStatement();
        List<Feedback> feedbacks = new ArrayList<>();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            Feedback f = new Feedback(rs.getInt("feedbackId")
                    ,rs.getString("comment"), rs.getInt("rate")
                    ,rs.getInt("teamId"), rs.getInt("eventId"));
            feedbacks.add(f);
        }
        return feedbacks;
    }
    public List<Feedback> getAll() throws SQLException {
        String query = "SELECT * FROM feedback";
        Statement stmt = con.createStatement();
        List<Feedback> feedbacks = new ArrayList<>();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            Feedback f = new Feedback(rs.getInt("feedbackId")
                    ,rs.getString("comment"), rs.getInt("rate")
                    ,rs.getInt("teamId"), rs.getInt("eventId"));
            feedbacks.add(f);
        }
        return feedbacks;
    }
    // Add these methods to get available team and event IDs
    public List<Integer> getTeamIds() throws SQLException {
        List<Integer> teamIds = new ArrayList<>();
        String query = "SELECT DISTINCT teamId FROM team"; // Fetch from the 'team' table
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                teamIds.add(rs.getInt("teamId"));
            }
        }
        return teamIds;
    }


    public List<Integer> getEventIds() throws SQLException {
        List<Integer> eventIds = new ArrayList<>();
        String query = "SELECT eventId FROM event"; // Corrected table name and column
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            eventIds.add(rs.getInt("eventId"));
        }
        return eventIds;
    }

}
