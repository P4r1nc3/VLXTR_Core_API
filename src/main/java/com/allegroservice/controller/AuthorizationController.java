package com.allegroservice.controller;

import com.allegroservice.model.TokenResponse;
import com.allegroservice.service.OrderService;
import com.allegroservice.service.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorizationController {

    @Value("${allegro.client-id}")
    private String clientId;

    @Value("${allegro.redirect-uri}")
    private String redirectUri;

    private final TokenService tokenService;

    public AuthorizationController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping()
    public ResponseEntity<TokenResponse> handleAuthorizationCallback(@RequestParam("code") String authorizationCode) {
        return ResponseEntity.ok(tokenService.exchangeAuthorizationCodeForToken(authorizationCode));
    }

    @GetMapping("/auth-link")
    public ResponseEntity<String> generateAuthorizationLink() {
        String authUrl = "https://allegro.pl/auth/oauth/authorize" +
                "?response_type=code" +
                "&client_id=" + clientId +
                "&redirect_uri=" + redirectUri;
        return ResponseEntity.ok(authUrl);
    }

    @GetMapping("/token")
    public ResponseEntity<TokenResponse> getToken() {
        return ResponseEntity.ok(tokenService.fetchAccessToken());
    }
}
