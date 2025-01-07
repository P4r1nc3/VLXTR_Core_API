package com.allegroservice.config;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AllegroAuthClient {

    private static final String TOKEN_URL = "https://allegro.pl/auth/oauth/token";
    @Value("${allegro.client-id}")
    private String clientId;

    @Value("${allegro.client-secret}")
    private String clientSecret;

    public String getAccessToken() throws IOException {
        OkHttpClient client = new OkHttpClient();

        String credentials = Credentials.basic(clientId, clientSecret);

        Request request = new Request.Builder()
                .url(TOKEN_URL)
                .post(RequestBody.create("", MediaType.parse("application/x-www-form-urlencoded")))
                .addHeader("Authorization", credentials)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String responseBody = response.body() != null ? response.body().string() : "Brak treści odpowiedzi";
                throw new IOException("Unexpected code " + response + "\nTreść odpowiedzi: " + responseBody);
            }

            String responseBody = response.body().string();

            return new com.fasterxml.jackson.databind.ObjectMapper()
                    .readTree(responseBody)
                    .get("access_token").asText();
        }
    }
}
