package com.example.advancedrestapi.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
public class OpenApiConfig {
@Bean
    public OpenAPI openInfo(){
        return new OpenAPI().info(new Info().title("Rest api: account info api").
                description("This api is used to mimic real rest api for account information ").
                version("v1")
                .contact(
                        new Contact().
                                name("Abdirahman").
                                email("abdirahman.bashir88@gmail.com").
                                url("https:linkedIn/bashirabdi.com")
                        ).license(
                                new License().
                                        name("MIT23").
                                        url("www.cbc.com"))
        );
    }

}
