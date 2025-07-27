package com.quyet.superapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class AppConfig {
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone(); // hoặc Clock.fixed(...) nếu cần cố định cho test
    }
}
