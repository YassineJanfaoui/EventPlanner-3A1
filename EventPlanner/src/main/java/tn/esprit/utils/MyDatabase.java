package tn.esprit.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {
    private final String URL = "jdbc:mysql://localhost:3306/test";
    private final String username = "root";
    private final String pwd = "";
    private Connection conn;
    private static MyDatabase instance;
    public static MyDatabase getInstance() {
        if (instance == null) {
            instance = new MyDatabase();
        }
        return instance;
    }

    public Connection getConnection() {
        return conn;
    }
    public MyDatabase() {
        try {
            conn = DriverManager.getConnection(URL, username, pwd);
            System.out.println("connected");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
















    public Connection getConn() {
        return conn;
    }


}
