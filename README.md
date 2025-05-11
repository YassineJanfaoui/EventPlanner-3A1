# HackForge - JavaFX Event Planner

## Description

HackForge is a full-featured desktop application for event management, built using JavaFX. It allows users to manage events, resources, venues, and billing in a modular and user-friendly interface. This application supports role-based access, modular MVC design, and persistent data storage using a relational database. Developed as part of the PIDEV 3A coursework at [Esprit School of Engineering](https://esprit.tn).

## Table of Contents

- [HackForge - JavaFX Event Planner](#hackforge---javafx-event-planner)
  - [Description](#description)
  - [Table of Contents](#table-of-contents)
  - [Tech Stack](#tech-stack)
    - [UI](#ui)
    - [Backend](#backend)
    - [Database](#database)
  - [Installation](#installation)
    - [Prerequisites](#prerequisites)
    - [Setup Steps](#setup-steps)
  - [Contributions](#contributions)
  - [Acknowledgments](#acknowledgments)

## Tech Stack

### UI

The interface is developed with JavaFX and FXML. It follows a clean and modular design using MVC pattern. External libraries like FontAwesomeFX and ControlsFX are used for icons and advanced UI components.

### Backend

- Java 17+
- Maven for dependency management and builds

### Database

- MySQL 8.0+ (connected via JDBC)

## Installation

### Prerequisites

- Java Development Kit (JDK) 17+
- IntelliJ IDEA (or another Java IDE)
- Maven
- MySQL Server 8.0+

### Setup Steps

```bash
# Clone the repository
git clone https://github.com/YassineJanfaoui/EventPlanner-3A1.git
cd EventPlanner-3A1

# Open the project in your IDE and build using Maven
```

- Configure the database connection in MyDatabase.java

- Run the main application class Main.java from your IDE.

## Contributions

We thank these people for contributing to the project by implementing different modules

- [Mohamed Yassine Janfaoui](mailto:mohamedyassine.janfaoui@esprit.tn) : Bill and equipment modules
- [Ayoub Rebhi](mailto:ayoub.rebhi@esprit.tn) : User module
- [Molka Boukhris](mailto:molka.boukhris@esprit.tn) : Venue and Catering modules
- [Mohamed Ali Mraihi](mailto:medali.mraihi@esprit.tn) : Feedback and Teams modules
- [Semah Ayachi](mailto:semah.ayachi@esprit.tn) : Event module
- [Mahmoud Ben Khelil](mailto:mahmoud.benkhelil@esprit.tn) : Partner and Workshop modules

## Acknowledgments

This project was completed under the guidance of [Professor Nouha Benslimen](mailto:nouha.benslimen@esprit.tn) at [**Esprit School of Engineering**](https://esprit.tn).
