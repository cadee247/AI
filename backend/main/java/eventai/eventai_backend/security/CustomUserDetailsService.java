package eventai.eventai_backend.security;

import eventai.eventai_backend.model.User;
import eventai.eventai_backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service // Marks this class as a Spring service component
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // Inject UserRepository
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Find user by username, throw exception if not found
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Return Spring Security UserDetails object
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), // username
                user.getPassword(), // hashed password
                new ArrayList<>()   // authorities/roles (empty for now)
        );
    }
}
