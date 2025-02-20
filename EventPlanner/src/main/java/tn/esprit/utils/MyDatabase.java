package tn.esprit.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {

    private final String URL ="jdbc:mysql://localhost:3306/event_planner";
    private final String USERNAME ="root";
    private final String PASSWORD ="";
    private Connection con;
    public static MyDatabase Instance;

    private MyDatabase() {
        try {
        con = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        System.out.println("Connected to database");
    }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static MyDatabase getInstance(){
        if(Instance == null){
            Instance = new MyDatabase();
        }
        return Instance;
    }

    public Connection getCon() {
        return con;
    }
}
