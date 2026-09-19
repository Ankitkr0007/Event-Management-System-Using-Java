import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistrationDAO {

    public void registerParticipant(int eventId, int participantId) {

        String eventSql =
                "SELECT max_capacity, status FROM events WHERE event_id = ?";

        String duplicateSql =
                "SELECT COUNT(*) FROM registrations " +
                "WHERE event_id = ? AND participant_id = ? " +
                "AND status = 'REGISTERED'";

        String countSql =
                "SELECT COUNT(*) FROM registrations " +
                "WHERE event_id = ? AND status = 'REGISTERED'";

        String registerSql =
                "INSERT INTO registrations " +
                "(event_id, participant_id, status) " +
                "VALUES (?, ?, 'REGISTERED')";

        try (Connection con = DBConnection.getConnection()) {

            // ------------------------------------------------
            // 1. Check whether the event exists
            // ------------------------------------------------

            int maxCapacity;
            String eventStatus;

            try (PreparedStatement ps =
                         con.prepareStatement(eventSql)) {

                ps.setInt(1, eventId);

                ResultSet rs = ps.executeQuery();

                if (!rs.next()) {
                    System.out.println("Event not found.");
                    return;
                }

                maxCapacity = rs.getInt("max_capacity");
                eventStatus = rs.getString("status");
            }

            // ------------------------------------------------
            // 2. Check whether the event is active
            // ------------------------------------------------

            if (!"ACTIVE".equalsIgnoreCase(eventStatus)) {

                System.out.println(
                    "Event is not active. Registration is not allowed."
                );

                return;
            }

            // ------------------------------------------------
            // 3. Check duplicate registration
            // ------------------------------------------------

            try (PreparedStatement ps =
                         con.prepareStatement(duplicateSql)) {

                ps.setInt(1, eventId);
                ps.setInt(2, participantId);

                ResultSet rs = ps.executeQuery();

                rs.next();

                if (rs.getInt(1) > 0) {

                    System.out.println(
                        "Participant is already registered for this event."
                    );

                    return;
                }
            }

            // ------------------------------------------------
            // 4. Count currently registered participants
            // ------------------------------------------------

            int registeredCount;

            try (PreparedStatement ps =
                         con.prepareStatement(countSql)) {

                ps.setInt(1, eventId);

                ResultSet rs = ps.executeQuery();

                rs.next();

                registeredCount = rs.getInt(1);
            }

            // ------------------------------------------------
            // 5. Check capacity
            // ------------------------------------------------

            if (registeredCount >= maxCapacity) {

                System.out.println(
                    "Event is full. Participant will be added to waiting list."
                );

                addToWaitingList(con, eventId, participantId);

                return;
            }

            // ------------------------------------------------
            // 6. Register participant
            // ------------------------------------------------

            try (PreparedStatement ps =
                         con.prepareStatement(registerSql)) {

                ps.setInt(1, eventId);
                ps.setInt(2, participantId);

                ps.executeUpdate();

                System.out.println(
                    "Participant registered successfully!"
                );
            }

        } catch (SQLException e) {

            System.out.println(
                "Error registering participant."
            );

            e.printStackTrace();
        }
    }


    // ========================================================
    // ADD PARTICIPANT TO WAITING LIST
    // ========================================================

    private void addToWaitingList(
            Connection con,
            int eventId,
            int participantId) {

        String checkSql =
                "SELECT COUNT(*) FROM waiting_list " +
                "WHERE event_id = ? AND participant_id = ?";

        String positionSql =
                "SELECT COALESCE(MAX(position), 0) + 1 " +
                "FROM waiting_list WHERE event_id = ?";

        String insertSql =
                "INSERT INTO waiting_list " +
                "(event_id, participant_id, position) " +
                "VALUES (?, ?, ?)";

        try {

            // --------------------------------------------
            // Check if participant is already waiting
            // --------------------------------------------

            try (PreparedStatement ps =
                         con.prepareStatement(checkSql)) {

                ps.setInt(1, eventId);
                ps.setInt(2, participantId);

                ResultSet rs = ps.executeQuery();

                rs.next();

                if (rs.getInt(1) > 0) {

                    System.out.println(
                        "Participant is already on the waiting list."
                    );

                    return;
                }
            }

            // --------------------------------------------
            // Find next waiting-list position
            // --------------------------------------------

            int position;

            try (PreparedStatement ps =
                         con.prepareStatement(positionSql)) {

                ps.setInt(1, eventId);

                ResultSet rs = ps.executeQuery();

                rs.next();

                position = rs.getInt(1);
            }

            // --------------------------------------------
            // Add participant to waiting list
            // --------------------------------------------

            try (PreparedStatement ps =
                         con.prepareStatement(insertSql)) {

                ps.setInt(1, eventId);
                ps.setInt(2, participantId);
                ps.setInt(3, position);

                ps.executeUpdate();

                System.out.println(
                    "Participant added to waiting list."
                );

                System.out.println(
                    "Waiting list position: " + position
                );
            }

        } catch (SQLException e) {

            System.out.println(
                "Error adding participant to waiting list."
            );

            e.printStackTrace();
        }
    }

    public void cancelRegistration(int eventId, int participantId) {

    String checkSql =
            "SELECT COUNT(*) FROM registrations " +
            "WHERE event_id = ? AND participant_id = ? " +
            "AND status = 'REGISTERED'";

    String cancelSql =
            "UPDATE registrations SET status = 'CANCELLED' " +
            "WHERE event_id = ? AND participant_id = ? " +
            "AND status = 'REGISTERED'";

    try (Connection con = DBConnection.getConnection()) {

        // --------------------------------------------
        // 1. Check registration exists
        // --------------------------------------------

        try (PreparedStatement ps =
                     con.prepareStatement(checkSql)) {

            ps.setInt(1, eventId);
            ps.setInt(2, participantId);

            ResultSet rs = ps.executeQuery();

            rs.next();

            if (rs.getInt(1) == 0) {

                System.out.println(
                    "Registration not found."
                );

                return;
            }
        }

        // --------------------------------------------
        // 2. Cancel registration
        // --------------------------------------------

        try (PreparedStatement ps =
                     con.prepareStatement(cancelSql)) {

            ps.setInt(1, eventId);
            ps.setInt(2, participantId);

            ps.executeUpdate();

            System.out.println(
                "Registration cancelled successfully!"
            );
        }

        // --------------------------------------------
        // 3. Promote first person from waiting list
        // --------------------------------------------

        promoteFromWaitingList(con, eventId);

    } catch (SQLException e) {

        System.out.println(
            "Error cancelling registration."
        );

        e.printStackTrace();
    }
}

    private void promoteFromWaitingList(
        Connection con,
        int eventId) {

    String selectSql =
            "SELECT waiting_id, participant_id, position " +
            "FROM waiting_list " +
            "WHERE event_id = ? " +
            "ORDER BY position ASC " +
            "LIMIT 1";

    String registerSql =
            "INSERT INTO registrations " +
            "(event_id, participant_id, status) " +
            "VALUES (?, ?, 'REGISTERED')";

    String deleteSql =
            "DELETE FROM waiting_list " +
            "WHERE waiting_id = ?";

    String updatePositionSql =
            "UPDATE waiting_list " +
            "SET position = position - 1 " +
            "WHERE event_id = ? AND position > ?";

    try {

        int waitingId;
        int participantId;
        int position;

        // --------------------------------------------
        // 1. Get first person from waiting list
        // --------------------------------------------

        try (PreparedStatement ps =
                     con.prepareStatement(selectSql)) {

            ps.setInt(1, eventId);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {

                System.out.println(
                    "No participant is waiting."
                );

                return;
            }

            waitingId = rs.getInt("waiting_id");
            participantId = rs.getInt("participant_id");
            position = rs.getInt("position");
        }

        // --------------------------------------------
        // 2. Register waiting participant
        // --------------------------------------------

        try (PreparedStatement ps =
                     con.prepareStatement(registerSql)) {

            ps.setInt(1, eventId);
            ps.setInt(2, participantId);

            ps.executeUpdate();
        }

        // --------------------------------------------
        // 3. Remove participant from waiting list
        // --------------------------------------------

        try (PreparedStatement ps =
                     con.prepareStatement(deleteSql)) {

            ps.setInt(1, waitingId);

            ps.executeUpdate();
        }

        // --------------------------------------------
        // 4. Update remaining positions
        // --------------------------------------------

        try (PreparedStatement ps =
                     con.prepareStatement(updatePositionSql)) {

            ps.setInt(1, eventId);
            ps.setInt(2, position);

            ps.executeUpdate();
        }

        System.out.println(
            "Participant " + participantId +
            " has been promoted from the waiting list."
        );

    } catch (SQLException e) {

        System.out.println(
            "Error promoting participant."
        );

        e.printStackTrace();
    }
}

    public void displayRegisteredParticipants(int eventId) {

    String sql =
            "SELECT p.participant_id, p.name, p.email, p.phone " +
            "FROM participants p " +
            "JOIN registrations r " +
            "ON p.participant_id = r.participant_id " +
            "WHERE r.event_id = ? " +
            "AND r.status = 'REGISTERED' " +
            "ORDER BY p.participant_id";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, eventId);

        ResultSet rs = ps.executeQuery();

        boolean found = false;

        System.out.println(
            "\n===== REGISTERED PARTICIPANTS ====="
        );

        while (rs.next()) {

            found = true;

            System.out.println(
                "Participant ID : " +
                rs.getInt("participant_id")
            );

            System.out.println(
                "Name           : " +
                rs.getString("name")
            );

            System.out.println(
                "Email          : " +
                rs.getString("email")
            );

            System.out.println(
                "Phone          : " +
                rs.getString("phone")
            );

            System.out.println("--------------------------------");
        }

        if (!found) {
            System.out.println(
                "No registered participants."
            );
        }

    } catch (SQLException e) {

        System.out.println(
            "Error retrieving participants."
        );

        e.printStackTrace();
    }
}

    public void displayWaitingList(int eventId) {

    String sql =
            "SELECT w.position, p.participant_id, " +
            "p.name, p.email, p.phone " +
            "FROM waiting_list w " +
            "JOIN participants p " +
            "ON w.participant_id = p.participant_id " +
            "WHERE w.event_id = ? " +
            "ORDER BY w.position";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, eventId);

        ResultSet rs = ps.executeQuery();

        boolean found = false;

        System.out.println(
            "\n========== WAITING LIST =========="
        );

        while (rs.next()) {

            found = true;

            System.out.println(
                "Position       : " +
                rs.getInt("position")
            );

            System.out.println(
                "Participant ID : " +
                rs.getInt("participant_id")
            );

            System.out.println(
                "Name           : " +
                rs.getString("name")
            );

            System.out.println(
                "Email          : " +
                rs.getString("email")
            );

            System.out.println("--------------------------------");
        }

        if (!found) {
            System.out.println(
                "Waiting list is empty."
            );
        }

    } catch (SQLException e) {

        System.out.println(
            "Error retrieving waiting list."
        );

        e.printStackTrace();
    }
}
}