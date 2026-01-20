package eventai.eventai_backend.controller;

import eventai.eventai_backend.dto.EventDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ai")

public class AiController {

    private final List<EventDto> demoEvents;

    // Session-based conversational memory
    private final Map<String, List<EventDto>> lastEventsBySession = new HashMap<>();
    private final Map<String, String> lastCategoryBySession = new HashMap<>();

    public AiController() {
        demoEvents = new ArrayList<>();

demoEvents = new ArrayList<>();

demoEvents.add(createEvent(1L, "Tech Meetup",
        "Join fellow developers for an evening of networking and tech talks.\n\n" +
        "📍 Location: Newtown Junction, Johannesburg\n" +
        "🕒 Time: 18:30 – 21:30\n" +
        "👔 Dress Code: Smart casual\n" +
        "💰 Price: Free entry\n" +
        "🍕 Food & Drinks: Light snacks and refreshments provided\n" +
        "🌐 Website: www.jhbtechmeetup.co.za\n" +
        "📞 Contact: 011 234 5678",
        LocalDateTime.of(2026, 1, 20, 18, 30),
        "tech"));

demoEvents.add(createEvent(2L, "AI Workshop",
        "A hands-on beginner-friendly AI workshop covering fundamentals and real-world use cases.\n\n" +
        "📍 Location: WeWork, Rosebank\n" +
        "🕒 Time: 10:00 – 16:00\n" +
        "👔 Dress Code: Casual\n" +
        "💰 Price: R350 per person\n" +
        "🍔 Food & Drinks: Lunch and coffee included\n" +
        "🌐 Website: www.aiworkshopsa.co.za\n" +
        "📞 Contact: 010 987 6543",
        LocalDateTime.of(2026, 2, 5, 10, 0),
        "tech"));

demoEvents.add(createEvent(3L, "Music Festival",
        "An energetic outdoor music festival featuring top local DJs and live performances.\n\n" +
        "📍 Location: Mary Fitzgerald Square, Newtown\n" +
        "🕒 Time: 15:00 – Late\n" +
        "👕 Dress Code: Festival wear\n" +
        "💰 Price: R250\n" +
        "🍻 Food & Drinks: Food trucks and bars available\n" +
        "🌐 Website: www.jhbmusicfest.co.za\n" +
        "📞 Contact: 011 555 1122",
        LocalDateTime.of(2026, 1, 25, 15, 0),
        "music"));

demoEvents.add(createEvent(4L, "Live Concert",
        "Experience a high-energy rock concert with one of South Africa’s top bands.\n\n" +
        "📍 Location: Melrose Arch Piazza\n" +
        "🕒 Time: 19:00 – 22:00\n" +
        "👔 Dress Code: Casual / Band merch encouraged\n" +
        "💰 Price: R400\n" +
        "🍔 Food & Drinks: Restaurants and bars on-site\n" +
        "🌐 Website: www.livemusicjhb.co.za\n" +
        "📞 Contact: 010 333 8899",
        LocalDateTime.of(2026, 2, 12, 19, 0),
        "music"));

demoEvents.add(createEvent(5L, "Cooking Masterclass",
        "Learn how to prepare gourmet meals with a professional chef in an interactive class.\n\n" +
        "📍 Location: Randburg Culinary Studio\n" +
        "🕒 Time: 11:00 – 14:00\n" +
        "👔 Dress Code: Casual (aprons provided)\n" +
        "💰 Price: R500\n" +
        "🍽 Food & Drinks: Full meal included\n" +
        "🌐 Website: www.cooklikeapro.co.za\n" +
        "📞 Contact: 011 777 9090",
        LocalDateTime.of(2026, 1, 30, 11, 0),
        "cooking"));

demoEvents.add(createEvent(6L, "Baking Workshop",
        "A beginner-friendly baking workshop covering cakes, pastries, and bread.\n\n" +
        "📍 Location: Linden Community Hall\n" +
        "🕒 Time: 10:00 – 13:00\n" +
        "👕 Dress Code: Comfortable clothing\n" +
        "💰 Price: R300\n" +
        "🧁 Food & Drinks: Ingredients provided\n" +
        "🌐 Website: www.bakejoy.co.za\n" +
        "📞 Contact: 012 444 2211",
        LocalDateTime.of(2026, 2, 12, 10, 0),
        "cooking"));

demoEvents.add(createEvent(7L, "Christmas Market",
        "A festive outdoor market with handcrafted gifts, food stalls, and live entertainment.\n\n" +
        "📍 Location: Emmarentia Gardens\n" +
        "🕒 Time: 12:00 – 20:00\n" +
        "🎄 Dress Code: Festive casual\n" +
        "💰 Price: Free entry\n" +
        "🍔 Food & Drinks: Multiple vendors available\n" +
        "🌐 Website: www.jhbchristmasmarket.co.za\n" +
        "📞 Contact: 011 888 1212",
        LocalDateTime.of(2026, 12, 15, 12, 0),
        "christmas"));

demoEvents.add(createEvent(8L, "Xmas Carol Night",
        "An evening of community carol singing with live choir performances.\n\n" +
        "📍 Location: Randburg Civic Centre\n" +
        "🕒 Time: 18:00 – 20:00\n" +
        "🎄 Dress Code: Festive / Warm clothing\n" +
        "💰 Price: Free\n" +
        "☕ Food & Drinks: Hot chocolate and snacks available\n" +
        "🌐 Website: www.carolsjhb.co.za\n" +
        "📞 Contact: 010 222 3344",
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
