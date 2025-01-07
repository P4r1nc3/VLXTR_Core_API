package com.allegroservice.controller;

import com.allegroservice.service.TokenService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private final TokenService tokenService;

    public OrderController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping("/token")
    public String getToken() {
        return tokenService.fetchAccessToken();
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
