package eventai.eventai_backend.service;

import eventai.eventai_backend.dto.CreateEventRequest;
import eventai.eventai_backend.dto.EventDto;
import eventai.eventai_backend.dto.UpdateEventRequest;
import eventai.eventai_backend.model.Event;
import eventai.eventai_backend.model.Organizer;
import eventai.eventai_backend.model.User;
import eventai.eventai_backend.repository.EventRepository;
import eventai.eventai_backend.repository.OrganizerRepository;
import eventai.eventai_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service // Marks this class as a Spring service component (business logic layer)
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final OrganizerRepository organizerRepository;

    /**
     * Constructor injection of repositories.
     * Allows the service to access events, users, and organizers in the database.
     */
    public EventService(EventRepository eventRepository, UserRepository userRepository, OrganizerRepository organizerRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.organizerRepository = organizerRepository;
    }

    /**
     * Retrieve all events from the database and convert them to DTOs.
     * @return List of EventDto
     */
    public List<EventDto> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(this::toDto) // Convert Event entity to EventDto
                .collect(Collectors.toList());
    }

    /**
     * Retrieve a single event by its ID.
     * @param id Event ID
     * @return EventDto
     * @throws RuntimeException if event not found
     */
    public EventDto getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        return toDto(event);
    }

    /**
     * Create a new event and save it to the database.
     * Optionally associate an organizer and a user with the event.
     * @param request CreateEventRequest DTO with event data
     * @return Created EventDto
     */
    public EventDto createEvent(CreateEventRequest request) {
        Event event = new Event();
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setDate(request.getDate());

        // Link the event to an organizer if organizerId is provided
        if (request.getOrganizerId() != null) {
            Organizer organizer = organizerRepository.findById(request.getOrganizerId())
                    .orElseThrow(() -> new RuntimeException("Organizer not found"));
            event.setOrganizer(organizer);
        }

        // Link the event to a user if userId is provided
        if (request.getUserId() != null) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            event.setUser(user);
        }

        eventRepository.save(event); // Persist the event in the database
        return toDto(event);
    }

    /**
     * Update an existing event by ID.
     * Optionally update associated organizer and user.
     * @param id Event ID
     * @param request UpdateEventRequest DTO with updated data
     * @return Updated EventDto
     */
    public EventDto updateEvent(Long id, UpdateEventRequest request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // Update basic event fields
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setDate(request.getDate());

        // Update organizer if provided
        if (request.getOrganizerId() != null) {
            Organizer organizer = organizerRepository.findById(request.getOrganizerId())
                    .orElseThrow(() -> new RuntimeException("Organizer not found"));
            event.setOrganizer(organizer);
        }

        // Update user if provided
        if (request.getUserId() != null) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            event.setUser(user);
        }

        eventRepository.save(event); // Persist changes
        return toDto(event);
    }

    /**
     * Delete an event from the database by ID.
     * @param id Event ID
     * @throws RuntimeException if event not found
     */
    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        eventRepository.delete(event);
    }

    /**
     * Helper method to convert Event entity to EventDto.
     * Includes optional fields for user and organizer IDs.
     * @param event Event entity
     * @return EventDto
     */
    private EventDto toDto(Event event) {
        EventDto dto = new EventDto();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setDescription(event.getDescription());
        dto.setDate(event.getDate());
        dto.setCreatedByUserId(event.getUser() != null ? event.getUser().getId() : null);
        dto.setOrganizerId(event.getOrganizer() != null ? event.getOrganizer().getId() : null);
        return dto;
    }
}
