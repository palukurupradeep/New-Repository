package com.shaw.claims.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateClient {
    @Value("${rest.username}")
    private String username;
    @Value("${rest.password}")
    private String password;
    @Autowired
    private RestTemplateBuilder restTemplateBuilder;
    @Bean
    public RestTemplate buildRestTemplate(){
        return restTemplateBuilder.
                basicAuthentication(username, password).build();
    }
}
