#include <ESP8266WiFi.h>
#include <ESP8266WebServer.h>

// WiFi Credentials
const char* ssid = "ESP8266";
const char* password = "testing";  // Minimum 8 characters

// Create Web Server on port 80
ESP8266WebServer server(80);

// Motor driver pins
#define enA  14  // D5 (PWM)
#define IN1  12  // D6
#define IN2  13  // D7
#define enB  15  // D8 (PWM)
#define IN3  2   // D4
#define IN4  0   // D3
#define beep 16  // D0 (Buzzer)

void setup() {
  Serial.begin(115200);
  delay(1000);
  Serial.println("Initializing...");

  // Motor driver pins setup
  pinMode(enA, OUTPUT);
  pinMode(enB, OUTPUT);
  pinMode(IN1, OUTPUT);
  pinMode(IN2, OUTPUT);
  pinMode(IN3, OUTPUT);
  pinMode(IN4, OUTPUT);
  pinMode(beep, OUTPUT);

  // Initialize motors stopped
  stopMotors();

  // Set up WiFi Access Point
  WiFi.softAP(ssid, password);
  Serial.print("WiFi AP Started: ");
  Serial.println(ssid);
  Serial.print("IP Address: ");
  Serial.println(WiFi.softAPIP());

  // Define server routes
  server.on("/", handleRoot);
  server.on("/forward", [](){ handleMotorControl('1'); });
  server.on("/backward", [](){ handleMotorControl('2'); });
  server.on("/left", [](){ handleMotorControl('4'); });
  server.on("/right", [](){ handleMotorControl('6'); });
  server.on("/stop", [](){ handleMotorControl('0'); });
  server.on("/beep", [](){ handleMotorControl('b'); });

  // Start the server
  server.begin();
  Serial.println("HTTP Server started!");
}

void loop() {
  server.handleClient();  // Listen for web requests

  // Serial Input Handling
  if (Serial.available()) {
    char serialCommand = Serial.read();
    handleMotorControl(serialCommand);
  }
}

// Web Page
void handleRoot() {
  server.send(200, "text/html",
              "<h1>ESP8266 Car Control</h1>"
              "<p><a href='/forward'>Forward</a></p>"
              "<p><a href='/backward'>Backward</a></p>"
              "<p><a href='/left'>Left</a></p>"
              "<p><a href='/right'>Right</a></p>"
              "<p><a href='/stop'>Stop</a></p>"
              "<p><a href='/beep'>Beep</a></p>");
}

// **STOP Motors Function** (Always call before changing direction)
void stopMotors() {
  digitalWrite(IN1, LOW);
  digitalWrite(IN2, LOW);
  digitalWrite(IN3, LOW);
  digitalWrite(IN4, LOW);
  analogWrite(enA, 0);
  analogWrite(enB, 0);
  Serial.println("Motors Stopped");
}

// **Unified Motor Control Function**
void handleMotorControl(char command) {
  stopMotors();  // Prevent unwanted movement

  switch (command) {
    case '1':  // Backward (was Forward)
      digitalWrite(IN1, LOW);
      digitalWrite(IN2, HIGH);
      digitalWrite(IN3, LOW);
      digitalWrite(IN4, HIGH);
      analogWrite(enA, 500);
      analogWrite(enB, 500);
      Serial.println("Moving forward");
      server.send(200, "text/plain", "Moving forward");
      break;

    case '2':  // Forward (was Backward)
      digitalWrite(IN1, HIGH);  // Right motor forward
      digitalWrite(IN2, LOW);
      digitalWrite(IN3, HIGH);  // Left motor forward
      digitalWrite(IN4, LOW);
      analogWrite(enA, 500);  // Adjust speed if necessary
      analogWrite(enB, 500);
      Serial.println("Moving backward");
      server.send(200, "text/plain", "Moving backward");
      break;

    case '4':  // Right (was Left)
      digitalWrite(IN1, HIGH);
      digitalWrite(IN2, LOW);
      digitalWrite(IN3, LOW);
      digitalWrite(IN4, HIGH);
      analogWrite(enA, 200);
      analogWrite(enB, 200);
      Serial.println("Turning left");
      server.send(200, "text/plain", "Turning left");
      break;

    case '6':  // Left (was Right)
      digitalWrite(IN1, LOW);
      digitalWrite(IN2, HIGH);
      digitalWrite(IN3, HIGH);
      digitalWrite(IN4, LOW);
      analogWrite(enA, 200);
      analogWrite(enB, 200);
      Serial.println("Turning right");
      server.send(200, "text/plain", "Turning right");
      break;

    case '0':  // Stop
      stopMotors();
      Serial.println("Stopping");
      server.send(200, "text/plain", "Stopped");
      break;

    case 'b':  // Beep
      digitalWrite(beep, HIGH);
      delay(500);
      digitalWrite(beep, LOW);
      Serial.println("Beep");
      server.send(200, "text/plain", "Beep!");
      break;

    default:
      Serial.println("Unknown command");
      break;
  }
}


