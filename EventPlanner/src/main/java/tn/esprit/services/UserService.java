package tn.esprit.services;

import tn.esprit.entities.*;
import tn.esprit.utils.MyDatabase;

import java.sql.Connection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService implements IService<User> {
    Connection con = MyDatabase.getInstance().getConnection();
    @Override
    public void addP(User u) {
        try {
            // Insert into the user table
            String query = "INSERT INTO user(username, password, email, name, phoneNumber, status, role) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getEmail());
            ps.setString(4, u.getName());
            ps.setString(5, u.getPhoneNumber());
            ps.setString(6, u.getStatus().toString());
            ps.setString(7, u.getRole().toString());
            ps.executeUpdate();

            // Retrieve the auto-generated UserId
            ResultSet generatedKeys = ps.getGeneratedKeys();
            int userId = -1;
            if (generatedKeys.next()) {
                userId = generatedKeys.getInt(1); // Get the auto-generated UserId
                u.setUserId(userId); // Set the UserId in the User object
            } else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }

            System.out.println("User created with ID: " + userId);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }


    @Override
    public List<User> returnList() {
        List<User> list = new ArrayList<>();
        try {
            String query = "SELECT * FROM user";
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                // Convert String to Status enum
                Status status = Status.valueOf(resultSet.getString("status"));
                // Convert String to Role enum
                Role role = Role.valueOf(resultSet.getString("role"));

                // Create User object
                User u = new User(
                        resultSet.getInt("UserId"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("email"),
                        resultSet.getString("name"),
                        resultSet.getString("phoneNumber"),
                        status, // Use the Status enum
                        role // Use the Role enum
                );
                list.add(u);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return list;
    }

    @Override
    public void update(User u) {
        String sql = "UPDATE user SET username = ?, password = ?, email = ?, name = ?, phoneNumber = ?, status = ?, role = ? WHERE UserId = ?";

        try {
            // Create a PreparedStatement
            PreparedStatement ps = con.prepareStatement(sql);

            // Set the parameters for the PreparedStatement
            ps.setString(1, u.getUsername());       // Update username
            ps.setString(2, u.getPassword());       // Update password
            ps.setString(3, u.getEmail());          // Update email
            ps.setString(4, u.getName());           // Update name
            ps.setString(5, u.getPhoneNumber());    // Update phoneNumber
            ps.setString(6, u.getStatus().toString()); // Update status (convert enum to string)
            ps.setString(7, u.getRole().toString());   // Update role (convert enum to string)
            ps.setInt(8, u.getUserId());                   // Use userId to identify the user

            // Execute the update query
            int rowsUpdated = ps.executeUpdate();

            // Check if the update was successful
            if (rowsUpdated > 0) {
                System.out.println("******************** MODIFIED **************************************");
            } else {
                System.out.println("No user found with ID: " + u.getUserId());
            }
        } catch (SQLException ex) {
            System.out.println("Error updating user: " + ex.getMessage());
        }
    }

    @Override
    public void delete(User u) {
        String query = "DELETE FROM user WHERE UserId=?";
        try {
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, u.getUserId());
            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("User deleted successfully");
            } else {
                System.out.println("No user found with the given ID");
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
    }

    @Override
    public void add(User user) {

    }


    public User getUserByEmailAndPass(String email, String password) {
        User user = null;

        try {
            String req = "SELECT * FROM user WHERE Email = ? AND Password = ?";
            PreparedStatement psmt = con.prepareStatement(req);
            psmt.setString(1, email);
            psmt.setString(2, password);
            ResultSet rs = psmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUserId(rs.getInt("userId"));
                user.setUsername(rs.getString("Username"));
                user.setPassword(rs.getString("Password"));
                user.setEmail(rs.getString("Email"));
                user.setName(rs.getString("Name"));
                user.setPhoneNumber(rs.getString("PhoneNumber"));

                // Convert String to Status enum
                String statusStr = rs.getString("Status");
                Status status = Status.valueOf(statusStr); // Convert String to Status enum
                user.setStatus(status);

                // Convert String to Role enum
                String roleStr = rs.getString("Role");
                Role role = Role.valueOf(roleStr); // Convert String to Role enum
                user.setRole(role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // Handle case where the database value does not match any enum constant
            e.printStackTrace();
        }

        return user;
    }}
