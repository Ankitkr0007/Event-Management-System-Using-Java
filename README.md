# Event Management System

A Java-based Event Management System built using **Java, JDBC, and MySQL**. The system provides a menu-driven interface for creating and managing events, registering participants, handling cancellations, searching events, and maintaining a waiting list when an event reaches its maximum capacity.

## Features

- Create and manage events
- View all events
- Update individual event details
- Cancel events
- Create participants
- Register participants for events
- Prevent duplicate registrations
- Cancel participant registrations
- Automatically manage waiting lists when an event is full
- Promote participants from the waiting list when a registration is cancelled
- Search events by relevant details
- Display registered participants
- Display waiting-list participants
- MySQL database connectivity using JDBC

## Technologies Used

- **Java**
- **JDBC**
- **MySQL**
- **MySQL Connector/J**
- **VS Code**
- **Git & GitHub**

## Project Structure

```text
Event Management System/
│
├── .gitignore
├── README.md
│
├── lib/
│   └── mysql-connector-j-8.0.46.jar
│
├── .vscode/
│   └── settings.json
│
└── src/
    ├── Mainfile.java
    ├── DBConnection.java
    │
    ├── Event.java
    ├── Participant.java
    │
    ├── EventDAO.java
    ├── ParticipantDAO.java
    └── RegistrationDAO.java


## 👨‍💻 Author

**Ankit Kumar**

BCA Student | AI & Machine Learning | Cybersecurity Enthusiast

If you found this project helpful, don't forget to ⭐ star the repository!