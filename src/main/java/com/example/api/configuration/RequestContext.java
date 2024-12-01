package com.example.api.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RequestContext {

    @Bean
    RestClient restClient() {
        return RestClient.create();
    }

}
