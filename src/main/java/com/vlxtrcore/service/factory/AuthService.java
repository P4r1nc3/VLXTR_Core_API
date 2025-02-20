package com.vlxtrcore.service.factory;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final HttpServletRequest request;

    public AuthService(HttpServletRequest request) {
        this.request = request;
    }

    public String getBearerToken() {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new com.vlxtrcore.exception.ApiException(HttpStatus.UNAUTHORIZED,
                "Unauthorized",
                "Authorization header is missing or invalid.",
                "Ensure you are sending a valid Bearer token.");
    }
}
