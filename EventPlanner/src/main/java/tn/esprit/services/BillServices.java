package tn.esprit.services;

import tn.esprit.entities.Bill;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillServices implements IService<Bill> {
    private Connection con;

    public BillServices() {
        con = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(Bill b) throws SQLException {
    }

    @Override
    public void addP(Bill b) throws SQLException {
        String query = "INSERT INTO bill (amount, archived, eventid, duedate, description, paymentstatus) VALUES (?,?,?,?,?,?)";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1, b.getAmount());
        pst.setInt(2, b.getArchived());
        pst.setInt(3, b.getEventID());
        pst.setDate(4, new java.sql.Date(b.getDueDate().getTime()));
        pst.setString(5, b.getDescription());
        pst.setString(6, b.getPaymentStatus());

        int addedLines = pst.executeUpdate();
        if (addedLines > 0)
            System.out.println("Bill added successfully");
        else
            System.out.println("Bill not added");
    }

    @Override
    public List<Bill> returnList() throws SQLException {
        String query = "SELECT * FROM bill";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        List<Bill> billList = new ArrayList<>();

        while (rs.next()) {
            Bill b = new Bill();
            b.setBillId(rs.getInt("billid"));
            b.setAmount(rs.getInt("amount"));
            b.setArchived(rs.getInt("archived"));
            b.setEventID(rs.getInt("eventId"));
            b.setDueDate(rs.getDate("duedate"));
            b.setDescription(rs.getString("description"));
            b.setPaymentStatus(rs.getString("paymentstatus"));

            billList.add(b);
        }
        return billList;
    }


    @Override
    public void delete(Bill b) {
        String query = "DELETE FROM bill WHERE billid=?";
        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, b.getBillId());
            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Bill deleted successfully");
            } else {
                System.out.println("No bill found with the given ID");
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
    }

    @Override
    public void update(Bill b) {
        String query = "UPDATE bill SET amount=?, archived=?, eventid=?, duedate=?, description=?, paymentstatus=? WHERE billid=?";
        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, b.getAmount());
            pst.setInt(2, b.getArchived());
            pst.setInt(3, b.getEventID());
            pst.setDate(4, new java.sql.Date(b.getDueDate().getTime()));
            pst.setString(5, b.getDescription());
            pst.setString(6, b.getPaymentStatus());
            pst.setInt(7, b.getBillId());

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated == 1) {
                System.out.println("Bill updated successfully");
            } else {
                System.out.println("No bill found with the given ID");
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
    }
    public int[] eventIDs() throws SQLException {
        String query = "SELECT eventID FROM event";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        List<Integer> eventIDsList = new ArrayList<>();
        while (rs.next()) {
            eventIDsList.add(rs.getInt("eventID"));
        }
        return eventIDsList.stream().mapToInt(i -> i).toArray();
    }
}
