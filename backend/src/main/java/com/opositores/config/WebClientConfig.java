package com.opositores.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    @Value("${ai.base-url}")
    private String aiBaseUrl;

    @Value("${ai.api-key}")
    private String aiApiKey;

    @Value("${ai.timeout-seconds:60}")
    private int timeoutSeconds;

    /** Cliente HTTP para llamadas a la API de IA */
    @Bean("aiWebClient")
    public WebClient aiWebClient() {
        return WebClient.builder()
                .baseUrl(aiBaseUrl)
                .defaultHeader("Authorization", "Bearer " + aiApiKey)
                .defaultHeader("Content-Type", "application/json")
                .codecs(c -> c.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))
                .build();
    }
}
