package com.esprit.tests;

import com.esprit.models.Event;
import com.esprit.services.EventService;
import com.esprit.models.Participant;
import com.esprit.services.ParticipantService;
import com.esprit.models.Reservation;
import com.esprit.services.ReservationService;

public class MainProg {
    public static void main(String[] args) {
        EventService eventService = new EventService();
        ParticipantService participantService = new ParticipantService();
        ReservationService reservationService = new ReservationService();

        // Adding a new event with an image path
        eventService.ajouter(new Event("Conference", "2024-02-11 09:00", "2024-02-11 17:00", 14, "Conference Description", 55, 10, "path/to/image.png"));

        // Listing all events
        System.out.println("Events:");
        System.out.println(eventService.rechercher());

        // Adding a new participant
        participantService.ajouter(new Participant("John Doe", "Tech Corp", 1, 1));

        // Listing all participants
        System.out.println("Participants:");
        System.out.println(participantService.rechercher());

        // Adding a new reservation
        reservationService.ajouter(new Reservation(null, 1, "2024-02-11", "100.00"));

        // Modifying an existing reservation (make sure to set the correct eventVenueId)
        // reservationService.modifier(new Reservation(1, null, 1, "2024-02-12", "150.00"));

        // Deleting a reservation (uncomment if needed)
        // reservationService.supprimer(new Reservation(1, null, null, null, null));

        // Listing all reservations
        System.out.println("Reservations:");
        System.out.println(reservationService.rechercher());
    }
}