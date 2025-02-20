package tn.esprit.services;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import tn.esprit.entities.ProjectSubmission;
import tn.esprit.utils.MyDatabase;

public class ProjectSubmissionServices implements IService<ProjectSubmission> {

    private Connection con;

    public ProjectSubmissionServices() {
        con = MyDatabase.getInstance().getConn();
    }

    @Override
    public void add(ProjectSubmission submission) throws SQLException {
        String query = "INSERT INTO projectsubmission (title, FileLink, SubmissionDate,teamId) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, submission.getTitle());
        ps.setString(2, submission.getFileLink());
        ps.setDate(3, new java.sql.Date(submission.getSubmissionDate().getTime()));
        ps.setInt(4, submission.getTeamId());
        ps.executeUpdate();
        System.out.println("Submission added");
    }

    @Override
    public void update(ProjectSubmission submission) throws SQLException {
        String query = "UPDATE projectsubmission SET title = ?, FileLink = ?, SubmissionDate = ?, teamId = ? WHERE submissionId = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, submission.getTitle());
        ps.setString(2, submission.getFileLink());
        ps.setDate(3, new java.sql.Date(submission.getSubmissionDate().getTime()));
        ps.setInt(4, submission.getTeamId());
        ps.setInt(5, submission.getSubmissionId());
        int rows = ps.executeUpdate();
        System.out.println("Rows updated: " + rows);
    }


    @Override
    public void delete(ProjectSubmission submission) throws SQLException {
        delete(submission.getSubmissionId());
    }

    @Override
    public void delete(int submissionId) throws SQLException {
        String query = "DELETE FROM projectsubmission WHERE submissionId = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, submissionId);
            int rowsDeleted = ps.executeUpdate();
        }
    }

    @Override
    public List<ProjectSubmission> getAll() throws SQLException {
        String query = "SELECT * FROM projectsubmission";
        List<ProjectSubmission> submissions = new ArrayList<>();
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                submissions.add(new ProjectSubmission(
                        rs.getInt("submissionId"),
                        rs.getString("title"),
                        rs.getString("FileLink"),
                        rs.getDate("SubmissionDate"),
                        rs.getInt("teamId")
                ));
            }
        }
        return submissions;
    }
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


}