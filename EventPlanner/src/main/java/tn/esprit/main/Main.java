package tn.esprit.main;
import tn.esprit.entities.Feedback;
import tn.esprit.entities.ProjectSubmission;
import tn.esprit.entities.Team;
import tn.esprit.services.FeedbackServices;
import tn.esprit.services.ProjectSubmissionServices;
import tn.esprit.services.TeamServices;
import tn.esprit.utils.MyDatabase;
import tn.esprit.utils.MyDatabase;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import com.assemblyai.api.AssemblyAI;
import javax.sound.sampled.*;
import com.assemblyai.api.resources.transcripts.requests.TranscriptParams;
import com.assemblyai.api.resources.transcripts.types.*;
import com.assemblyai.api.RealtimeTranscriber;
import tn.esprit.utils.SentimentAnalyzer;
import tn.esprit.utils.XAiApiService;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import static java.lang.Thread.interrupted;
import static tn.esprit.utils.SentimentAnalyzer.analyze;

public class Main {

    public static void main(String[] args) {


        MyDatabase db = MyDatabase.getInstance();
        /*
        Thread thread = new Thread(()-> {
            try {
                RealtimeTranscriber realtimeTranscriber = new RealtimeTranscriber.Builder()
                        .apiKey("9bb02b5764304475aa11ca9840e95660")
                        .sampleRate(16_000)
                        .onSessionBegins(sessionBegins -> System.out.println(
                                "Session id"+sessionBegins.getSessionId()
                        )).onPartialTranscript(transcript ->{
                            if (!transcript.getText().isEmpty())
                                System.out.println(transcript.getText());
                        })
                        .onFinalTranscript(transcript -> System.out.println("final"+ transcript.getText()))
                        .onError(err -> System.out.println("error" +err.getMessage() ))
                        .build();
                System.out.println("connecting to real-time transcriber");
                realtimeTranscriber.connect();
                System.out.println("Starting recording");
                AudioFormat audioFormat = new AudioFormat(16_000, 16, 1, true, false);
                TargetDataLine targetDataLine = AudioSystem.getTargetDataLine(audioFormat);
                targetDataLine.open(audioFormat);
                byte[] data = new byte[targetDataLine.getBufferSize()];
                targetDataLine.start();
                while (!interrupted()) {
                    targetDataLine.read(data, 0, data.length);
                    realtimeTranscriber.sendAudio(data);
                }
                System.out.println("End of recording");
                targetDataLine.close();
                System.out.println("closing realtime transcript connection");
                realtimeTranscriber.close();
            }
            catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
        System.out.println("press enter key to terminate");
        try {
            System.in.read();
            System.exit(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
        System.out.println("Test 1: " + analyze("This event was fantastic!"));
        System.out.println("Test 2: " + analyze("Terrible experience"));


    }

    }


