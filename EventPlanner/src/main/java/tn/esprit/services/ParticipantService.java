package tn.esprit.services;

import tn.esprit.entities.*;
import tn.esprit.utils.MyDataBase;

import java.sql.Connection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParticipantService implements IService<Participant> {
    Connection con = MyDataBase.getInstance().getConnection();

    @Override
    public void addp(Participant participant) {
        // Implementation if needed
    }

    @Override
    public List<Participant> returnList() {
        List<Participant> list = new ArrayList<>();
        try {
            String query = "SELECT p.participantId, p.name, p.affiliation, p.age, t.teamid, t.TeamName " +
                    "FROM participant p " +
                    "INNER JOIN team t ON p.teamid = t.teamid";
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Participant p = new Participant(
                        resultSet.getInt("participantId"),
                        resultSet.getString("name"),
                        resultSet.getString("affiliation"),
                        resultSet.getInt("age"),
                        resultSet.getInt("teamid")
                );
                list.add(p);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return list;
    }

    @Override
    public void update(Participant participant) {
        String query = "UPDATE participant SET name=?, affiliation=?, age=?, teamid=? WHERE participantId=?";
        try {
            PreparedStatement pst = con.prepareStatement(query);
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
    public void delete(Participant participant) {
        String query = "DELETE FROM participant WHERE participantId=?";
        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, participant.getParticipantId());
            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("participant deleted successfully");
            } else {
                System.out.println("No participant found with the given ID");
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
    }

    @Override
    public void add(Participant participant) {
        String query = "INSERT INTO participant (name, affiliation, age, teamid) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, participant.getName());
            pst.setString(2, participant.getAffiliation());
            pst.setInt(3, participant.getAge());
            pst.setInt(4, participant.getTeamid());
            pst.executeUpdate();
            System.out.println("Participant added");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Team> getTeams() {
        List<Team> teams = new ArrayList<>();
        try {
            String query = "SELECT teamid, TeamName FROM team";
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Team team = new Team(
                        resultSet.getInt("teamid"),
                        resultSet.getString("TeamName")
                );
                teams.add(team);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return teams;
    }
    public Team getTeam(int teamid) {
        Team team = null;
        try {
            String req = "SELECT * FROM team WHERE teamid = ? ";
        PreparedStatement psmt = con.prepareStatement(req);
        psmt.setInt(1, teamid);
        ResultSet rs = psmt.executeQuery();

        if (rs.next()) {
            team = new Team(rs.getInt("teamid"), rs.getString("TeamName"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } catch (IllegalArgumentException e) {
        // Handle case where the database value does not match any enum constant
        e.printStackTrace();
    }

        return team;
    }
    public String getTeamNameById(int teamId) {
        String teamName = "Unknown"; // Default value if team is not found
        try {
            String query = "SELECT TeamName FROM team WHERE teamid = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, teamId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                teamName = rs.getString("TeamName");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return teamName;
    }
}