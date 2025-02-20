package tn.esprit.main;
import tn.esprit.entities.Feedback;
import tn.esprit.entities.ProjectSubmission;
import tn.esprit.entities.Team;
import tn.esprit.services.FeedbackServices;
import tn.esprit.services.ProjectSubmissionServices;
import tn.esprit.services.TeamServices;
import tn.esprit.utils.MyDatabase;
import tn.esprit.utils.MyDatabase;

import java.sql.SQLException;
import java.util.Date;

public class Main {

    public static void main(String[] args) {
        MyDatabase db = MyDatabase.getInstance();
        TeamServices teamServices = new TeamServices();
        Team team = new Team("G2",9,3,3);
        FeedbackServices fs = new FeedbackServices();
        Feedback f = new Feedback("this is the best",10,2,1);
        Feedback f1 = new Feedback("most fun i ever had",10,1,2);
        ProjectSubmissionServices service = new ProjectSubmissionServices();
        Date today = new Date();
        ProjectSubmission sub = new ProjectSubmission("algo", "https://github.com/algo", today,2);
//          try {
//            teamServices.add(team);
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        try {
//            teamServices.update(team2);
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        try {
//            teamServices.delete(5);
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        } */
//        try {
//            System.out.println(teamServices.getAll());
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//       try {
//            fs.add(f);
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//       try {
//            fs.update(f1);
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        try {
//            fs.delete(1);
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        try {
//            System.out.println(fs.getAll());
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        try {
//            service.add(sub);
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//     try {
//            service.update(sub);
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        try {
//            service.delete(1);
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        try {
//            System.out.println(service.getAll());
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        try {
//            teamServices.add(team);
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        try {
//            System.out.println(fs.getAll());
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        System.out.println("Using eventId: " + team.getEventId());
        try {
           System.out.println(fs.getAll());
       } catch (SQLException e) {
           System.out.println(e.getMessage());
      }
    }

}
