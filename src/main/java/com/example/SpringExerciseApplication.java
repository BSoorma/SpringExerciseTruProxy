package com.example;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(servers = {@Server(url = "${server.base}${server.servlet.contextPath}")})
@SpringBootApplication
public class SpringExerciseApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringExerciseApplication.class, args);
    }

}
