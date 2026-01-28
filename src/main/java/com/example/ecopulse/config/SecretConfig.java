package com.example.ecopulse.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:secret.yaml", factory = YamlPropertySourceFactory.class)
public class SecretConfig {
}