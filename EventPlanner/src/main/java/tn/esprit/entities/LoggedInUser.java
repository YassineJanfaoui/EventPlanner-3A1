package tn.esprit.entities;
import tn.esprit.entities.*;

public class LoggedInUser {

    // Private static instance of the class
    private static LoggedInUser instance;

    // Private variable to store the logged-in user
    private User loggedInUser;

    // Private constructor to prevent instantiation from outside
    private LoggedInUser() {}

    // Public method to provide access to the instance
    public static LoggedInUser getInstance() {
        if (instance == null) {
            instance = new LoggedInUser();
        }
        return instance;
    }

    // Method to set the logged-in user
    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }

    // Method to get the logged-in user
    public User getLoggedInUser() {
        return loggedInUser;
    }

    // Method to clear the logged-in user (e.g., on logout)
    public void clearLoggedInUser() {
        this.loggedInUser = null;
    }
}