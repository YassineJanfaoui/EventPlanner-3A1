package tn.esprit.entities;
import tn.esprit.entities.*;

public class SelectedUser {

    // Private static instance of the class
    private static SelectedUser instance;

    // Private variable to store the logged-in user
    private User SelectedUser;

    // Private constructor to prevent instantiation from outside
    private SelectedUser() {}

    // Public method to provide access to the instance
    public static SelectedUser getInstance() {
        if (instance == null) {
            instance = new SelectedUser();
        }
        return instance;
    }

    // Method to set the logged-in user
    public void setSelectedUser(User user) {
        this.SelectedUser = user;
    }

    // Method to get the logged-in user
    public User getSelectedUserr() {
        return SelectedUser;
    }

    // Method to clear the logged-in user (e.g., on logout)
    public void clearSelectedUser() {
        this.SelectedUser = null;
    }
}