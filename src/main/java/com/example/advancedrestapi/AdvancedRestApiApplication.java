package com.example.advancedrestapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Advanced Rest API", version = "2.0", description = "Account Information"))
public class AdvancedRestApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdvancedRestApiApplication.class, args);
    }

}
