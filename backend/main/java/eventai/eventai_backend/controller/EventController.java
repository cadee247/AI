package eventai.eventai_backend.controller;

import eventai.eventai_backend.dto.EventDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
public class EventController {

    // Hardcoded demo events
    private final List<EventDto> demoEvents = List.of(
            // TECH
            new EventDto(1L, "Tech Meetup", "A meetup for developers in Johannesburg",
                    LocalDateTime.of(2026, 1, 20, 18, 30), 1L, 1L, "tech", "Johannesburg"),
            new EventDto(2L, "AI Workshop", "Hands-on AI workshop for beginners",
                    LocalDateTime.of(2026, 2, 5, 10, 0), 1L, 1L, "tech", "Cape Town"),
            new EventDto(3L, "Java Conference", "Annual Java conference in Pretoria",
                    LocalDateTime.of(2026, 3, 12, 9, 0), 1L, 1L, "tech", "Pretoria"),
            new EventDto(4L, "Cybersecurity Talk", "Learn about modern cybersecurity trends",
                    LocalDateTime.of(2026, 4, 1, 15, 30), 1L, 1L, "tech", "Johannesburg"),

            // MUSIC
            new EventDto(5L, "Jazz Night", "Smooth jazz evening in Cape Town",
                    LocalDateTime.of(2026, 1, 22, 20, 0), 2L, 2L, "music", "Cape Town"),
            new EventDto(6L, "Rock Concert", "Live rock bands in Durban",
                    LocalDateTime.of(2026, 2, 10, 19, 0), 2L, 2L, "music", "Durban"),
            new EventDto(7L, "Choir Performance", "Community choir performance",
                    LocalDateTime.of(2026, 3, 15, 18, 0), 2L, 2L, "music", "Johannesburg"),
            new EventDto(8L, "Electronic Festival", "EDM festival in Pretoria",
                    LocalDateTime.of(2026, 4, 5, 16, 0), 2L, 2L, "music", "Pretoria"),

            // COOKING
            new EventDto(9L, "Baking Workshop", "Learn to bake bread and pastries",
                    LocalDateTime.of(2026, 1, 25, 10, 0), 3L, 3L, "cooking", "Johannesburg"),
            new EventDto(10L, "Vegan Cooking Class", "Plant-based cooking tips",
                    LocalDateTime.of(2026, 2, 18, 12, 0), 3L, 3L, "cooking", "Cape Town"),
            new EventDto(11L, "BBQ Masterclass", "Grill the perfect steak",
                    LocalDateTime.of(2026, 3, 20, 14, 0), 3L, 3L, "cooking", "Durban"),
            new EventDto(12L, "Pastry Secrets", "Learn French pastries",
                    LocalDateTime.of(2026, 4, 12, 11, 0), 3L, 3L, "cooking", "Pretoria"),

            // CHRISTMAS
            new EventDto(13L, "Christmas Market", "Festive Christmas market with food and crafts",
                    LocalDateTime.of(2026, 12, 15, 10, 0), 4L, 4L, "christmas", "Johannesburg"),
            new EventDto(14L, "Carols by Candlelight", "Sing Christmas carols in the park",
                    LocalDateTime.of(2026, 12, 20, 18, 30), 4L, 4L, "christmas", "Cape Town"),
            new EventDto(15L, "Christmas Workshop", "Make your own decorations",
                    LocalDateTime.of(2026, 12, 10, 14, 0), 4L, 4L, "christmas", "Durban"),
            new EventDto(16L, "Santa Parade", "See Santa in the city parade",
                    LocalDateTime.of(2026, 12, 22, 15, 0), 4L, 4L, "christmas", "Pretoria")
    );

    @GetMapping
    public ResponseEntity<List<EventDto>> getAllEvents(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String city
    ) {
        List<EventDto> filtered = demoEvents.stream()
                .filter(e -> category == null || e.getCategory().equalsIgnoreCase(category))
                .filter(e -> city == null || e.getCity().equalsIgnoreCase(city))
                .collect(Collectors.toList());
        return ResponseEntity.ok(filtered);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDto> getEventById(@PathVariable Long id) {
        return demoEvents.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
