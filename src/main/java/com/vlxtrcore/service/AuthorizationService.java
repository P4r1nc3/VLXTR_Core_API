package com.vlxtrcore.service;

import com.vlxtrcore.dto.allegro.TokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;

@Service
public class AuthorizationService {

    @Value("${allegro.client-id}")
    private String clientId;

    @Value("${allegro.client-secret}")
    private String clientSecret;

    @Value("${allegro.redirect-uri}")
    private String redirectUri;

    private final WebClient webClient;

    public AuthorizationService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://allegro.pl").build();
    }

    public String generateAuthorizationUrl() {
        return "https://allegro.pl/auth/oauth/authorize" +
                "?response_type=code" +
                "&client_id=" + clientId +
                "&redirect_uri=" + redirectUri;
    }

    public TokenResponse fetchAccessToken() {
        try {
            String credentials = clientId + ":" + clientSecret;
            String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

            return webClient.post()
                    .uri("/auth/oauth/token")
                    .header("Authorization", "Basic " + encodedCredentials)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .bodyValue("grant_type=client_credentials")
                    .retrieve()
                    .bodyToMono(TokenResponse.class)
                    .block();

        } catch (Exception e) {
            throw new RuntimeException("Error fetching token: " + e.getMessage(), e);
        }
    }

    public TokenResponse exchangeAuthorizationCodeForToken(String authorizationCode) {
        try {
            String credentials = clientId + ":" + clientSecret;
            String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

            return webClient.post()
                    .uri("/auth/oauth/token")
                    .header("Authorization", "Basic " + encodedCredentials)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .bodyValue("grant_type=authorization_code&code=" + authorizationCode + "&redirect_uri=" + redirectUri)
                    .retrieve()
                    .bodyToMono(TokenResponse.class)
                    .block();

        } catch (Exception e) {
            throw new RuntimeException("Error exchanging authorization code: " + e.getMessage(), e);
        }
    }
}
