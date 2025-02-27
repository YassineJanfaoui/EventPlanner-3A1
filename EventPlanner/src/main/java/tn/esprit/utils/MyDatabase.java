package tn.esprit.utils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDataBase {

    private Connection connection;
    private static MyDataBase instance;

    // Database connection dsetails
    private final String URL = "jdbc:mysql://localhost:3306/event_planner";
    private final String USERNAME = "root";
    private final String PASSWORD = "";

    /**
     * Private constructor to prevent instantiation from outside the class.
     * Initializes the database connection.
     */
    private MyDataBase() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connected to the database");
        } catch (SQLException e) {
            System.out.println("Error while connecting to the database: " + e.getMessage());
        }
    }
    public static MyDataBase getInstance() {
        if (instance == null) {
            synchronized (MyDataBase.class) { // Ensure thread safety in multi-threaded environments
                if (instance == null) {
                    instance = new MyDataBase();
                }
            }
        }
        return instance;
    }
    public Connection getConnection() {
        return connection;
    }
}
