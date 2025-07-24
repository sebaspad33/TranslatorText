package pe.edu.vallegrande.TranslateTextEdu.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .addServersItem(new Server().url("https://fictional-zebra-56w9g6xp9xrcv5w6-8080.app.github.dev")) 
                .info(new Info()
                    .title("Translate Azure API") 
                    .description("Microservicio de servicio cognitivo para traducir texto") 
                    .license(new License().name("Valle Grande").url("https://vallegrande.edu.pe"))
                    .version("1.0.0") 
                );
    }
}
