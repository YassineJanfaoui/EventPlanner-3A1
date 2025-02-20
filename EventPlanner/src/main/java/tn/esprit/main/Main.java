package tn.esprit.main;

import tn.esprit.utils.MyDatabase;

import java.util.List;

import tn.esprit.entities.*;
import tn.esprit.services.*;

public class Main {
    public static void main(String[] args) {
        MyDatabase db = MyDatabase.getInstance();
        UserService userService=new UserService();
        /*User admin = new Admin(
                "adminUser",
                "adminPass",
                "admin@example.com",
                "Admin Name",
                "123456789",
                Status.ACTIVE,
                Role.ADMIN
        );

        User teamLeader = new TeamLeader(
                "teamLeaderUser",
                "teamLeaderPass",
                "teamleader@example.com",
                "Team Leader Name",
                "987654321",
                Status.ACTIVE,
                Role.TEAM_LEADER
        );
        User eventPlanner = new EventPlanner(
                "eventPlannerUser",
                "eventPlannerPass",
                "eventplanner@example.com",
                "Event Planner Name",
                "555555555",
                Status.ACTIVE,
                Role.EVENT_PLANNER
        );
        User simpleUser = new SimpleUser(
                "simpleUser",
                "simplePass",
                "simpleuser@example.com",
                "Simple User Name",
                "111111111",
                Status.ACTIVE,
                Role.SIMPLE_USER
        );
        userService.create(admin);
        userService.create(teamLeader);
        userService.create(eventPlanner);
        userService.create(simpleUser);*/
        User simpleUser = new SimpleUser(
                "simpleUser",
                "simplePass",
                "simpleuser@example.com",
                "Simple User Name",
                "111111111",
                Status.ACTIVE,
                Role.SIMPLE_USER
        );
        List<User> userList=userService.returnList();
        if (userList.isEmpty()) {
            System.out.println("No users found in the database.");
        } else {
            System.out.println("List of Users:");
            for (User user : userList) {
                System.out.println(user);
            }
        }
        User admin = new Admin(
                "admin2222User",
                "adminPass",
                "admin2@example.com",
                "Admin Name",
                "123456789",
                Status.ACTIVE,
                Role.ADMIN
        );
        userService.update(admin);
        userList=userService.returnList();
        if (userList.isEmpty()) {
            System.out.println("No users found in the database.");
        } else {
            System.out.println("List of Users:");
            for (User user : userList) {
                System.out.println(user);
            }
        }
        /*userService.delete(admin);
        userList=userService.returnList();
        if (userList.isEmpty()) {
            System.out.println("No users found in the database.");
        } else {
            System.out.println("List of Users:");
            for (User user : userList) {
                System.out.println(user);
            }
        }*/
    }
}
