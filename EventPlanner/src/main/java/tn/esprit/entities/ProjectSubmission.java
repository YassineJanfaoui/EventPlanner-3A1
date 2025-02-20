package tn.esprit.entities;

import java.util.Date;

public class ProjectSubmission {
    private int submissionId;
    private String Title;
    private String FileLink;
    private Date SubmissionDate;
    private int teamId;

    public ProjectSubmission(int submissionId, String title, String fileLink, Date submissionDate,int teamId) {
        this.submissionId = submissionId;
        Title = title;
        FileLink = fileLink;
        SubmissionDate = submissionDate;
        this.teamId = teamId;
    }

    public ProjectSubmission(String title, String fileLink, Date submissionDate,int teamId) {
        Title = title;
        FileLink = fileLink;
        SubmissionDate = submissionDate;
        this.teamId = teamId;
    }

    public ProjectSubmission() {
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(int submissionId) {
        this.submissionId = submissionId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getFileLink() {
        return FileLink;
    }

    public void setFileLink(String fileLink) {
        FileLink = fileLink;
    }

    public Date getSubmissionDate() {
        return SubmissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        SubmissionDate = submissionDate;
    }

    @Override
    public String toString() {
        return "ProjectSubmission{" +
                "submissionId=" + submissionId +
                ", Title='" + Title + '\'' +
                ", FileLink='" + FileLink + '\'' +
                ", SubmissionDate=" + SubmissionDate +
                ", teamId=" + teamId +
                '}';
    }
}
