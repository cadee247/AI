package eventai.eventai_backend.dto;

import java.time.LocalDateTime;

public class EventDto {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime date;
    private Long createdByUserId;
    private Long organizerId;

    // NEW for demo filtering
    private String category;
    private String city;

    public EventDto() {}

    public EventDto(Long id, String title, String description, LocalDateTime date,
                    Long createdByUserId, Long organizerId, String category, String city) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.createdByUserId = createdByUserId;
        this.organizerId = organizerId;
        this.category = category;
        this.city = city;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public Long getCreatedByUserId() { return createdByUserId; }
    public void setCreatedByUserId(Long createdByUserId) { this.createdByUserId = createdByUserId; }

    public Long getOrganizerId() { return organizerId; }
    public void setOrganizerId(Long organizerId) { this.organizerId = organizerId; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
}
