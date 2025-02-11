package com.allegroservice.service;

import com.allegroservice.dto.allegro.OffersResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AllegroService {

    private final WebClient webClient;

    public AllegroService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.allegro.pl").build();
    }

    // Fetch all offers
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

    // Fetch a specific offer by offerId
    public OffersResponse.Offer fetchOffer(String bearerToken, String offerId) {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/sale/product-offers/{offerId}")
                            .build(offerId))
                    .header("Authorization", bearerToken)
                    .header("Accept", "application/vnd.allegro.public.v1+json")
                    .retrieve()
                    .bodyToMono(OffersResponse.Offer.class) // Fetching a single offer object
                    .block();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching offer with ID " + offerId + ": " + e.getMessage(), e);
        }
    }
}
