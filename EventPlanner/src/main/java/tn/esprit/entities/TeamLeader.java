package tn.esprit.entities;

public class TeamLeader extends User{
    public TeamLeader(int userId, String username, String password, String email, String name, String phoneNumber, Status status, Role role) {
        super(userId, username, password, email, name, phoneNumber, status, role);
    }

    public TeamLeader(String username, String password, String email, String name, String phoneNumber, Status status, Role role) {
        super(username, password, email, name, phoneNumber, status, role);
    }

    public TeamLeader() {
    }
}
