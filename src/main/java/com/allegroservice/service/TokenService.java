package com.allegroservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;

@Service
public class TokenService {

    @Value("${allegro.client-id}")
    private String clientId;

    @Value("${allegro.client-secret}")
    private String clientSecret;

    private final WebClient webClient;

    public TokenService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://allegro.pl").build();
    }

    public String fetchAccessToken() {
        try {
            // Kodowanie clientId:clientSecret w Base64
            String credentials = clientId + ":" + clientSecret;
            String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

            // Wysyłanie żądania z WebClient
            return webClient.post()
                    .uri("/auth/oauth/token")
                    .header("Authorization", "Basic " + encodedCredentials)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .bodyValue("grant_type=client_credentials")
                    .retrieve()
                    .bodyToMono(String.class) // Pobieranie odpowiedzi jako String (JSON)
                    .block(); // Blokowanie dla synchronizacji

        } catch (Exception e) {
            return "Error fetching token: " + e.getMessage();
        }
    }
}
