package eventai.eventai_backend.dto;

import java.time.LocalDateTime;

public class CreateEventRequest {

    // Event title
    private String title;

    // Event description
    private String description;

    // Date and time of the event
    private LocalDateTime date;

    // ID of the event organizer
    private Long organizerId;

    // ID of the user creating the event
    private Long userId;

    // Get event title
    public String getTitle() {
        return title;
    }

    // Set event title
    public void setTitle(String title) {
        this.title = title;
    }

    // Get event description
    public String getDescription() {
        return description;
    }

    // Set event description
    public void setDescription(String description) {
        this.description = description;
    }

    // Get event date and time
    public LocalDateTime getDate() {
        return date;
    }

    // Set event date and time
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    // Get organizer ID
    public Long getOrganizerId() {
        return organizerId;
    }

    // Set organizer ID
    public void setOrganizerId(Long organizerId) {
        this.organizerId = organizerId;
    }

    // Get user ID
    public Long getUserId() {
        return userId;
    }

    // Set user ID
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
