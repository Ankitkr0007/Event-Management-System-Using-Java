import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ParticipantDAO {

    // CREATE PARTICIPANT
    public int addParticipant(Participant participant) {

        String sql = "INSERT INTO participants (name, email, phone) " +
                     "VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql,
                     java.sql.Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, participant.getName());
            ps.setString(2, participant.getEmail());
            ps.setString(3, participant.getPhone());

            ps.executeUpdate();

            // Get the automatically generated participant ID
            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {

                int participantId = rs.getInt(1);

                participant.setParticipantId(participantId);

                System.out.println(
                    "Participant created successfully!"
                );

                System.out.println(
                    "Participant ID: " + participantId
                );

                return participantId;
            }

        } catch (SQLException e) {

            System.out.println("Error creating participant.");
            e.printStackTrace();
        }

        return -1;
    }
}