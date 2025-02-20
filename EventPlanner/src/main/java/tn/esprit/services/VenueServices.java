package tn.esprit.services;

import tn.esprit.entities.Venue;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VenueServices implements IService<Venue> {
    private Connection con;
    public VenueServices() throws SQLException {
        con=MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(Venue venue) throws SQLException {
        String query= "INSERT INTO `venue`(`VenueId`, `Location`, `NbrPlaces`, `VenueName`, `Availability`, `Cost`, `Parking`) VALUES ('"+venue.getVenueId()+"','"+venue.getLocation()+"','"+venue.getNbrPlaces()+"','"+venue.getVenueName()+"','"+venue.getAvailability()+"','"+venue.getCost()+"','"+venue.getParking()+"')";
        Statement st=con.createStatement();
        st.executeUpdate(query);
        System.out.println("Venue added");
    }

    @Override
    public void addP(Venue v) throws SQLException {
        System.out.println("Debug: Venue object values before insert:");
        System.out.println("Location: " + v.getLocation());
        System.out.println("NbrPlaces: " + v.getNbrPlaces());
        System.out.println("VenueName: " + v.getVenueName());
        System.out.println("Availability: " + v.getAvailability());
        System.out.println("Cost: " + v.getCost());
        System.out.println("Parking: " + v.getParking());
        if (v.getAvailability() == null || v.getAvailability().isEmpty()) {
            throw new SQLException("Error: Availability cannot be null!");
        }
        String query="INSERT INTO venue (Location,NbrPlaces,VenueName,Availability,Cost,Parking) VALUES (?,?,?,?,?,?)";
        PreparedStatement pst=con.prepareStatement(query);
        pst.setString(1, v.getLocation());
        pst.setInt(2, v.getNbrPlaces());
        pst.setString(3, v.getVenueName());
        pst.setString(4, v.getAvailability());
        pst.setDouble(5, v.getCost());
        pst.setString(6, v.getParking());
        pst.executeUpdate();
        System.out.println("Venue added");
    }

    @Override
    public List<Venue> returnList() throws SQLException {
        String query="SELECT * FROM venue";
        Statement st=con.createStatement();
        ResultSet rs=st.executeQuery(query);
        List<Venue> venueList=new ArrayList<>();
        while(rs.next()){
            Venue v=new Venue();
            v.setVenueId(rs.getInt("VenueId"));
            v.setVenueName(rs.getString("VenueName"));
            v.setLocation(rs.getString("Location"));
            v.setNbrPlaces(rs.getInt("NbrPlaces"));
            v.setAvailability(rs.getString("Availability"));
            v.setCost(rs.getFloat("Cost"));
            v.setParking(rs.getString("Parking"));
            venueList.add(v);
        }
        return venueList;
    }

    @Override
    public void delete(Venue venue) {
        String query = "DELETE FROM venue WHERE VenueId=?";
        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, venue.getVenueId());
            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Venue deleted successfully");
            } else {
                System.out.println("No venue found with the given ID");
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
    }

    @Override
    public void update(Venue v) {
        String query = "UPDATE venue SET Location=?, NbrPlaces=?, VenueName=?, Availability=?, Cost=?, Parking=? WHERE VenueId=?";
        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, v.getLocation());
            pst.setInt(2, v.getNbrPlaces());
            pst.setString(3, v.getVenueName());
            pst.setString(4, v.getAvailability());
            pst.setDouble(5, v.getCost());
            pst.setString(6, v.getParking());
            pst.setInt(7, v.getVenueId());
            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated == 1) {
                System.out.println("Venue updated successfully");
            } else {
                System.out.println("No venue found with the given ID");
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
    }

    public int[] venueIDs() throws SQLException {
        String query = "SELECT VenueId FROM venue";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        List<Integer> venueIDsList = new ArrayList<>();

        while (rs.next()) {
            venueIDsList.add(rs.getInt("VenueId"));
        }

        return venueIDsList.stream().mapToInt(i -> i).toArray();
    }
}
