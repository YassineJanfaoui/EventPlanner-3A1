package tn.esprit.entities;

public class Venue {
    //private int VenueId,NbrPlaces;
    //private Float Cost;
    //private String VenueName,Location,Availability,Parking;
    private int VenueId;
    private String Location;
    private int NbrPlaces;
    private String VenueName;
    private String Availability;
    private Float Cost;
    private String Parking;


    public Venue(int VenueId, String Location, int NbrPlaces, String VenueName, String Availability, Float Cost, String Parking) {
        this.VenueId = VenueId;
        this.Location = Location;
        this.NbrPlaces = NbrPlaces;
        this.VenueName = VenueName;
        this.Availability = Availability;
        this.Cost = Cost;
        this.Parking = Parking;
    }

    public Venue() {
    }

    public int getVenueId() {
        return VenueId;
    }

    public int getNbrPlaces() {
        return NbrPlaces;
    }

    public String getVenueName() {
        return VenueName;
    }

    public String getLocation() {
        return Location;
    }

    public float getCost() {
        return Cost;
    }

    public void setCost(float cost) {
        Cost = cost;
    }

    public String getAvailability() {
        return Availability;
    }

    public void setAvailability(String availability) {
        Availability = availability;
    }

    public String getParking() {
        return Parking;
    }

    public void setParking(String parking) {
        Parking = parking;
    }

    public void setVenueId(int venueId) {
        VenueId = venueId;
    }

    public void setNbrPlaces(int nbrPlaces) {
        NbrPlaces = nbrPlaces;
    }

    public void setVenueName(String venueName) {
        VenueName = venueName;
    }

    public void setLocation(String location) {
        Location = location;
    }


    @Override
    public String toString() {
        return "Venue{" +
                "VenueId=" + VenueId +
                ", VenueName='" + VenueName + '\'' +
                ", NbrPlaces=" + NbrPlaces +
                ", Location='" + Location + '\'' +
                ", Availability='" + Availability + '\'' +
                ", Cost=" + Cost +
                ", Parking='" + Parking + '\'' +
                '}';
    }
}
