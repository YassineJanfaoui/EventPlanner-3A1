package tn.esprit.utils;

import com.fazecast.jSerialComm.SerialPort;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class Arduino {
    private static Arduino instance; // Singleton instance
    private SerialPort serialPort;
    private OutputStream outputStream;
    private InputStream inputStream;
    private Scanner scanner;

    // Private constructor to initialize connection automatically
    private Arduino() {
        try {
            connect("COM5", 9600); // Default port and baud rate
        } catch (Exception e) {
            System.out.println("Error initializing Arduino connection: " + e.getMessage());
        }
    }

    // Singleton instance retrieval
    public static Arduino getInstance() {
        if (instance == null) {
            instance = new Arduino();
        }
        return instance;
    }

    // Connect to the Arduino
    public boolean connect(String portName, int baudRate) {
        serialPort = SerialPort.getCommPort(portName);
        serialPort.setBaudRate(baudRate);

        if (serialPort.openPort()) {
            outputStream = serialPort.getOutputStream();
            inputStream = serialPort.getInputStream();
            scanner = new Scanner(inputStream);
            System.out.println("Connected to Arduino on port " + portName);
            return true;
        } else {
            System.out.println("Failed to connect to Arduino on port " + portName);
            return false;
        }
    }

    // Send data to Arduino
    public void sendData(String data) {
        if (serialPort != null && serialPort.isOpen()) {
            try {
                outputStream.write((data + "\n").getBytes()); // Ensures newline termination
                outputStream.flush();
                System.out.println("Sent: " + data);
            } catch (Exception e) {
                System.out.println("Error sending data: " + e.getMessage());
            }
        } else {
            System.out.println("Serial port is not open!");
        }
    }

    // Read data from Arduino
    public String readData() {
        if (serialPort != null && serialPort.isOpen() && scanner.hasNextLine()) {
            return scanner.nextLine();
        }
        return null;
    }

    // Disconnect from Arduino
    public void disconnect() {
        if (serialPort != null && serialPort.isOpen()) {
            try {
                serialPort.closePort();
                if (outputStream != null) outputStream.close();
                if (inputStream != null) inputStream.close();
                if (scanner != null) scanner.close();
                System.out.println("Disconnected from Arduino.");
            } catch (Exception e) {
                System.out.println("Error disconnecting: " + e.getMessage());
            }
        }
    }
    public boolean isConnected() {
        return serialPort != null && serialPort.isOpen();
    }
}
