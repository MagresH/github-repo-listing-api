package com.example.githubrepolistingapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "github-repo-listing-api", version = "1.0",
        contact = @Contact(name = "MagresH", email = "magresh11@gmail.com")

        )
)
public class OpenApiConfig {

}