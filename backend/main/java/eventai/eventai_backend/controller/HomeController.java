package eventai.eventai_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Welcome to Event AI! This Event AI tells you what events are happening in JHB. " +
                "If you would like to continue, please click the Register or Login button. " +
                "Once registered, Lio will assist you to find your perfect event.";
    }
}
