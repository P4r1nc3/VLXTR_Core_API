package com.allegroservice.controller;

import com.allegroservice.model.TokenResponse;
import com.allegroservice.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private final TokenService tokenService;

    public OrderController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping("/token")
    public ResponseEntity<TokenResponse> getToken() {
        TokenResponse tokenResponse = tokenService.fetchAccessToken();
        return ResponseEntity.ok(tokenResponse);
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
