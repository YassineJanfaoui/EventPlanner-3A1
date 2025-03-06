package tn.esprit.services;


import java.sql.*;

import tn.esprit.entities.Team;
import tn.esprit.utils.MyDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamServices implements IService<Team> {

    private Connection con;

    public TeamServices() {
        con = MyDatabase.getInstance().getConnection();
    }

    public TeamServices(int id, String teamName, Integer eventId) {
    }

    @Override
    public void add(Team team) throws SQLException {
        String query = "INSERT INTO team (TeamName, Score, Rank, eventId) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, team.getTeamName());
            ps.setInt(2, team.getScore());
            ps.setInt(3, team.getRank());
            ps.setInt(4, team.getEventId());
            ps.executeUpdate();
            System.out.println("Team added");
        } catch (SQLException e) {
            if (e.getSQLState().equals("23000")) { // Foreign key error code
                throw new SQLException("Event does not exist.", e);
            } else {
                throw e;
            }
        }
    }

    @Override
    public void addP(Team team) throws SQLException {

    }


    @Override
    public void update(Team team)  {

        try {
            String query = "UPDATE team SET TeamName = ?, Score = ?, Rank = ? WHERE teamid = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, team.getTeamName());
            ps.setInt(2,team.getScore() );
            ps.setInt(3,team.getRank() );
            ps.setInt(4,team.getId() );
            int rows =ps.executeUpdate();
            System.out.println("Rows updated: " + rows);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete(Team team) {
        String query = "DELETE FROM team WHERE teamid=?";
        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, team.getId());
            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("team deleted successfully");
            } else {
                System.out.println("No team found with the given ID");
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
    }


    @Override
    public List<Team> returnList() throws SQLException {
        String query = "SELECT * FROM team";
        Statement stmt = con.createStatement();
        List<Team> teams = new ArrayList<>();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            Team t = new Team(rs.getInt("teamid"),rs.getString("TeamName"), rs.getInt("Score"), rs.getInt("Rank"),rs.getInt("eventId"));
            teams.add(t);
        }
        return teams;
    }
    public List<Integer> getEventIds() throws SQLException {
        List<Integer> eventIds = new ArrayList<>();
        String query = "SELECT DISTINCT eventId FROM event"; // Ensure the table name is correct
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                eventIds.add(rs.getInt("eventId"));
            }
        }
        return eventIds;
    }

    // In TeamServices.java
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
    public List<Team> getLeaderboard() throws SQLException {
        List<Team> teams = new ArrayList<>();
        String query = "SELECT * FROM team ORDER BY Score DESC";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                teams.add(new Team(
                        rs.getInt("teamid"),
                        rs.getString("TeamName"),
                        rs.getInt("Score"),
                        rs.getInt("Rank"),
                        rs.getInt("eventId")
                ));
            }
        }
        return teams;
    }



}

