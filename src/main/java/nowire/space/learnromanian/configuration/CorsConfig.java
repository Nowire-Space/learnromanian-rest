package nowire.space.learnromanian.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {

    @Value("${webapp.url}")
    private String webAppUrl;

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/*")
                .allowedOrigins(webAppUrl)
                .allowedMethods("GET", "POST", "DELETE")
                .allowedHeaders("*");
//                .allowedHeaders("Access-Control-Request-Method", "Origin");
    }
}