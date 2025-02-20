package tn.esprit.entities;

public class Admin extends User {
    public Admin(int userId, String username, String password, String email, String name, String phoneNumber, Status status, Role role) {
        super(userId, username, password, email, name, phoneNumber, status, role);
    }

    public Admin() {
    }

    public Admin(String username, String password, String email, String name, String phoneNumber, Status status, Role role) {
        super(username, password, email, name, phoneNumber, status, role);
    }
}