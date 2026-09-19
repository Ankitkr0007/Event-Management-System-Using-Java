import java.util.Scanner;

public class Mainfile {

    private static Scanner scanner = new Scanner(System.in);

    private static EventDAO eventDAO = new EventDAO();
    private static ParticipantDAO participantDAO = new ParticipantDAO();
    private static RegistrationDAO registrationDAO = new RegistrationDAO();

    public static void main(String[] args) {

        int choice;

        do {

            displayMenu();

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    createEvent();
                    break;

                case 2:
                    eventDAO.displayAllEvents();
                    break;

                case 3:
                    updateEvent();
                    break;

                case 4:
                    cancelEvent();
                    break;

                case 5:
                    createParticipant();
                    break;

                case 6:
                    registerParticipant();
                    break;

                case 7:
                    cancelRegistration();
                    break;

                case 8:
                    searchEvent();
                    break;

                case 9:
                    displayRegisteredParticipants();
                    break;

                case 10:
                    displayWaitingList();
                    break;

                case 0:
                    System.out.println(
                        "Thank you for using Event Management System!"
                    );
                    break;

                default:
                    System.out.println(
                        "Invalid choice. Please try again."
                    );
            }

        } while (choice != 0);

        scanner.close();
    }


    // =====================================================
    // DISPLAY MENU
    // =====================================================

    private static void displayMenu() {

        System.out.println();
        System.out.println("========================================");
        System.out.println("       EVENT MANAGEMENT SYSTEM");
        System.out.println("========================================");
        System.out.println("1. Create Event");
        System.out.println("2. View All Events");
        System.out.println("3. Update Event");
        System.out.println("4. Cancel Event");
        System.out.println("5. Create Participant");
        System.out.println("6. Register Participant");
        System.out.println("7. Cancel Registration");
        System.out.println("8. Search Event");
        System.out.println("9. View Registered Participants");
        System.out.println("10. View Waiting List");
        System.out.println("0. Exit");
        System.out.println("========================================");
    }


    // =====================================================
    // CREATE EVENT
    // =====================================================

    private static void createEvent() {

        System.out.println("\n===== CREATE EVENT =====");

        System.out.print("Enter event name: ");
        String eventName = scanner.nextLine();

        System.out.print("Enter event date (YYYY-MM-DD): ");
        String eventDate = scanner.nextLine();

        System.out.print("Enter venue: ");
        String venue = scanner.nextLine();

        System.out.print("Enter organizer: ");
        String organizer = scanner.nextLine();

        System.out.print("Enter maximum capacity: ");
        int maxCapacity = scanner.nextInt();
        scanner.nextLine();

        Event event = new Event(
            eventName,
            eventDate,
            venue,
            organizer,
            maxCapacity
        );

        eventDAO.addEvent(event);
    }


    // =====================================================
    // UPDATE EVENT
    // =====================================================
        private static void updateEvent() {

    System.out.println("\n===== UPDATE EVENT =====");

    System.out.print("Enter event ID: ");
    int eventId = scanner.nextInt();
    scanner.nextLine();

    int choice;

    do {

        System.out.println();
        System.out.println("What do you want to update?");
        System.out.println("1. Event Name");
        System.out.println("2. Event Date");
        System.out.println("3. Venue");
        System.out.println("4. Organizer");
        System.out.println("5. Maximum Capacity");
        System.out.println("0. Back");

        System.out.print("Enter your choice: ");
        choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {

            case 1:

                System.out.print("Enter new event name: ");
                String eventName = scanner.nextLine();

                eventDAO.updateEventField(
                    eventId,
                    choice,
                    eventName
                );

                break;

            case 2:

                System.out.print(
                    "Enter new event date (YYYY-MM-DD): "
                );

                String eventDate = scanner.nextLine();

                eventDAO.updateEventField(
                    eventId,
                    choice,
                    eventDate
                );

                break;

            case 3:

                System.out.print("Enter new venue: ");
                String venue = scanner.nextLine();

                eventDAO.updateEventField(
                    eventId,
                    choice,
                    venue
                );

                break;

            case 4:

                System.out.print("Enter new organizer: ");
                String organizer = scanner.nextLine();

                eventDAO.updateEventField(
                    eventId,
                    choice,
                    organizer
                );

                break;

            case 5:

                System.out.print(
                    "Enter new maximum capacity: "
                );

                String capacity = scanner.nextLine();

                eventDAO.updateEventField(
                    eventId,
                    choice,
                    capacity
                );

                break;

            case 0:

                System.out.println("Returning to main menu.");
                break;

            default:

                System.out.println(
                    "Invalid choice. Please try again."
                );
        }

    } while (choice != 0);
}


    // =====================================================
    // CANCEL EVENT
    // =====================================================

    private static void cancelEvent() {

        System.out.println("\n===== CANCEL EVENT =====");

        System.out.print("Enter event ID: ");
        int eventId = scanner.nextInt();
        scanner.nextLine();

        eventDAO.cancelEvent(eventId);
    }


    // =====================================================
    // CREATE PARTICIPANT
    // =====================================================

    private static void createParticipant() {

        System.out.println("\n===== CREATE PARTICIPANT =====");

        System.out.print("Enter participant name: ");
        String name = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter phone: ");
        String phone = scanner.nextLine();

        Participant participant = new Participant(
            name,
            email,
            phone
        );

        participantDAO.addParticipant(participant);
    }


    // =====================================================
    // REGISTER PARTICIPANT
    // =====================================================

    private static void registerParticipant() {

        System.out.println("\n===== REGISTER PARTICIPANT =====");

        System.out.print("Enter event ID: ");
        int eventId = scanner.nextInt();

        System.out.print("Enter participant ID: ");
        int participantId = scanner.nextInt();

        scanner.nextLine();

        registrationDAO.registerParticipant(
            eventId,
            participantId
        );
    }


    // =====================================================
    // CANCEL REGISTRATION
    // =====================================================

    private static void cancelRegistration() {

        System.out.println("\n===== CANCEL REGISTRATION =====");

        System.out.print("Enter event ID: ");
        int eventId = scanner.nextInt();

        System.out.print("Enter participant ID: ");
        int participantId = scanner.nextInt();

        scanner.nextLine();

        registrationDAO.cancelRegistration(
            eventId,
            participantId
        );
    }


    // =====================================================
    // SEARCH EVENT
    // =====================================================

    private static void searchEvent() {

        System.out.println("\n===== SEARCH EVENT =====");

        System.out.print(
            "Enter event name, venue, or organizer: "
        );

        String keyword = scanner.nextLine();

        eventDAO.searchEvents(keyword);
    }


    // =====================================================
    // DISPLAY REGISTERED PARTICIPANTS
    // =====================================================

    private static void displayRegisteredParticipants() {

        System.out.println(
            "\n===== REGISTERED PARTICIPANTS ====="
        );

        System.out.print("Enter event ID: ");
        int eventId = scanner.nextInt();

        scanner.nextLine();

        registrationDAO.displayRegisteredParticipants(
            eventId
        );
    }


    // =====================================================
    // DISPLAY WAITING LIST
    // =====================================================

    private static void displayWaitingList() {

        System.out.println("\n===== WAITING LIST =====");

        System.out.print("Enter event ID: ");
        int eventId = scanner.nextInt();

        scanner.nextLine();

        registrationDAO.displayWaitingList(eventId);
    }
}