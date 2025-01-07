package com.allegroservice.controller;

import com.allegroservice.model.TokenResponse;
import com.allegroservice.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorizationController {

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
        return ResponseEntity.ok(tokenService.generateAuthorizationUrl());
    }

    @GetMapping("/token")
    public ResponseEntity<TokenResponse> getToken() {
        return ResponseEntity.ok(tokenService.fetchAccessToken());
    }
}
