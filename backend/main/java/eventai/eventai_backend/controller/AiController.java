package eventai.eventai_backend.controller;

import eventai.eventai_backend.dto.EventDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"}, allowCredentials = "true")
public class AiController {

    private final List<EventDto> demoEvents;

    // Session-based conversational memory
    private final Map<String, List<EventDto>> lastEventsBySession = new HashMap<>();
    private final Map<String, String> lastCategoryBySession = new HashMap<>();

    public AiController() {
        demoEvents = new ArrayList<>();

        demoEvents.add(createEvent(1L, "Tech Meetup",
                "A meetup for developers in Johannesburg",
                LocalDateTime.of(2026, 1, 20, 18, 30),
                "tech"));

        demoEvents.add(createEvent(2L, "AI Workshop",
                "Hands-on AI workshop for beginners",
                LocalDateTime.of(2026, 2, 5, 10, 0),
                "tech"));

        demoEvents.add(createEvent(3L, "Music Festival",
                "Annual music festival with local DJs",
                LocalDateTime.of(2026, 1, 25, 15, 0),
                "music"));

        demoEvents.add(createEvent(4L, "Live Concert",
                "Rock band live concert",
                LocalDateTime.of(2026, 2, 12, 19, 0),
                "music"));

        demoEvents.add(createEvent(5L, "Cooking Masterclass",
                "Learn to cook gourmet meals",
                LocalDateTime.of(2026, 1, 30, 11, 0),
                "cooking"));

        demoEvents.add(createEvent(6L, "Baking Workshop",
                "Hands-on baking class for beginners",
                LocalDateTime.of(2026, 2, 12, 10, 0),
                "cooking"));

        demoEvents.add(createEvent(7L, "Christmas Market",
                "Festive market with gifts and food",
                LocalDateTime.of(2026, 12, 15, 12, 0),
                "christmas"));

        demoEvents.add(createEvent(8L, "Xmas Carol Night",
                "Community carol singing",
                LocalDateTime.of(2026, 12, 20, 18, 0),
                "christmas"));
    }

    @PostMapping("/respond")
    public ResponseEntity<Map<String, Object>> respond(@RequestBody Map<String, String> payload) {

        String message = payload.getOrDefault("message", "").toLowerCase().trim();
        String sessionId = payload.getOrDefault("sessionId", "default");

        Map<String, Object> response = new HashMap<>();

        // Greeting
        if (message.isEmpty() || message.matches("hi|hello|hey")) {
            response.put("reply",
                    "Hello! 👋 Tell me what you're in the mood for — Tech, Music, Cooking, or Christmas 🎉");
            return ResponseEntity.ok(response);
        }

        // Casual acknowledgement handling
        if (isAcknowledgement(message)) {
            response.put("reply",
                    "Nice 😊 Want to explore more events or switch categories?\nTech, Music, Cooking, or Christmas?");
            return ResponseEntity.ok(response);
        }

        // Follow-up intent (details)
        if (isFollowUp(message)) {
            List<EventDto> lastEvents = lastEventsBySession.get(sessionId);

            if (lastEvents == null || lastEvents.isEmpty()) {
                response.put("reply",
                        "Tell me what kind of events you're interested in first 🙂");
                return ResponseEntity.ok(response);
            }

            List<Map<String, String>> details = lastEvents.stream()
                    .map(e -> Map.of(
                            "sender", "ai",
                            "text",
                            "🎟️ " + e.getTitle() +
                                    "\n📅 " + e.getDate() +
                                    "\n📍 Johannesburg" +
                                    "\n📝 " + e.getDescription()
                    ))
                    .toList();

            response.put("type", "events");
            response.put("messages", details);
            return ResponseEntity.ok(response);
        }

        // Category detection
        String category = detectCategory(message);

        if (category != null) {
            List<EventDto> filtered = demoEvents.stream()
                    .filter(e -> e.getCategory().equalsIgnoreCase(category))
                    .collect(Collectors.toList());

            lastCategoryBySession.put(sessionId, category);
            lastEventsBySession.put(sessionId, filtered);

            if (filtered.isEmpty()) {
                response.put("reply",
                        "I couldn’t find any " + category + " events right now.");
                return ResponseEntity.ok(response);
            }

            List<Map<String, String>> messages = filtered.stream()
                    .map(e -> Map.of(
                            "sender", "ai",
                            "text",
                            "🎉 " + e.getTitle() +
                                    "\n📅 " + e.getDate() +
                                    "\n💬 Want more details?"
                    ))
                    .toList();

            response.put("type", "events");
            response.put("messages", messages);
            return ResponseEntity.ok(response);
        }

        // Smart fallback
        response.put("reply",
                "I might have missed that 🙂 You can ask about events, say *tell me more*, or choose a category:\nTech, Music, Cooking, or Christmas.");
        return ResponseEntity.ok(response);
    }

    private boolean isFollowUp(String message) {
        return message.contains("more")
                || message.contains("details")
                || message.contains("tell me")
                || message.contains("about")
                || message.contains("when");
    }

    private boolean isAcknowledgement(String message) {
        return message.equals("yes")
                || message.equals("yeah")
                || message.equals("yep")
                || message.equals("ok")
                || message.equals("okay")
                || message.equals("cool")
                || message.equals("nice");
    }

    private String detectCategory(String message) {
        if (message.contains("christmas") || message.contains("xmas")) return "christmas";
        if (message.contains("tech") || message.contains("ai") || message.contains("startup")) return "tech";
        if (message.contains("music") || message.contains("concert") || message.contains("festival")) return "music";
        if (message.contains("cook") || message.contains("food") || message.contains("chef")) return "cooking";
        return null;
    }

    private EventDto createEvent(
            Long id,
            String title,
            String description,
            LocalDateTime date,
            String category
    ) {
        EventDto e = new EventDto();
        e.setId(id);
        e.setTitle(title);
        e.setDescription(description);
        e.setDate(date);
        e.setCategory(category);
        e.setCity("Johannesburg");
        e.setCreatedByUserId(1L);
        e.setOrganizerId(1L);
        return e;
    }
}
