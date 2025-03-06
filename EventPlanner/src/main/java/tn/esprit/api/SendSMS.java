package tn.esprit.api;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import tn.esprit.entities.Reservation;

public class SendSMS {

    public static final String ACCOUNT_SID = System.getenv("ACaba77eb7d59bdaa0f2691c38e13bd1a2");
    public static final String AUTH_TOKEN = System.getenv("bd0b061e3a94be97ba7520726806f790\n"
            + "");

    public static void sendSMS(String o) {
        Twilio.init("ACaba77eb7d59bdaa0f2691c38e13bd1a2", "bd0b061e3a94be97ba7520726806f790");
        Message message = Message.creator(new PhoneNumber("+21653989935"),
                new PhoneNumber("+13203732347"), "Nouvel Event! ReservationDate is   :" + o + "est le nouvel prix =" + o).create();

        System.out.println(message.getSid());
    }
}


