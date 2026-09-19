public class Event {

    private int eventId;
    private String eventName;
    private String eventDate;
    private String venue;
    private String organizer;
    private int maxCapacity;
    private String status;

    // Constructor
    public Event(int eventId, String eventName, String eventDate,
                 String venue, String organizer,
                 int maxCapacity, String status) {

        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.venue = venue;
        this.organizer = organizer;
        this.maxCapacity = maxCapacity;
        this.status = status;
    }

    // Constructor for creating a new event
    public Event(String eventName, String eventDate,
                 String venue, String organizer,
                 int maxCapacity) {

        this.eventName = eventName;
        this.eventDate = eventDate;
        this.venue = venue;
        this.organizer = organizer;
        this.maxCapacity = maxCapacity;
        this.status = "ACTIVE";
    }

    // Getters

    public int getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getVenue() {
        return venue;
    }

    public String getOrganizer() {
        return organizer;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public String getStatus() {
        return status;
    }

    // Setters

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}