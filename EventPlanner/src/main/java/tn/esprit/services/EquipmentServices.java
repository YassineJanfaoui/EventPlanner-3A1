package tn.esprit.services;

import tn.esprit.entities.Equipment;
import tn.esprit.utils.MyDatabase;
import tn.esprit.utils.Arduino;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EquipmentServices implements IService<Equipment> {
    private Connection con;
    private Arduino arduino;
    public EquipmentServices() {

        con=MyDatabase.getInstance().getConnection();
        arduino=Arduino.getInstance();
    }
    @Override
    public void add(Equipment e) throws SQLException {

    }

    @Override
    public void addP(Equipment e) throws SQLException {
        String query = "INSERT INTO equipment (name, state, category, quantity) VALUES (?,?,?,?)";

        PreparedStatement pst=con.prepareStatement(query);
        pst.setString(1, e.getName());
        pst.setString(2, e.getState());
        pst.setString(3, e.getCategory());
        pst.setInt(4, e.getQuantity());
        int addedLines=pst.executeUpdate();
        if(addedLines>0)
            System.out.println("Equipment added");
        else
            System.out.println("Equipment not added");

    }

    @Override
    public List<Equipment> returnList() throws SQLException {
        String query="SELECT * FROM equipment";
        Statement st=con.createStatement();
        ResultSet rs=st.executeQuery(query);
        List<Equipment> equipmentList=new ArrayList<>();
        while(rs.next()){
            Equipment e=new Equipment();
            e.setEquipmentId(rs.getInt("Equipmentid"));
            e.setName(rs.getString("name"));
            e.setState(rs.getString("state"));
            e.setCategory(rs.getString("category"));
            e.setQuantity(rs.getInt("quantity"));
            equipmentList.add(e);
        }
        return equipmentList;
    }

    @Override
    public void delete(Equipment e){
        String query = "DELETE FROM equipment WHERE equipmentid=?";
        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, e.getEquipmentId());
            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Equipment deleted successfully");
            } else {
                System.out.println("No equipment found with the given ID");
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }

        if (arduino.isConnected()) {
            arduino.sendData("9");
        }
    }

    @Override
    public void update(Equipment e){
        String query = "UPDATE equipment SET name=?, state=?, category=?, quantity=? WHERE equipmentid=?";
        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, e.getName());
            pst.setString(2, e.getState());
            pst.setString(3, e.getCategory());
            pst.setInt(4, e.getQuantity());
            pst.setInt(5, e.getEquipmentId());
            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated == 1) {
                System.out.println("Equipment updated successfully");
                System.out.println(e.getState());
                if (Objects.equals(e.getState(), "Maintenance") || Objects.equals(e.getState(), "maintenance")) {
                    System.out.println(e.getState());
                    arduino.sendData("9");
                }
            } else {
                System.out.println("No equipment found with the given ID");
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }


    }
    public List<Equipment> searchEquipmentByName(String query) throws SQLException {
        String sql = "SELECT * FROM equipment WHERE name LIKE ?";
        PreparedStatement statement = con.prepareStatement(sql);
        statement.setString(1, "%" + query + "%");  // Use wildcards to allow partial matching
        ResultSet resultSet = statement.executeQuery();

        List<Equipment> equipmentList = new ArrayList<>();
        while (resultSet.next()) {
            Equipment e = new Equipment();
            e.setEquipmentId(resultSet.getInt("Equipmentid"));
            e.setName(resultSet.getString("name"));
            e.setState(resultSet.getString("state"));
            e.setCategory(resultSet.getString("category"));
            e.setQuantity(resultSet.getInt("quantity"));
            equipmentList.add(e);
        }
        return equipmentList;
    }

}
