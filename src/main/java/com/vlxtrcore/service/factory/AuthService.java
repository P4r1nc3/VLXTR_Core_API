package com.vlxtrcore.service.factory;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final HttpServletRequest request;

    public AuthService(HttpServletRequest request) {
        this.request = request;
    }

    public String getBearerToken() {
        String authHeader = request.getHeader("Authorization");
        return authHeader.substring(7);
    }
}
