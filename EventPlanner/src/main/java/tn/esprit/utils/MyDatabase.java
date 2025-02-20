package tn.esprit.utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {
    private final String URL ="jdbc:mysql://localhost:3306/event_planner";
    private final String USER = "root";
    private final String PASS = "";
    private Connection con;
    public static MyDatabase instance;

    public MyDatabase() {
        try{
            con = DriverManager.getConnection(URL,USER,PASS);
            System.out.println("Connected");
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

    }

    public static MyDatabase getInstance(){
        if(instance == null){
            instance = new MyDatabase();
        }
        return instance;
    }
    public Connection getConnection(){
        return con;
    }
}
