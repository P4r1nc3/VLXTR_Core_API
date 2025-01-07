package com.allegroservice.controller;

import com.allegroservice.dto.TokenResponse;
import com.allegroservice.service.AuthorizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    public AuthorizationController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @GetMapping()
    public ResponseEntity<TokenResponse> handleAuthorizationCallback(@RequestParam("code") String authorizationCode) {
        return ResponseEntity.ok(authorizationService.exchangeAuthorizationCodeForToken(authorizationCode));
    }

    @GetMapping("/auth-link")
    public ResponseEntity<String> generateAuthorizationLink() {
        return ResponseEntity.ok(authorizationService.generateAuthorizationUrl());
    }

    @GetMapping("/token")
    public ResponseEntity<TokenResponse> getToken() {
        return ResponseEntity.ok(authorizationService.fetchAccessToken());
    }
}
