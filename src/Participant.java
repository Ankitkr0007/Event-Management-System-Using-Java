public class Participant {

    private int participantId;
    private String name;
    private String email;
    private String phone;

    // Constructor for existing participant
    public Participant(int participantId, String name,
                       String email, String phone) {

        this.participantId = participantId;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    // Constructor for new participant
    public Participant(String name, String email, String phone) {

        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    // Getters

    public int getParticipantId() {
        return participantId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    // Setters

    public void setParticipantId(int participantId) {
        this.participantId = participantId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}