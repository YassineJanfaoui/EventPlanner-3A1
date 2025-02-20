package tn.esprit.entities;

import java.util.Objects;

public class User {
    private int userId;
    private String username;
    private String password;
    private String email;
    private String name;
    private String phoneNumber;
    private Status status; // Changed to enum
    private Role role; // Changed to enum

    public User(int userId, String username, String password, String email, String name, String phoneNumber, Status status, Role role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.role = role;
    }

    public User(String username, String password, String email, String name, String phoneNumber, Status status, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.role = role;
    }

    public User() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return userId == user.userId && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(email, user.email) && Objects.equals(name, user.name) && Objects.equals(phoneNumber, user.phoneNumber) && status == user.status && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, password, email, name, phoneNumber, status, role);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", status=" + status +
                ", role=" + role +
                '}';
    }
}