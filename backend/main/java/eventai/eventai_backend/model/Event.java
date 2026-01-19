package eventai.eventai_backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity // Marks this class as a JPA entity (table in DB)
@Table(name = "events") // Maps to "events" table
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID
    private Long id;

    // Event title
    private String title;

    // Event description
    private String description;

    // Event date and time
    private LocalDateTime date;

    @ManyToOne // Many events can belong to one organizer
    @JoinColumn(name = "organizer_id") // Foreign key column
    private Organizer organizer;

    @ManyToOne // Many events can be created by one user
    @JoinColumn(name = "user_id") // Foreign key column
    private User user;

    // Getters and setters

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Organizer getOrganizer() {
        return organizer;
    }
    public void setOrganizer(Organizer organizer) {
        this.organizer = organizer;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
}
