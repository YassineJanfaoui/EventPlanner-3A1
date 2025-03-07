package tn.esprit.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import java.io.*;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class GoogleCalendarService {
    private static final String APPLICATION_NAME = "EventPlanner";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Arrays.asList(
            "https://www.googleapis.com/auth/calendar",
            "https://www.googleapis.com/auth/calendar.events");
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    public static Calendar getCalendarService() throws IOException, GeneralSecurityException {
        final HttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        return new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private static Credential getCredentials(final HttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream in = GoogleCalendarService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .setApprovalPrompt("force")
                .build();

        // 🚨 FORCE RE-AUTHENTICATION 🚨
        Credential credential = flow.loadCredential("user");
        if (credential == null || credential.getAccessToken() == null) {
            System.out.println("⚠ No valid credentials found. Please authenticate.");
            LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
            credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
            System.out.println("✅ Successfully authenticated.");
        }

        return credential;
    }

    public static Event createEvent(String venueName, String location, String reservationDate) {
        int retryCount = 3; // Number of retries
        while (retryCount > 0) {
            try {
                Calendar service = getCalendarService();
                Event event = new Event()
                        .setSummary("Event at " + venueName)
                        .setLocation(location)
                        .setDescription("Reservation for this venue.");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate localDate = LocalDate.parse(reservationDate, formatter);
                com.google.api.client.util.DateTime dateTime = new com.google.api.client.util.DateTime(localDate.toString() + "T10:00:00.000Z");

                EventDateTime start = new EventDateTime().setDateTime(dateTime).setTimeZone("UTC");
                EventDateTime end = new EventDateTime().setDateTime(dateTime).setTimeZone("UTC");
                event.setStart(start);
                event.setEnd(end);

                System.out.println("Creating event with title: " + event.getSummary());
                System.out.println("Start time: " + event.getStart().getDateTime());
                System.out.println("End time: " + event.getEnd().getDateTime());
                System.out.println("TimeZone: " + event.getStart().getTimeZone());

                String calendarId = "1f7b5bfeb880f50a2c5bdf03c6e76301055e55975247c4b378d55d35f9592d1c@group.calendar.google.com";
                Event createdEvent = service.events().insert(calendarId, event).execute();
                System.out.println("Event created: " + createdEvent.getHtmlLink());
                return createdEvent;
            } catch (Exception e) {
                retryCount--;
                if (retryCount == 0) {
                    e.printStackTrace();
                    return null;
                }
                System.out.println("⚠ Retrying... Attempts left: " + retryCount);
            }
        }
        return null;
    }
}