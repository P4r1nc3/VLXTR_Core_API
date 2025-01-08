package com.allegroservice.service;

import com.allegroservice.dto.allegro.OrderResponse;
import com.allegroservice.dto.allegro.TokenResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class OrderService {

    private final AuthorizationService authorizationService;
    private final WebClient webClient;

    public OrderService(AuthorizationService authorizationService, WebClient.Builder webClientBuilder) {
        this.authorizationService = authorizationService;
        this.webClient = webClientBuilder.baseUrl("https://api.allegro.pl").build();
    }

    public List<OrderResponse> fetchOrders() {
        TokenResponse tokenResponse = authorizationService.fetchAccessToken();

        return webClient.get()
                .uri("/order/checkout-forms") // Endpoint zamówień
                .header("Authorization", "Bearer " + tokenResponse.getAccessToken())
                .header("Accept", "application/vnd.allegro.public.v1+json")
                .retrieve()
                .bodyToFlux(OrderResponse.class)
                .collectList()
                .block();
    }
}
