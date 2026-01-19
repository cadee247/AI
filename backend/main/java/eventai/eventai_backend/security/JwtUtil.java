package eventai.eventai_backend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component // Marks this class as a Spring bean
public class JwtUtil {

    // Secret key for signing JWTs (HS256)
    private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Token expiration time: 10 hours
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 10;

    // Generate JWT token for a given username
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // Set username as subject
                .setIssuedAt(new Date()) // Token creation time
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Expiration time
                .signWith(key) // Sign with secret key
                .compact(); // Build token string
    }

    // Validate token matches username and is not expired
    public boolean validateToken(String token, String username) {
        String tokenUsername = extractUsername(token);
        return tokenUsername.equals(username) && !isTokenExpired(token);
    }

    // Extract username (subject) from token
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key) // Set key to validate signature
                .build()
                .parseClaimsJws(token) // Parse JWT
                .getBody()
                .getSubject(); // Return subject (username)
    }

    // Check if token is expired
    private boolean isTokenExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date()); // Expired if expiration date is before now
    }
}
