import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EventDAO {

    public void addEvent(Event event) {

        String sql = "INSERT INTO events " +
                     "(event_name, event_date, venue, organizer, max_capacity, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, event.getEventName());
            ps.setString(2, event.getEventDate());
            ps.setString(3, event.getVenue());
            ps.setString(4, event.getOrganizer());
            ps.setInt(5, event.getMaxCapacity());
            ps.setString(6, event.getStatus());

            ps.executeUpdate();

            System.out.println("Event created successfully!");

        } catch (SQLException e) {
            System.out.println("Error creating event.");
            e.printStackTrace();
        }
    }

    public void displayAllEvents() {

    String sql = "SELECT * FROM events";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql);
         java.sql.ResultSet rs = ps.executeQuery()) {

        System.out.println("\n========== ALL EVENTS ==========");

        while (rs.next()) {

            System.out.println("Event ID     : " + rs.getInt("event_id"));
            System.out.println("Event Name   : " + rs.getString("event_name"));
            System.out.println("Date         : " + rs.getDate("event_date"));
            System.out.println("Venue        : " + rs.getString("venue"));
            System.out.println("Organizer    : " + rs.getString("organizer"));
            System.out.println("Max Capacity : " + rs.getInt("max_capacity"));
            System.out.println("Status       : " + rs.getString("status"));
            System.out.println("--------------------------------");
        }

    } catch (SQLException e) {
        System.out.println("Error retrieving events.");
        e.printStackTrace();
    }
}

//     public void updateEvent(Event event) {

//     String sql = "UPDATE events SET " +
//                  "event_name = ?, " +
//                  "event_date = ?, " +
//                  "venue = ?, " +
//                  "organizer = ?, " +
//                  "max_capacity = ? " +
//                  "WHERE event_id = ?";

//     try (Connection con = DBConnection.getConnection();
//          PreparedStatement ps = con.prepareStatement(sql)) {

//         ps.setString(1, event.getEventName());
//         ps.setString(2, event.getEventDate());
//         ps.setString(3, event.getVenue());
//         ps.setString(4, event.getOrganizer());
//         ps.setInt(5, event.getMaxCapacity());
//         ps.setInt(6, event.getEventId());

//         int rows = ps.executeUpdate();

//         if (rows > 0) {
//             System.out.println("Event updated successfully!");
//         } else {
//             System.out.println("Event not found.");
//         }

//     } catch (SQLException e) {
//         System.out.println("Error updating event.");
//         e.printStackTrace();
//     }
// }

    public void cancelEvent(int eventId) {

    String sql = "UPDATE events SET status = 'CANCELLED' " +
                 "WHERE event_id = ?";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, eventId);

        int rows = ps.executeUpdate();

        if (rows > 0) {
            System.out.println("Event cancelled successfully!");
        } else {
            System.out.println("Event not found.");
        }

    } catch (SQLException e) {
        System.out.println("Error cancelling event.");
        e.printStackTrace();
    }
}

    public void searchEvents(String keyword) {

    String sql =
            "SELECT * FROM events " +
            "WHERE event_name LIKE ? " +
            "OR venue LIKE ? " +
            "OR organizer LIKE ?";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        String searchKeyword = "%" + keyword + "%";

        ps.setString(1, searchKeyword);
        ps.setString(2, searchKeyword);
        ps.setString(3, searchKeyword);

        ResultSet rs = ps.executeQuery();

        boolean found = false;

        System.out.println("\n========== SEARCH RESULTS ==========");

        while (rs.next()) {

            found = true;

            System.out.println(
                "Event ID     : " +
                rs.getInt("event_id")
            );

            System.out.println(
                "Event Name   : " +
                rs.getString("event_name")
            );

            System.out.println(
                "Date         : " +
                rs.getDate("event_date")
            );

            System.out.println(
                "Venue        : " +
                rs.getString("venue")
            );

            System.out.println(
                "Organizer    : " +
                rs.getString("organizer")
            );

            System.out.println(
                "Max Capacity : " +
                rs.getInt("max_capacity")
            );

            System.out.println(
                "Status       : " +
                rs.getString("status")
            );

            System.out.println("--------------------------------");
        }

        if (!found) {
            System.out.println("No matching events found.");
        }

    } catch (SQLException e) {

        System.out.println(
            "Error searching events."
        );

        e.printStackTrace();
    }
}

    public void updateEventField(int eventId, int choice, String value) {

    String column;

    switch (choice) {

        case 1:
            column = "event_name";
            break;

        case 2:
            column = "event_date";
            break;

        case 3:
            column = "venue";
            break;

        case 4:
            column = "organizer";
            break;

        case 5:
            column = "max_capacity";
            break;

        default:
            System.out.println("Invalid update choice.");
            return;
    }

    String sql = "UPDATE events SET " + column + " = ? " +
                 "WHERE event_id = ?";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        if (choice == 5) {
            ps.setInt(1, Integer.parseInt(value));
        } else {
            ps.setString(1, value);
        }

        ps.setInt(2, eventId);

        int rows = ps.executeUpdate();

        if (rows > 0) {
            System.out.println("Event updated successfully!");
        } else {
            System.out.println("Event not found.");
        }

    } catch (SQLException e) {

        System.out.println("Error updating event.");
        e.printStackTrace();

    } catch (NumberFormatException e) {

        System.out.println("Maximum capacity must be a number.");
    }
}
}