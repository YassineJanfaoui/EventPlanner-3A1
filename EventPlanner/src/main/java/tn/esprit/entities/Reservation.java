package tn.esprit.entities;

import java.util.Objects;

public class Reservation {
    private Integer eventVenueId; // Primary Key
    private Integer venueId; // Foreign Key, can be null
    private Integer eventId; // Foreign Key, can be null
    private String reservationDate; // Date of reservation
    private String reservationPrice; // Price of the reservation

    // Constructors
    public Reservation(Integer eventVenueId, Integer venueId, Integer eventId, String reservationDate, String reservationPrice) {
        this.eventVenueId = eventVenueId;
        this.venueId = venueId;
        this.eventId = eventId;
        this.reservationDate = reservationDate;
        this.reservationPrice = reservationPrice;
    }

    public Reservation(Integer venueId, Integer eventId, String reservationDate, String reservationPrice) {
        this.venueId = venueId;
        this.eventId = eventId;
        this.reservationDate = reservationDate;
        this.reservationPrice = reservationPrice;
    }

    public Integer getEventVenueId() {
        return eventVenueId;
    }

    public void setEventVenueId(Integer eventVenueId) {
        this.eventVenueId = eventVenueId;
    }

    public Integer getVenueId() {
        return venueId;
    }

    public void setVenueId(Integer venueId) {
        this.venueId = venueId;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getReservationPrice() {
        return reservationPrice;
    }

    public void setReservationPrice(String reservationPrice) {
        this.reservationPrice = reservationPrice;
    }

    // Override toString
    @Override
    public String toString() {
        return "Reservation{" +
                "eventVenueId=" + eventVenueId +
                ", venueId=" + venueId +
                ", eventId=" + eventId +
                ", reservationDate='" + reservationDate + '\'' +
                ", reservationPrice='" + reservationPrice + '\'' +
                '}';
    }

    // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return eventVenueId == that.eventVenueId &&
                Objects.equals(venueId, that.venueId) &&
                Objects.equals(eventId, that.eventId) &&
                Objects.equals(reservationDate, that.reservationDate) &&
                Objects.equals(reservationPrice, that.reservationPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventVenueId, venueId, eventId, reservationDate, reservationPrice);
    }
}