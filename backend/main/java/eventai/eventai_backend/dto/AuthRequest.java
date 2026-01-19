package eventai.eventai_backend.dto;

import lombok.Data;

@Data // Generates getters, setters, equals, hashCode, and toString
public class AuthRequest {

    // User email address
    private String email;

    // User password (plain text before hashing)
    private String password;
}
