// src/main/java/com/example/frattab/config/SendGridConfig.java
package com.example.frattab.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sendgrid.SendGrid;

@Configuration
public class SendGridConfig {

    @Value("${sendgrid.api-key}")
    private String apiKey;

    @Bean
    public SendGrid sendGridClient() {
        // Instantiate the SendGrid HTTP client
        return new SendGrid(apiKey);
    }
}
