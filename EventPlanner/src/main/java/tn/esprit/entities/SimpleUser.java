package tn.esprit.entities;

public class SimpleUser extends User{
    public SimpleUser(int userId, String username, String password, String email, String name, String phoneNumber, Status status, Role role) {
        super(userId, username, password, email, name, phoneNumber, status, role);
    }

    public SimpleUser(String username, String password, String email, String name, String phoneNumber, Status status, Role role) {
        super(username, password, email, name, phoneNumber, status, role);
    }

    public SimpleUser() {
    }
}
