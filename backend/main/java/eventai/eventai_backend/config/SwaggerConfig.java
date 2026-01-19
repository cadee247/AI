package eventai.eventai_backend.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // Marks this class as a Spring configuration class
public class SwaggerConfig {

    @Bean
    public OpenAPI eventAiOpenAPI() {
        // Create and configure the OpenAPI (Swagger) documentation
        return new OpenAPI()
                .info(new Info()
                        // API title shown in Swagger UI
                        .title("EventAI API")
                        // Short description of the API
                        .description("API documentation for EventAI")
                        // API version
                        .version("1.0.0"))
                .externalDocs(new ExternalDocumentation()
                        // Link to external documentation (GitHub repo)
                        .description("EventAI GitHub")
                        .url("https://github.com/yourusername/eventai"));
    }
}
