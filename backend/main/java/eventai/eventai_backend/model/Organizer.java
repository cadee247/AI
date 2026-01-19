package eventai.eventai_backend.model;

import jakarta.persistence.*;
import java.util.List;

@Entity // Marks this class as a JPA entity (table in DB)
@Table(name = "organizers") // Maps to "organizers" table
public class Organizer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID
    private Long id;

    // Organizer name
    private String name;

    // Organizer email
    private String email;

    @OneToMany(
            mappedBy = "organizer", // "organizer" field in Event entity
            cascade = CascadeType.ALL, // Persist/Remove events automatically
            orphanRemoval = true // Remove events if removed from list
    )
    private List<Event> events; // List of events organized by this organizer

    // Getters and setters

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public List<Event> getEvents() {
        return events;
    }
    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
