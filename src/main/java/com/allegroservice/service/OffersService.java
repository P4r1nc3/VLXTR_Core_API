package com.allegroservice.service;

import com.allegroservice.model.OffersResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class OffersService {

    private final WebClient webClient;

    public OffersService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.allegro.pl").build();
    }

    public OffersResponse fetchOffers(String bearerToken) {
        try {
            return webClient.get()
                    .uri("/sale/offers")
                    .header("Authorization", bearerToken)
                    .header("Accept", "application/vnd.allegro.public.v1+json")
                    .retrieve()
                    .bodyToMono(OffersResponse.class)
                    .block();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching offers: " + e.getMessage(), e);
        }
    }
}
