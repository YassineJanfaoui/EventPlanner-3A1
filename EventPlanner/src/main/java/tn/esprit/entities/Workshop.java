package tn.esprit.entities;

import java.util.Date;

public class Workshop {
    private int workshopId, duration;
    private String title, description, coach;
    private Date startDate;
    private int  partnerId;


    public Workshop() {
    }

    public Workshop(String title, String coach,int duration, Date startDate, String description, int partnerid) {
        this.duration = duration;
        this.title = title;
        this.description = description;
        this.coach = coach;
        this.startDate = startDate;
        this.partnerId = partnerid;
    }

    public Workshop(int workshopId, String title, String coach, int duration, Date startDate, String description, int partnerid) {
        this.workshopId = workshopId;
        this.duration = duration;
        this.title = title;
        this.description = description;
        this.coach = coach;
        this.startDate = startDate;
        this.partnerId = partnerid;
    }

    public int getWorkshopId() {
        return workshopId;
    }

    public void setWorkshopId(int workshopId) {
        this.workshopId = workshopId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getCoach() {
        return coach;
    }

    public void setCoach(String coach) {
        this.coach = coach;
    }

    public int getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(int partnerid) {
        this.partnerId = partnerid;
    }

    @Override
    public String toString() {
        return "Workshop{" +
                "WorkshopId=" + workshopId +
                ", title='" + title + '\'' +
                ", coach='" + coach + '\'' +
                ", duration=" + duration +
                ", Description='" + description + '\'' +
                ", StartDate='" + startDate + '\'' +
                ", partnerid='" + partnerId + '\'' +
                '}';
    }
}
