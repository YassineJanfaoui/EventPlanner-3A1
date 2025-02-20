package com.esprit.models;

import java.util.Objects;

public class Participant {
    private int participantId;
    private String name;
    private String affiliation;
    private int age;
    private int teamid; // Foreign key reference to Event

    // Constructors
    public Participant(int participantId, String name, String affiliation, int age, int eventId) {
        this.participantId = participantId;
        this.name = name;
        this.affiliation = affiliation;
        this.age = age;
        this.teamid = eventId;
    }

    public Participant(String name, String affiliation, int age, int eventId) {
        this.name = name;
        this.affiliation = affiliation;
        this.age = age;
        this.teamid = eventId;
    }

    // Getters and Setters
    public int getParticipantId() {
        return participantId;
    }

    public void setParticipantId(int participantId) {
        this.participantId = participantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getTeamid() {
        return teamid;
    }

    public void setTeamid(int teamid) {
        this.teamid = teamid;
    }

    @Override
    public String toString() {
        return "Participant{" +
                "participantId=" + participantId +
                ", name='" + name + '\'' +
                ", affiliation='" + affiliation + '\'' +
                ", age=" + age +
                ", teamid=" + teamid +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return participantId == that.participantId && age == that.age && teamid == that.teamid && Objects.equals(name, that.name) && Objects.equals(affiliation, that.affiliation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(participantId, name, affiliation, age, teamid);
    }
}
