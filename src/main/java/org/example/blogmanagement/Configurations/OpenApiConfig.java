package org.example.blogmanagement.Configurations;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
               title = "Blog Management API",
               description = "This is a simple API allows users to make blog posts and comment on them ",
               contact = @Contact(
                       name = "Nicole0509",
                       url = "https://github.com/Nicole0509/spring-boot-blog-management",
                       email = "nnicole0509@gmail.com"
               ),
               version = "1.0",
               license = @License(
                       name = "No license",
                       url = "https://some-url.com"
               ),
               termsOfService = "Some Terms of Service"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8081/"
                ),
                @Server(
                        description = "PROD ENV",
                        url = "https://github.com/Nicole0509/spring-boot-blog-management"
                )
        }
)
public class OpenApiConfig {
}
