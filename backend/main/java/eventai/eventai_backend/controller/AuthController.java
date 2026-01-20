package eventai.eventai_backend.controller;

import eventai.eventai_backend.dto.AuthResponseDTO;
import eventai.eventai_backend.dto.LoginRequestDTO;
import eventai.eventai_backend.dto.RegisterRequestDTO;
import eventai.eventai_backend.dto.UserDTO;
import eventai.eventai_backend.model.User;
import eventai.eventai_backend.repository.UserRepository;
import eventai.eventai_backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController // Marks this class as a REST controller
@RequestMapping("/api/auth") // Base URL for auth endpoints

public class AuthController {

    @Autowired
    private UserRepository userRepository; // Access user data from database

    @Autowired
    private PasswordEncoder passwordEncoder; // Hash and verify passwords

    @Autowired
    private JwtUtil jwtUtil; // Generate JWT tokens

    @PostMapping("/register") // Register a new user
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO dto) {

        // Check if username already exists
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        // Create and save new user
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // Hash password
        userRepository.save(user);

        // Return basic user info (no password)
        return ResponseEntity.ok(new UserDTO(user.getId(), user.getUsername()));
    }

    @PostMapping("/login") // Authenticate user
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO dto) {

        // Find user by username
        User user = userRepository.findByUsername(dto.getUsername()).orElse(null);

        // Validate username and password
        if (user == null || !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        // Generate JWT token for authenticated user
        String token = jwtUtil.generateToken(user.getUsername());

        // Return token to frontend
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }
}