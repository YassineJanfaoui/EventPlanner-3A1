package tn.esprit.services;


import java.sql.*;

import tn.esprit.entities.Team;
import tn.esprit.utils.MyDatabase;

import java.util.ArrayList;
import java.util.List;

public class TeamServices implements IService<Team> {

    private Connection con;

    public TeamServices() {
        con = MyDatabase.getInstance().getConn();
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
    public void update(Team team) throws SQLException {
        String query = "UPDATE team SET TeamName = ?, Score = ?, Rank = ? WHERE teamid = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, team.getTeamName());
        ps.setInt(2,team.getScore() );
        ps.setInt(3,team.getRank() );
        ps.setInt(4,team.getId() );
        int rows =ps.executeUpdate();
        System.out.println("Rows updated: " + rows);
    }

    @Override
    public void delete(Team team) throws SQLException {

    }

    @Override
    public void delete(int teamId) throws SQLException {
        String query = "DELETE FROM team WHERE teamid = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, teamId);
            int rowsDeleted = ps.executeUpdate();}
    }

    @Override
    public List<Team> getAll() throws SQLException {
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




}

