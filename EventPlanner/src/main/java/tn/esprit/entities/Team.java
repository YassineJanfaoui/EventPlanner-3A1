package tn.esprit.entities;

public class Team {
    private int teamid;
    private String TeamName;
    private int Score;
    private int Rank;
    private int eventId;

    public Team(int teamid, String teamName, int score, int rank,int eventId) {
        this.teamid = teamid;
        TeamName = teamName;
        Score = score;
        Rank = rank;
        this.eventId = eventId;
    }

    public Team(String teamName, int score, int rank, int eventId) {
        TeamName = teamName;
        Score = score;
        Rank = rank;
        this.eventId = eventId;
    }

    public Team() {
    }

    public Team(int teamid, String teamName) {
        this.teamid = teamid;
        TeamName = teamName;
    }

    public int getId() {
        return teamid;
    }

    public void setId(int teamid) {
        this.teamid = teamid;
    }

    public String getTeamName() {
        return TeamName;
    }

    public void setTeamName(String teamName) {
        TeamName = teamName;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int score) {
        Score = score;
    }

    public int getRank() {
        return Rank;
    }

    public void setRank(int rank) {
        Rank = rank;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + teamid +
                ", TeamName='" + TeamName + '\'' +
                ", Score=" + Score +
                ", Rank=" + Rank +
                ", eventId=" + eventId +
                '}';
    }
    public String toStringName(String s){
        return s;
    }

}