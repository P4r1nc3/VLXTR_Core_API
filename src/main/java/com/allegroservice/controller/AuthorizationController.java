package com.allegroservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorizationController {

    @Value("${allegro.client-id}")
    private String clientId;

    @Value("${allegro.redirect-uri}")
    private String redirectUri;

    @GetMapping("/auth-link")
    public String generateAuthorizationLink() {
        String authUrl = "https://allegro.pl/auth/oauth/authorize" +
                "?response_type=code" +
                "&client_id=" + clientId +
                "&redirect_uri=" + redirectUri;
        return authUrl;
    }
}
