#include <WiFi.h>
#include <HTTPClient.h>

const char* ssid = "ESP-896E54";       // Same as ESP8266 AP SSID
const char* password = "";             // Same as ESP8266 AP Password
const char* serverIP = "192.168.4.1";  // Default SoftAP IP for ESP8266
const int ledPin = 8;

void setup() {
    Serial.begin(115200);
    delay(1000);
    Serial.println("Connecting to ESP8266...");
    pinMode(ledPin, OUTPUT);
    digitalWrite(ledPin, LOW); 
    WiFi.begin(ssid, NULL);
    while (WiFi.status() != WL_CONNECTED) {
        digitalWrite(ledPin, HIGH); // LED ON
        delay(200);
        digitalWrite(ledPin, LOW);  // LED OFF
        delay(200);
    }
    digitalWrite(ledPin, LOW);
    Serial.println("\nConnected!");
    Serial.print("ESP32 IP: ");
    Serial.println(WiFi.localIP());
}

void loop() {
    if (Serial.available()) {
        char command = Serial.read();
        if (command == '9') {
            executeRoutine1();
        } else {
            sendCommand(command);
        }
    }
}

void sendCommand(char command) {
    String path;

    switch (command) {
        case '1': path = "/forward"; break;
        case '2': path = "/backward"; break;
        case '4': path = "/left"; break;
        case '6': path = "/right"; break;
        case '0': path = "/stop"; break;
        case 'b': path = "/beep"; break;
        default:
            Serial.println("Invalid Command!");
            return;
    }

    HTTPClient http;
    String url = "http://" + String(serverIP) + path;
    http.begin(url);
    int httpResponseCode = http.GET();
    http.end();

    Serial.print("Sent: ");
    Serial.print(command);
    Serial.print(" | HTTP Response: ");
    Serial.println(httpResponseCode);
}

void executeRoutine1() {
    Serial.println("Executing Routine...");

    sendCommand('1');
    delay(1000);  

    sendCommand('0');  
    delay(50);
    
    sendCommand('6');
    delay(250);        

    sendCommand('1'); 
    delay(500); 

    sendCommand('0');          
}

