package tn.esprit.entities;

import java.util.Objects;

public class Event {
    private int eventId;
    private String name;
    private String startDate;
    private String endDate;
    private int maxParticipants;
    private String description;
    private int fee;
    private int userId; // Foreign key
    private String image; // New field for image

    // Constructors
    public Event(int eventId, String name, String startDate, String endDate, int maxParticipants, String description, int fee, int userId, String image) {
        this.eventId = eventId;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxParticipants = maxParticipants;
        this.description = description;
        this.fee = fee;
        this.userId = userId; // Initialize userId
        this.image = image; // Initialize image
    }

    public Event(String name, String startDate, String endDate, int maxParticipants, String description, int fee, int userId, String image) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxParticipants = maxParticipants;
        this.description = description;
        this.fee = fee;
        this.userId = userId; // Initialize userId
        this.image = image; // Initialize image
    }

    // Getters and Setters
    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public int getUserId() { // Getter for userId
        return userId;
    }

    public void setUserId(int userId) { // Setter for userId
        this.userId = userId;
    }

    public String getImage() { // New getter for image
        return image;
    }

    public void setImage(String image) { // New setter for image
        this.image = image;
    }

    // Override toString
    @Override
    public String toString() {
        return "Event{" +
                "eventId=" + eventId +
                ", name='" + name + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", maxParticipants=" + maxParticipants +
                ", description='" + description + '\'' +
                ", fee=" + fee +
                ", userId=" + userId + // Include userId in toString
                ", image='" + image + '\'' + // Include image in toString
                '}';
    }

    // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return eventId == event.eventId &&
                maxParticipants == event.maxParticipants &&
                fee == event.fee &&
                userId == event.userId &&
                Objects.equals(name, event.name) &&
                Objects.equals(startDate, event.startDate) &&
                Objects.equals(endDate, event.endDate) &&
                Objects.equals(description, event.description) &&
                Objects.equals(image, event.image); // Include image in equals
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, name, startDate, endDate, maxParticipants, description, fee, userId, image); // Include image in hashCode
    }
}