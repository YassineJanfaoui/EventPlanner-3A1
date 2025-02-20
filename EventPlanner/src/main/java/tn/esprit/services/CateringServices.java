package tn.esprit.services;

import tn.esprit.entities.Catering;
import tn.esprit.entities.Venue;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CateringServices implements IService<Catering> {
    private Connection con;
    public CateringServices() throws SQLException {
        con= MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(Catering catering) throws SQLException {
        String query= "INSERT INTO `catering`(`CateringId`, `MenuType`, `NbrPlates`, `Pricing`, `MealSchedule`, `Beverages`, `VenueId`) VALUES ('"+catering.getCateringId()+"','"+catering.getMenuType()+"','"+catering.getNbrPlates()+"','"+catering.getPricing()+"','"+catering.getMealSchedule()+"','"+catering.getBeverages()+"','"+catering.getVenueId()+"')";
        Statement st=con.createStatement();
        st.executeUpdate(query);
        System.out.println("Catering added");
    }

    @Override
    public void addP(Catering c) throws SQLException {
        String query="INSERT INTO catering (MenuType,NbrPlates,Pricing,MealSchedule,Beverages,VenueId) VALUES (?,?,?,?,?,?)";
        System.out.println("Debug: Inside addP() method");
        System.out.println("Debug: Catering object values:");
        System.out.println("MenuType: " + c.getMenuType());
        System.out.println("NbrPlates: " + c.getNbrPlates());
        System.out.println("Pricing: " + c.getPricing());
        System.out.println("MealSchedule: " + c.getMealSchedule());
        System.out.println("Beverages: " + c.getBeverages());
        System.out.println("VenueId: " + c.getVenueId());
        if (c.getMenuType() == null || c.getMenuType().isEmpty()) {
            throw new SQLException("MenuTypeE cannot be NULL.");
        }
        PreparedStatement pst=con.prepareStatement(query);
        //pst.setInt(1, c.getCateringId());
        pst.setString(1, c.getMenuType());
        pst.setInt(2, c.getNbrPlates());
        pst.setDouble(3, c.getPricing());
        pst.setString(4, c.getMealSchedule());
        pst.setString(5, c.getBeverages());
        pst.setInt(6, c.getVenueId());
        pst.executeUpdate();
        System.out.println("Catering added");
    }

    @Override
    public List<Catering> returnList() throws SQLException {
        String query="SELECT * FROM catering";
        Statement st=con.createStatement();
        ResultSet rs=st.executeQuery(query);
        List<Catering> cateringList=new ArrayList<>();
        while(rs.next()){
            Catering c=new Catering();
            c.setCateringId(rs.getInt("CateringId"));
            c.setMenuType(rs.getString("MenuType"));
            c.setNbrPlates(rs.getInt("NbrPlates"));
            c.setPricing(rs.getDouble("Pricing"));
            c.setMealSchedule(rs.getString("MealSchedule"));
            c.setBeverages(rs.getString("Beverages"));
            c.setVenueId(rs.getInt("VenueId"));
            cateringList.add(c);
        }
        return cateringList;
    }

    @Override
    public void delete(Catering catering) {
        String query = "DELETE FROM catering WHERE CateringId=?";
        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, catering.getCateringId());
            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Catering deleted successfully");
            } else {
                System.out.println("No Catering found with the given ID");
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
    }

    @Override
    public void update(Catering c) {
        String query = "UPDATE catering SET MenuType=?, NbrPlates=?, Pricing=?, MealSchedule=?, Beverages=?, VenueId=? WHERE CateringId=?";
        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, c.getMenuType());
            pst.setInt(2, c.getNbrPlates());
            pst.setDouble(3, c.getPricing());
            pst.setString(4, c.getMealSchedule());
            pst.setString(5, c.getBeverages());
            pst.setInt(6, c.getVenueId());

            pst.setInt(7, c.getCateringId());
            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated == 1) {
                System.out.println("Catering updated successfully");
            } else {
                System.out.println("No Catering found with the given ID");
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
    }
}

