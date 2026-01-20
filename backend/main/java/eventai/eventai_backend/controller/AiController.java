package eventai.eventai_backend.controller;

import eventai.eventai_backend.dto.EventDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.function.Predicate;
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

        LocalDateTime now = LocalDateTime.now();

        // Greeting
        if (message.isEmpty() || message.matches("hi|hello|hey|greetings")) {
            response.put("reply",
                    "Hello! I'm Lio 👋 Your smart event assistant. Tell me what you're in the mood for — Tech, Music, Cooking, or Christmas 🎉. Or ask about upcoming events!");
            return ResponseEntity.ok(response);
        }

        // Casual acknowledgement handling
        if (isAcknowledgement(message)) {
            String lastCategory = lastCategoryBySession.getOrDefault(sessionId, "");
            String categorySuggestion = lastCategory.isEmpty() ? "" : " more in " + lastCategory + " or";
            response.put("reply",
                    "Awesome! 😊 Want to explore" + categorySuggestion + " switch categories? Options: Tech, Music, Cooking, or Christmas.");
            return ResponseEntity.ok(response);
        }

        // Follow-up intent (details)
        if (isFollowUp(message)) {
            List<EventDto> lastEvents = lastEventsBySession.get(sessionId);
            if (lastEvents == null || lastEvents.isEmpty()) {
                response.put("reply",
                        "First, let's find some events! What category interests you? 😊");
                return ResponseEntity.ok(response);
            }
            // Check if specifying a particular event (simple number or title match)
            EventDto specificEvent = detectSpecificEvent(message, lastEvents);
            List<EventDto> eventsToDetail = specificEvent != null ? List.of(specificEvent) : lastEvents;

            List<Map<String, String>> details = eventsToDetail.stream()
                    .map(e -> Map.of(
                            "sender", "ai",
                            "text",
                            "🎟️ " + e.getTitle() +
                                    "\n📅 " + e.getDate() +
                                    "\n📍 " + e.getCity() +
                                    "\n📝 " + e.getDescription() +
                                    "\n\nWhat do you think? Book now or more info?"
                    ))
                    .toList();
            response.put("type", "events");
            response.put("messages", details);
            return ResponseEntity.ok(response);
        }

        // Detect time filter
        Predicate<EventDto> timeFilter = detectTimeFilter(message, now);

        // Category detection
        String category = detectCategory(message);

        // If no category but asking for events generally
        boolean isGeneralEventsQuery = category == null && (message.contains("events") || message.contains("what's on") || message.contains("upcoming"));

        List<EventDto> filtered;
        if (isGeneralEventsQuery) {
            filtered = demoEvents.stream()
                    .filter(timeFilter)
                    .filter(e -> e.getDate().isAfter(now)) // Always filter upcoming
                    .sorted(Comparator.comparing(EventDto::getDate))
                    .collect(Collectors.toList());
            lastCategoryBySession.put(sessionId, "all");
        } else if (category != null) {
            filtered = demoEvents.stream()
                    .filter(e -> e.getCategory().equalsIgnoreCase(category))
                    .filter(timeFilter)
                    .filter(e -> e.getDate().isAfter(now)) // Always filter upcoming
                    .sorted(Comparator.comparing(EventDto::getDate))
                    .collect(Collectors.toList());
            lastCategoryBySession.put(sessionId, category);
        } else {
            // Smart fallback
            response.put("reply",
                    "Hmm, not sure about that one 🙂 I'm Lio, here to help with events. Try asking about upcoming events, or categories like Tech, Music, Cooking, or Christmas.");
            return ResponseEntity.ok(response);
        }

        lastEventsBySession.put(sessionId, filtered);

        if (filtered.isEmpty()) {
            String catText = category != null ? category : "matching";
            response.put("reply",
                    "Sorry, no upcoming " + catText + " events found right now. Check back soon or try another category! 😊");
            return ResponseEntity.ok(response);
        }

        // Number the events for easier reference
        List<Map<String, String>> messages = new ArrayList<>();
        for (int i = 0; i < filtered.size(); i++) {
            EventDto e = filtered.get(i);
            messages.add(Map.of(
                    "sender", "ai",
                    "text",
                    (i + 1) + ". 🎉 " + e.getTitle() +
                            "\n📅 " + e.getDate() +
                            "\n💬 Say 'more about " + (i + 1) + "' for details!"
            ));
        }
        response.put("type", "events");
        response.put("messages", messages);
        return ResponseEntity.ok(response);
    }

    private boolean isFollowUp(String message) {
        return message.contains("more")
                || message.contains("details")
                || message.contains("tell me")
                || message.contains("about")
                || message.contains("when")
                || message.contains("how much")
                || message.contains("where");
    }

    private boolean isAcknowledgement(String message) {
        return message.equals("yes")
                || message.equals("yeah")
                || message.equals("yep")
                || message.equals("ok")
                || message.equals("okay")
                || message.equals("cool")
                || message.equals("nice")
                || message.equals("thanks")
                || message.equals("thank you");
    }

    private String detectCategory(String message) {
        if (message.contains("christmas") || message.contains("xmas") || message.contains("festive") || message.contains("holiday")) return "christmas";
        if (message.contains("tech") || message.contains("ai") || message.contains("startup") || message.contains("technology") || message.contains("programming") || message.contains("developers")) return "tech";
        if (message.contains("music") || message.contains("concert") || message.contains("festival") || message.contains("dj") || message.contains("live performance") || message.contains("band")) return "music";
        if (message.contains("cook") || message.contains("food") || message.contains("chef") || message.contains("baking") || message.contains("culinary") || message.contains("gourmet")) return "cooking";
        return null;
    }

    private Predicate<EventDto> detectTimeFilter(String message, LocalDateTime now) {
        if (message.contains("today")) {
            return e -> e.getDate().toLocalDate().equals(now.toLocalDate());
        } else if (message.contains("this week")) {
            LocalDateTime startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            LocalDateTime endOfWeek = startOfWeek.plusDays(6).withHour(23).withMinute(59);
            return e -> !e.getDate().isBefore(startOfWeek) && !e.getDate().isAfter(endOfWeek);
        } else if (message.contains("this month")) {
            LocalDateTime startOfMonth = now.withDayOfMonth(1);
            LocalDateTime endOfMonth = now.with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59);
            return e -> !e.getDate().isBefore(startOfMonth) && !e.getDate().isAfter(endOfMonth);
        }
        return e -> true; // No filter
    }

    private EventDto detectSpecificEvent(String message, List<EventDto> lastEvents) {
        // Simple detection: look for number like "1" or "about 2"
        for (int i = 0; i < lastEvents.size(); i++) {
            int num = i + 1;
            if (message.contains(String.valueOf(num))) {
                return lastEvents.get(i);
            }
        }
        // Or title match (partial)
        for (EventDto e : lastEvents) {
            if (message.contains(e.getTitle().toLowerCase())) {
                return e;
            }
        }
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
