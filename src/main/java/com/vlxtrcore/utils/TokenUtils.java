package com.vlxtrcore.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class TokenUtils {
    private final HttpServletRequest request;

    public TokenUtils(HttpServletRequest request) {
        this.request = request;
    }

    public String getBearerToken() {
        String authHeader = request.getHeader("Authorization");
        return authHeader.substring(7);
    }
}
