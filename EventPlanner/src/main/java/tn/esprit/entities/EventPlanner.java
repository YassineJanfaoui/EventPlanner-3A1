package tn.esprit.entities;

public class EventPlanner extends User{
    public EventPlanner(int userId, String username, String password, String email, String name, String phoneNumber, Status status, Role role) {
        super(userId, username, password, email, name, phoneNumber, status, role);
    }

    public EventPlanner(String username, String password, String email, String name, String phoneNumber, Status status, Role role) {
        super(username, password, email, name, phoneNumber, status, role);
    }

    public EventPlanner() {
    }
}
