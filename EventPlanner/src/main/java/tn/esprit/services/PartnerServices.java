package tn.esprit.services;

import tn.esprit.entities.Partner;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PartnerServices implements IService<Partner> {
    private Connection con;

    public PartnerServices() {
        con = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(Partner p) {
    }

    @Override
    public void addP(Partner p) throws SQLException {
        String query = "INSERT INTO partner (`name`, `category`) VALUES (?, ?)";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, p.getName());
        pst.setString(2, p.getCategory());
        pst.executeUpdate();
        System.out.println("Partner added");
    }

    @Override
    public List<Partner> returnList() throws SQLException {
        String query = "SELECT * FROM partner";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        List<Partner> partnerList = new ArrayList<>();
        while (rs.next()) {
            Partner p = new Partner();
            p.setPartnerId(rs.getInt("partnerId"));
            p.setName(rs.getString("name"));
            p.setCategory(rs.getString("category"));
            partnerList.add(p);
        }
        return partnerList;
    }

    @Override
    public void delete(Partner p) {
        String query = "DELETE FROM partner WHERE partnerId=?";
        try {
            int id = p.getPartnerId();
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, id);
            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Partner deleted successfully");
            } else {
                System.out.println("No partner found with the given ID");
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
    }

    @Override
    public void update(Partner p) {
        String query = "UPDATE partner SET name=?, category=? WHERE partnerId=?";
        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, p.getName());
            pst.setString(2, p.getCategory());
            pst.setInt(3, p.getPartnerId());
            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated == 1) {
                System.out.println("Partner updated successfully");
            } else {
                System.out.println("No partner found with the given ID");
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
    }
}
