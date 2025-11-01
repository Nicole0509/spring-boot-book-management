package org.example.blogmanagement.Configurations;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Blog Management API",
                description = "This is a simple API allows users to make blog posts and comment on them. This acts as a mini feature of social media platforms. ",
                contact = @Contact(
                        name = "Nicole0509",
                        url = "https://github.com/Nicole0509/spring-boot-blog-management"
                ),
                version = "1.0.0"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8081/"
                )
        }

)
public class OpenApiConfig {
}
