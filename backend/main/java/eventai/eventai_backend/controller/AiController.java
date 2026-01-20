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
    private final Map<String, EventDto> lastDetailedEventBySession = new HashMap<>();

    public AiController() {
        demoEvents = new ArrayList<>();
        // Tech events
        demoEvents.add(createEvent(1L, "Tech Meetup",
                "Join fellow developers for an evening of networking and tech talks. Dive into the latest trends in software development, share your projects, and connect with industry experts.\n\n" +
                "📍 Location: Newtown Junction, Johannesburg\n" +
                "🕒 Time: 18:30 – 21:30\n" +
                "👔 Dress Code: Smart casual\n" +
                "💰 Price: Free entry\n" +
                "🍕 Food & Drinks: Light snacks and refreshments provided\n" +
                "🌐 Website: www.jhbtechmeetup.co.za\n" +
                "📞 Contact: 011 234 5678\n" +
                "Highlights: Guest speakers from leading tech companies, interactive Q&A sessions, and opportunities for collaboration on open-source projects.",
                LocalDateTime.of(2026, 1, 20, 18, 30),
                "tech"));
        demoEvents.add(createEvent(2L, "AI Workshop",
                "A hands-on beginner-friendly AI workshop covering fundamentals and real-world use cases. Learn about machine learning algorithms, build your first AI model, and explore ethical AI practices.\n\n" +
                "📍 Location: WeWork, Rosebank, Johannesburg\n" +
                "🕒 Time: 10:00 – 16:00\n" +
                "👔 Dress Code: Casual\n" +
                "💰 Price: R350 per person\n" +
                "🍔 Food & Drinks: Lunch and coffee included\n" +
                "🌐 Website: www.aiworkshopsa.co.za\n" +
                "📞 Contact: 010 987 6543\n" +
                "Highlights: Practical coding exercises with Python and TensorFlow, group discussions on AI applications in healthcare and finance, and certificates for participants.",
                LocalDateTime.of(2026, 2, 5, 10, 0),
                "tech"));
        demoEvents.add(createEvent(9L, "Blockchain Summit",
                "Explore the future of blockchain technology with experts from around the world. Discussions on cryptocurrencies, smart contracts, and decentralized finance.\n\n" +
                "📍 Location: Sandton Convention Centre, Johannesburg\n" +
                "🕒 Time: 09:00 – 17:00\n" +
                "👔 Dress Code: Business casual\n" +
                "💰 Price: R500\n" +
                "🍽 Food & Drinks: Buffet lunch and beverages provided\n" +
                "🌐 Website: www.blockchainsummitjhb.co.za\n" +
                "📞 Contact: 011 456 7890\n" +
                "Highlights: Keynote speeches by blockchain pioneers, hands-on demos of NFT creation, and networking with startups in the Web3 space.",
                LocalDateTime.of(2026, 3, 15, 9, 0),
                "tech"));
        demoEvents.add(createEvent(10L, "Cybersecurity Conference",
                "Stay ahead of cyber threats with insights from top security professionals. Topics include data privacy, ethical hacking, and emerging risks in IoT.\n\n" +
                "📍 Location: Gallagher Convention Centre, Midrand, Johannesburg\n" +
                "🕒 Time: 08:30 – 16:30\n" +
                "👔 Dress Code: Professional\n" +
                "💰 Price: R600\n" +
                "🍔 Food & Drinks: Morning tea, lunch, and afternoon snacks included\n" +
                "🌐 Website: www.cybersecjhb.co.za\n" +
                "📞 Contact: 012 345 6789\n" +
                "Highlights: Live hacking demonstrations, panel debates on AI in cybersecurity, and workshops on building secure networks.",
                LocalDateTime.of(2026, 4, 22, 8, 30),
                "tech"));
        demoEvents.add(createEvent(11L, "Startup Pitch Night",
                "Watch innovative startups pitch their ideas to investors and get feedback. A great opportunity for entrepreneurs and tech enthusiasts alike.\n\n" +
                "📍 Location: 22 on Sloane, Bryanston, Johannesburg\n" +
                "🕒 Time: 17:00 – 20:00\n" +
                "👔 Dress Code: Smart casual\n" +
                "💰 Price: R200\n" +
                "🍹 Food & Drinks: Cocktails and finger foods served\n" +
                "🌐 Website: www.startuppitchjhb.co.za\n" +
                "📞 Contact: 011 789 0123\n" +
                "Highlights: Live pitches from 10 startups, investor Q&A, and post-event networking with mentors and venture capitalists.",
                LocalDateTime.of(2026, 5, 10, 17, 0),
                "tech"));

        // Music events
        demoEvents.add(createEvent(3L, "Music Festival",
                "An energetic outdoor music festival featuring top local DJs and live performances. Enjoy a mix of electronic, afrobeat, and hip-hop under the stars.\n\n" +
                "📍 Location: Mary Fitzgerald Square, Newtown, Johannesburg\n" +
                "🕒 Time: 15:00 – Late\n" +
                "👕 Dress Code: Festival wear\n" +
                "💰 Price: R250\n" +
                "🍻 Food & Drinks: Food trucks and bars available\n" +
                "🌐 Website: www.jhbmusicfest.co.za\n" +
                "📞 Contact: 011 555 1122\n" +
                "Highlights: Multiple stages with international headliners, art installations, and dance zones for all ages.",
                LocalDateTime.of(2026, 1, 25, 15, 0),
                "music"));
        demoEvents.add(createEvent(4L, "Live Concert",
                "Experience a high-energy rock concert with one of South Africa’s top bands. Feel the thrill of live music with powerful vocals and guitar solos.\n\n" +
                "📍 Location: Melrose Arch Piazza, Johannesburg\n" +
                "🕒 Time: 19:00 – 22:00\n" +
                "👔 Dress Code: Casual / Band merch encouraged\n" +
                "💰 Price: R400\n" +
                "🍔 Food & Drinks: Restaurants and bars on-site\n" +
                "🌐 Website: www.livemusicjhb.co.za\n" +
                "📞 Contact: 010 333 8899\n" +
                "Highlights: Special guest appearances, pyrotechnics show, and an acoustic set for fans.",
                LocalDateTime.of(2026, 2, 12, 19, 0),
                "music"));
        demoEvents.add(createEvent(12L, "Jazz Night",
                "Immerse yourself in smooth jazz rhythms with talented local musicians. A relaxing evening of improvisation and classic tunes.\n\n" +
                "📍 Location: The Orbit Jazz Club, Braamfontein, Johannesburg\n" +
                "🕒 Time: 20:00 – 23:00\n" +
                "👔 Dress Code: Smart casual\n" +
                "💰 Price: R150\n" +
                "🍷 Food & Drinks: Wine bar and light meals available\n" +
                "🌐 Website: www.orbitjazz.co.za\n" +
                "📞 Contact: 011 123 4567\n" +
                "Highlights: Jam sessions with audience participation, featured soloists, and a cozy intimate venue atmosphere.",
                LocalDateTime.of(2026, 3, 8, 20, 0),
                "music"));
        demoEvents.add(createEvent(13L, "Hip-Hop Battle",
                "Witness fierce rap battles and freestyle sessions from up-and-coming artists. A night of raw energy and lyrical talent.\n\n" +
                "📍 Location: Zone 6 Venue, Soweto, Johannesburg\n" +
                "🕒 Time: 18:00 – 00:00\n" +
                "👕 Dress Code: Streetwear\n" +
                "💰 Price: R300\n" +
                "🍻 Food & Drinks: Street food vendors and bars on-site\n" +
                "🌐 Website: www.hiphopbattlejhb.co.za\n" +
                "📞 Contact: 010 456 7890\n" +
                "Highlights: Live beatboxing, graffiti art displays, and prizes for crowd favorites.",
                LocalDateTime.of(2026, 4, 18, 18, 0),
                "music"));
        demoEvents.add(createEvent(14L, "Classical Symphony",
                "Enjoy a captivating performance by the Johannesburg Philharmonic Orchestra, featuring timeless pieces from Beethoven and Mozart.\n\n" +
                "📍 Location: Linder Auditorium, Parktown, Johannesburg\n" +
                "🕒 Time: 19:30 – 21:30\n" +
                "👔 Dress Code: Formal\n" +
                "💰 Price: R450\n" +
                "🍸 Food & Drinks: Pre-show cocktails available\n" +
                "🌐 Website: www.jhbsymphony.co.za\n" +
                "📞 Contact: 011 890 1234\n" +
                "Highlights: Guest conductor from Europe, thematic program notes, and post-concert meet-and-greet with musicians.",
                LocalDateTime.of(2026, 5, 25, 19, 30),
                "music"));

        // Cooking events
        demoEvents.add(createEvent(5L, "Cooking Masterclass",
                "Learn how to prepare gourmet meals with a professional chef in an interactive class. Focus on international cuisines with fresh, local ingredients.\n\n" +
                "📍 Location: Randburg Culinary Studio, Johannesburg\n" +
                "🕒 Time: 11:00 – 14:00\n" +
                "👔 Dress Code: Casual (aprons provided)\n" +
                "💰 Price: R500\n" +
                "🍽 Food & Drinks: Full meal included\n" +
                "🌐 Website: www.cooklikeapro.co.za\n" +
                "📞 Contact: 011 777 9090\n" +
                "Highlights: Step-by-step guidance on techniques like sous-vide and plating, wine pairing tips, and take-home recipes.",
                LocalDateTime.of(2026, 1, 30, 11, 0),
                "cooking"));
        demoEvents.add(createEvent(6L, "Baking Workshop",
                "A beginner-friendly baking workshop covering cakes, pastries, and bread. Discover the secrets to perfect dough and decadent frostings.\n\n" +
                "📍 Location: Linden Community Hall, Johannesburg\n" +
                "🕒 Time: 10:00 – 13:00\n" +
                "👕 Dress Code: Comfortable clothing\n" +
                "💰 Price: R300\n" +
                "🧁 Food & Drinks: Ingredients provided, taste your creations\n" +
                "🌐 Website: www.bakejoy.co.za\n" +
                "📞 Contact: 012 444 2211\n" +
                "Highlights: Hands-on mixing and decorating, tips for gluten-free alternatives, and packaging to take treats home.",
                LocalDateTime.of(2026, 2, 12, 10, 0),
                "cooking"));
        demoEvents.add(createEvent(15L, "Street Food Tour",
                "Embark on a guided tour of Johannesburg's best street food spots, tasting diverse flavors from African to Asian fusion.\n\n" +
                "📍 Location: Starting at Maboneng Precinct, Johannesburg\n" +
                "🕒 Time: 12:00 – 15:00\n" +
                "👕 Dress Code: Casual and comfortable shoes\n" +
                "💰 Price: R400\n" +
                "🍲 Food & Drinks: Samples from various vendors included\n" +
                "🌐 Website: www.streetfoodtourjhb.co.za\n" +
                "📞 Contact: 011 567 8901\n" +
                "Highlights: Stories behind each dish, interactions with local chefs, and dietary accommodations for vegans and allergies.",
                LocalDateTime.of(2026, 3, 20, 12, 0),
                "cooking"));
        demoEvents.add(createEvent(16L, "Wine and Cheese Pairing",
                "Discover the art of pairing South African wines with artisanal cheeses in a relaxed setting.\n\n" +
                "📍 Location: Fourways Farmers Market, Johannesburg\n" +
                "🕒 Time: 16:00 – 18:00\n" +
                "👔 Dress Code: Casual\n" +
                "💰 Price: R350\n" +
                "🍷 Food & Drinks: Wine tastings and cheese platters provided\n" +
                "🌐 Website: www.winecheesejhb.co.za\n" +
                "📞 Contact: 010 123 4567\n" +
                "Highlights: Expert sommelier guidance, regional wine education, and tips for hosting your own pairings.",
                LocalDateTime.of(2026, 4, 5, 16, 0),
                "cooking"));
        demoEvents.add(createEvent(17L, "Vegan Cooking Class",
                "Master plant-based cooking with creative recipes that are healthy and delicious, using seasonal produce.\n\n" +
                "📍 Location: Greenside Kitchen Studio, Johannesburg\n" +
                "🕒 Time: 09:00 – 12:00\n" +
                "👕 Dress Code: Casual\n" +
                "💰 Price: R450\n" +
                "🥦 Food & Drinks: All ingredients and tastings included\n" +
                "🌐 Website: www.vegancookjhb.co.za\n" +
                "📞 Contact: 011 234 5678\n" +
                "Highlights: Focus on nutrition, alternative ingredient swaps, and sustainable cooking practices.",
                LocalDateTime.of(2026, 5, 15, 9, 0),
                "cooking"));

        // Christmas events
        demoEvents.add(createEvent(7L, "Christmas Market",
                "A festive outdoor market with handcrafted gifts, food stalls, and live entertainment. Browse unique artisan wares and enjoy the holiday spirit.\n\n" +
                "📍 Location: Emmarentia Gardens, Johannesburg\n" +
                "🕒 Time: 12:00 – 20:00\n" +
                "🎄 Dress Code: Festive casual\n" +
                "💰 Price: Free entry\n" +
                "🍔 Food & Drinks: Multiple vendors available\n" +
                "🌐 Website: www.jhbchristmasmarket.co.za\n" +
                "📞 Contact: 011 888 1212\n" +
                "Highlights: Santa photo ops, live music from carolers, and workshops for making holiday decorations.",
                LocalDateTime.of(2026, 12, 15, 12, 0),
                "christmas"));
        demoEvents.add(createEvent(8L, "Xmas Carol Night",
                "An evening of community carol singing with live choir performances. Gather with friends and family for heartwarming holiday songs.\n\n" +
                "📍 Location: Randburg Civic Centre, Johannesburg\n" +
                "🕒 Time: 18:00 – 20:00\n" +
                "🎄 Dress Code: Festive / Warm clothing\n" +
                "💰 Price: Free\n" +
                "☕ Food & Drinks: Hot chocolate and snacks available\n" +
                "🌐 Website: www.carolsjhb.co.za\n" +
                "📞 Contact: 010 222 3344\n" +
                "Highlights: Professional choir leads, audience sing-alongs, and festive lighting displays.",
                LocalDateTime.of(2026, 12, 20, 18, 0),
                "christmas"));
        demoEvents.add(createEvent(18L, "Holiday Lights Tour",
                "Take a magical bus tour through Johannesburg's best holiday light displays, with stops for photos and treats.\n\n" +
                "📍 Location: Starting at Montecasino, Fourways, Johannesburg\n" +
                "🕒 Time: 19:00 – 21:00\n" +
                "🎄 Dress Code: Warm and festive\n" +
                "💰 Price: R250\n" +
                "🍪 Food & Drinks: Cookies and mulled wine included\n" +
                "🌐 Website: www.holidaylightsjhb.co.za\n" +
                "📞 Contact: 011 345 6789\n" +
                "Highlights: Guided narration of displays, family-friendly activities, and surprise holiday giveaways.",
                LocalDateTime.of(2026, 12, 10, 19, 0),
                "christmas"));
        demoEvents.add(createEvent(19L, "Festive Baking Session",
                "Bake holiday treats like gingerbread and mince pies in a fun, interactive group setting.\n\n" +
                "📍 Location: Melville Community Centre, Johannesburg\n" +
                "🕒 Time: 14:00 – 17:00\n" +
                "🎄 Dress Code: Casual (aprons provided)\n" +
                "💰 Price: R300\n" +
                "🧁 Food & Drinks: Ingredients and tastings provided\n" +
                "🌐 Website: www.festivebakejhb.co.za\n" +
                "📞 Contact: 010 567 8901\n" +
                "Highlights: Recipes inspired by global traditions, decorating contests, and take-home goodies.",
                LocalDateTime.of(2026, 12, 5, 14, 0),
                "christmas"));
        demoEvents.add(createEvent(20L, "New Year's Eve Countdown Party",
                "Ring in the new year with fireworks, live music, and a festive countdown in the heart of Johannesburg.\n\n" +
                "📍 Location: Nelson Mandela Square, Sandton, Johannesburg\n" +
                "🕒 Time: 20:00 – 01:00\n" +
                "🎉 Dress Code: Party attire\n" +
                "💰 Price: R500\n" +
                "🍾 Food & Drinks: Champagne toast and buffet included\n" +
                "🌐 Website: www.nyejhb.co.za\n" +
                "📞 Contact: 011 678 9012\n" +
                "Highlights: DJ sets, dance floors, and a spectacular fireworks show at midnight.",
                LocalDateTime.of(2026, 12, 31, 20, 0),
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

        // Recall previous query
        if (isRecall(message)) {
            String lastCategory = lastCategoryBySession.getOrDefault(sessionId, "");
            List<EventDto> lastEvents = lastEventsBySession.get(sessionId);
            if (lastCategory.isEmpty() || lastEvents == null || lastEvents.isEmpty()) {
                response.put("reply", "I don't have any previous queries from you yet. Let's start fresh—what events interest you? 😊");
                return ResponseEntity.ok(response);
            }
            response.put("reply", "Previously, you asked about " + lastCategory + " events. Here's a quick recap:");
            List<Map<String, String>> messages = new ArrayList<>();
            for (int i = 0; i < lastEvents.size(); i++) {
                EventDto e = lastEvents.get(i);
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

        // Booking intent
        if (isBooking(message)) {
            EventDto lastDetailed = lastDetailedEventBySession.get(sessionId);
            if (lastDetailed == null) {
                response.put("reply", "Which event would you like to book? Tell me more details first! 😊");
                return ResponseEntity.ok(response);
            }
            response.put("reply",
                    "To book for " + lastDetailed.getTitle() + ", visit the website: " + extractWebsite(lastDetailed.getDescription()) +
                            " or contact: " + extractContact(lastDetailed.getDescription()) + ". Let me know if you need more help! 🎟️");
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
            // Check if specifying particular events (now supports multiple)
            List<EventDto> specificEvents = detectSpecificEvents(message, lastEvents);
            List<EventDto> eventsToDetail = !specificEvents.isEmpty() ? specificEvents : lastEvents;

            List<Map<String, String>> details = eventsToDetail.stream()
                    .map(e -> Map.of(
                            "sender", "ai",
                            "text",
                            "🎟️ " + e.getTitle() +
                                    "\n📅 " + e.getDate() +
                                    "\n📍 " + e.getCity() +
                                    "\n📝 " + e.getDescription() +
                                    "\n\nWhat do you think? Want more events or details?"
                    ))
                    .toList();
            // Update last detailed if single
            if (eventsToDetail.size() == 1) {
                lastDetailedEventBySession.put(sessionId, eventsToDetail.get(0));
            }
            response.put("type", "events");
            response.put("messages", details);
            return ResponseEntity.ok(response);
        }

        // Detect time filter (now with fuzzy)
        Predicate<EventDto> timeFilter = detectTimeFilter(message, now);

        // Category detection (now supports multiple categories)
        List<String> categories = detectCategories(message);

        // If no category but asking for events generally
        boolean isGeneralEventsQuery = categories.isEmpty() && (fuzzyContains(message, "events") || fuzzyContains(message, "what's on") || fuzzyContains(message, "upcoming"));

        List<EventDto> filtered;
        if (isGeneralEventsQuery) {
            filtered = demoEvents.stream()
                    .filter(timeFilter)
                    .filter(e -> e.getDate().isAfter(now)) // Always filter upcoming
                    .sorted(Comparator.comparing(EventDto::getDate))
                    .collect(Collectors.toList());
            lastCategoryBySession.put(sessionId, "all");
        } else if (!categories.isEmpty()) {
            filtered = demoEvents.stream()
                    .filter(e -> categories.contains(e.getCategory().toLowerCase()))
                    .filter(timeFilter)
                    .filter(e -> e.getDate().isAfter(now)) // Always filter upcoming
                    .sorted(Comparator.comparing(EventDto::getDate))
                    .collect(Collectors.toList());
            lastCategoryBySession.put(sessionId, String.join(" and ", categories));
        } else {
            // Smart fallback with typo suggestion hint
            response.put("reply",
                    "Hmm, not sure about that one—maybe a typo? 🙂 I'm Lio, here to help with events. Try asking about upcoming events, or categories like Tech, Music, Cooking, or Christmas.");
            return ResponseEntity.ok(response);
        }

        lastEventsBySession.put(sessionId, filtered);

        if (filtered.isEmpty()) {
            String catText = !categories.isEmpty() ? String.join(", ", categories) : "matching";
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
        return fuzzyContains(message, "more")
                || fuzzyContains(message, "details")
                || fuzzyContains(message, "tell me")
                || fuzzyContains(message, "about")
                || fuzzyContains(message, "when")
                || fuzzyContains(message, "how much")
                || fuzzyContains(message, "where");
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

    private boolean isBooking(String message) {
        return fuzzyContains(message, "book")
                || fuzzyContains(message, "register")
                || fuzzyContains(message, "sign up")
                || fuzzyContains(message, "tickets");
    }

    private boolean isRecall(String message) {
        return fuzzyContains(message, "previous")
                || fuzzyContains(message, "what did i ask")
                || fuzzyContains(message, "last query")
                || fuzzyContains(message, "history");
    }

    private List<String> detectCategories(String message) {
        // Expanded keywords per category
        Map<String, List<String>> categoryKeywords = Map.of(
                "christmas", List.of("christmas", "xmas", "festive", "holiday"),
                "tech", List.of("tech", "ai", "startup", "technology", "programming", "developers"),
                "music", List.of("music", "concert", "festival", "dj", "live performance", "band"),
                "cooking", List.of("cook", "food", "chef", "baking", "culinary", "gourmet")
        );

        Set<String> categories = new HashSet<>();
        for (Map.Entry<String, List<String>> entry : categoryKeywords.entrySet()) {
            for (String keyword : entry.getValue()) {
                if (fuzzyContains(message, keyword)) {
                    categories.add(entry.getKey().toLowerCase());
                }
            }
        }
        return new ArrayList<>(categories);
    }

    private Predicate<EventDto> detectTimeFilter(String message, LocalDateTime now) {
        if (fuzzyContains(message, "today")) {
            return e -> e.getDate().toLocalDate().equals(now.toLocalDate());
        } else if (fuzzyContains(message, "this week")) {
            LocalDateTime startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            LocalDateTime endOfWeek = startOfWeek.plusDays(6).withHour(23).withMinute(59);
            return e -> !e.getDate().isBefore(startOfWeek) && !e.getDate().isAfter(endOfWeek);
        } else if (fuzzyContains(message, "this month")) {
            LocalDateTime startOfMonth = now.withDayOfMonth(1);
            LocalDateTime endOfMonth = now.with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59);
            return e -> !e.getDate().isBefore(startOfMonth) && !e.getDate().isAfter(endOfMonth);
        }
        return e -> true; // No filter
    }

    private List<EventDto> detectSpecificEvents(String message, List<EventDto> lastEvents) {
        List<EventDto> specifics = new ArrayList<>();
        // Detect numbers
        for (int i = 0; i < lastEvents.size(); i++) {
            int num = i + 1;
            if (message.contains(String.valueOf(num))) {
                specifics.add(lastEvents.get(i));
            }
        }
        // Or title matches (fuzzy partial)
        for (EventDto e : lastEvents) {
            String[] titleWords = e.getTitle().toLowerCase().split("\\s+");
            for (String word : titleWords) {
                if (fuzzyContains(message, word) && !specifics.contains(e)) {
                    specifics.add(e);
                }
            }
        }
        return specifics;
    }

    // New: Fuzzy matching utility using Levenshtein distance
    private boolean fuzzyContains(String text, String keyword) {
        // Simple contains for exact match first
        if (text.contains(keyword)) {
            return true;
        }
        // Split text into words and check fuzzy match per word
        String[] words = text.split("\\s+");
        for (String word : words) {
            if (levenshteinDistance(word, keyword) <= Math.max(1, keyword.length() / 3)) { // Threshold: 1-2 edits based on length
                return true;
            }
        }
        return false;
    }

    // New: Levenshtein distance implementation (edit distance)
    private int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = min(dp[i - 1][j - 1] + (s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1),
                            dp[i - 1][j] + 1,
                            dp[i][j - 1] + 1);
                }
            }
        }
        return dp[s1.length()][s2.length()];
    }

    private int min(int... numbers) {
        return Arrays.stream(numbers).min().orElse(Integer.MAX_VALUE);
    }

    // Helper to extract website from description
    private String extractWebsite(String description) {
        // Simple regex or string find; assume format 🌐 Website: www.example.com
        int start = description.indexOf("🌐 Website: ") + 12;
        int end = description.indexOf("\n", start);
        return description.substring(start, end);
    }

    // Helper to extract contact from description
    private String extractContact(String description) {
        // Simple regex or string find; assume format 📞 Contact: 011 234 5678
        int start = description.indexOf("📞 Contact: ") + 12;
        int end = description.indexOf("\n", start);
        if (end == -1) end = description.length();
        return description.substring(start, end);
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
