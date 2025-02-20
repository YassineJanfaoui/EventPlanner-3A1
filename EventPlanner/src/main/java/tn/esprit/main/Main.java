package tn.esprit.main;

import tn.esprit.entities.Partner;
import tn.esprit.entities.Workshop;
import tn.esprit.services.PartnerServices;
import tn.esprit.services.WorkshopServices;
import tn.esprit.utils.MyDatabase;

import java.sql.SQLException;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        MyDatabase db = MyDatabase.getInstance();

        WorkshopServices ws = new WorkshopServices();

        try {
            System.out.println(ws.returnList());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
