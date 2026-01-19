package eventai.eventai_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data // Generates getters, setters, equals, hashCode, and toString
@AllArgsConstructor // Generates a constructor with all fields
public class AuthResponseDTO {

    // JWT token returned after successful login
    private String token;
}
