package tn.esprit.services;

import tn.esprit.entities.Partner;
import tn.esprit.entities.Workshop;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorkshopServices implements IService<Workshop> {
    private Connection con;
    public WorkshopServices() {
        con = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(Workshop w)  {
    }

    @Override
    public void addP(Workshop w) throws SQLException {
        String query = "INSERT INTO workshop (`title`, `coach`, `duration`, `startDate`, `description`, `partnerId`) VALUES (?,?,?,?,?,?)";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, w.getTitle());
        pst.setString(2, w.getCoach());
        pst.setInt(3, w.getDuration());
        pst.setDate(4, new java.sql.Date(w.getStartDate().getTime()));
        pst.setString(5, w.getDescription());
        pst.setInt(6, w.getPartnerId());
        pst.executeUpdate();
        System.out.println("Workshop added");
    }


    @Override
    public List<Workshop> returnList() throws SQLException {
        String query = "SELECT * FROM workshop";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        List<Workshop> workshopList = new ArrayList<>();
        while (rs.next()) {
            Workshop w = new Workshop();
            w.setWorkshopId(rs.getInt("workshopId"));
            w.setTitle(rs.getString("title"));
            w.setCoach(rs.getString("coach"));
            w.setDuration(rs.getInt("duration"));
            w.setStartDate(rs.getDate("startDate"));
            w.setDescription(rs.getString("description"));
            w.setPartnerId(rs.getInt("partnerId"));
            workshopList.add(w);
        }
        return workshopList;
    }

    @Override
    public void delete(Workshop w) {
        String query = "DELETE FROM workshop WHERE workshopId=?";
        try {
            int id = w.getWorkshopId();
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, id);
            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Workshop deleted successfully");
            } else {
                System.out.println("No workshop found with the given ID");
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
    }

    @Override
    public void update(Workshop w) {
        String query = "UPDATE workshop SET title=?, coach=?, duration=?, startDate=?, description=?, partnerId=?  WHERE workshopId=?";
        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, w.getTitle());
            pst.setString(2, w.getCoach());
            pst.setInt(3, w.getDuration());
            pst.setDate(4, new java.sql.Date(w.getStartDate().getTime()));
            pst.setString(5, w.getDescription());
            pst.setInt(6,w.getPartnerId());
            pst.setInt(7, w.getWorkshopId());
            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated == 1) {
                System.out.println("Workshop updated successfully");
            } else {
                System.out.println("No workshop found with the given ID");
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
    }
//    public int[] getPartnerIDs() throws SQLException {
//        String query = "SELECT partnerId FROM partner";
//        Statement st = con.createStatement();
//        ResultSet rs = st.executeQuery(query);
//        List<Integer> eventIDsList = new ArrayList<>();
//        while (rs.next()) {
//            eventIDsList.add(rs.getInt("partnerId"));
//        }
//        return eventIDsList.stream().mapToInt(i -> i).toArray();
//    }

    public List<Integer> getPartnerIDs() throws SQLException {
        String query = "SELECT partnerId FROM partner";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        List<Integer> partnerIDsList = new ArrayList<>();
        while (rs.next()) {
            partnerIDsList.add(rs.getInt("partnerId"));
        }
        return partnerIDsList;
    }


}
