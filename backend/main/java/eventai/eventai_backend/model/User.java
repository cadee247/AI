package eventai.eventai_backend.model;

import jakarta.persistence.*;
import java.util.List;

@Entity // Marks this class as a JPA entity (table in DB)
@Table(name = "users") // Maps to "users" table
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID
    private Long id;

    // Username for login
    private String username;

    // Hashed password
    private String password;

    @OneToMany(
            mappedBy = "user", // "user" field in Event entity
            cascade = CascadeType.ALL, // Persist/Remove events automatically
            orphanRemoval = true // Remove events if removed from list
    )
    private List<Event> events; // List of events created by this user

    // Getters and setters

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public List<Event> getEvents() {
        return events;
    }
    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
