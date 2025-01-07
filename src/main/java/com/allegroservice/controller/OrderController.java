package com.allegroservice.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@RestController
public class OrderController {

    private final RestTemplate template = new RestTemplate();
    @Value("${allegro.client-id}")
    private String clientId;

    @Value("${allegro.client-secret}")
    private String clientSecret;

    @GetMapping("/token")
    public String getToken() {
        try {
            System.out.println("get toekn test");
            // Kodowanie clientId:clientSecret w Base64
            String credentials = clientId + ":" + clientSecret;
            String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

            // Ustawianie nagłówków
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Basic " + encodedCredentials);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            // Body żądania
            String body = "grant_type=client_credentials";

            // Tworzenie żądania
            HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);

            // Wysyłanie żądania na produkcyjne środowisko Allegro
            ResponseEntity<String> responseEntity = template.exchange(
                    "https://allegro.pl/auth/oauth/token",
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            // Odpowiedź z serwera
            return responseEntity.getBody();

        } catch (Exception e) {
            return "Error fetching token: " + e.getMessage();
        }
    }

    @GetMapping("/test")
    public String test() {
        System.out.println("test");
        return "test";
    }
}
