package tn.esprit.entities;

import javafx.scene.control.Button;
import tn.esprit.utils.SentimentAnalyzer;

public class Feedback {
    private int FeedbackId;
    private String comment;
    private int rate;
    private int teamId,eventId;
    private String sentiment;

    public Feedback() {
    }

    public Feedback(int feedbackId, String comment, int rate, int teamId, int eventId) {
        FeedbackId = feedbackId;
        this.comment = comment;
        this.rate = rate;
        this.teamId = teamId;
        this.eventId = eventId;
        this.sentiment = SentimentAnalyzer.analyze(comment);
    }

    public Feedback(String comment, int rate, int teamId, int eventId) {
        this.comment = comment;
        this.rate = rate;
        this.teamId = teamId;
        this.eventId = eventId;

    }

    @Override
    public String toString() {
        return "Feedback{" +
                "FeedbackId=" + FeedbackId +
                ", comment='" + comment + '\'' +
                ", rate=" + rate +
                ", teamId=" + teamId +
                ", eventId=" + eventId +
                '}';
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getFeedbackId() {
        return FeedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        FeedbackId = feedbackId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }
}
