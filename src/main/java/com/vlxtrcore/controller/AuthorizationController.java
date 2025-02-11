package com.vlxtrcore.controller;

import com.vlxtrcore.dto.allegro.TokenResponse;
import com.vlxtrcore.service.AuthorizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    public AuthorizationController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @GetMapping("/user/callback")
    public ResponseEntity<TokenResponse> handleAuthorizationCallback(@RequestParam("code") String authorizationCode) {
        return ResponseEntity.ok(authorizationService.exchangeAuthorizationCodeForToken(authorizationCode));
    }

    @GetMapping("/user/link")
    public ResponseEntity<String> generateAuthorizationLink() {
        return ResponseEntity.ok(authorizationService.generateAuthorizationUrl());
    }

    @GetMapping("/device/token")
    public ResponseEntity<TokenResponse> getToken() {
        return ResponseEntity.ok(authorizationService.fetchAccessToken());
    }
}
